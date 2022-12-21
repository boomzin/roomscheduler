[Приложение для бронирования переговорных комнат](https://github.com/boomzin/roomscheduler)
===============================

С помощью приложения сотрудники могут резервировать переговорную комнату на определенное время,  а также получать инофрмацию о том, что офис менеджер подтвердил или отклонил такую возможность. Офис менеджер может подтвердить или отклонить бронирование переговорных пользователем, а также отредактировать информацию о переговорной или добавить новую комнату или удалить уже имеющуюся.


### Возможности приложения:
- четыре пользовательские роли: 
    - администратор;
    - менеджер комнат и расписания;
    - рядовой сотрудник;
    - анонимный пользователь.
- администраторы имеют возможность:
  - создавать, удалять, просматривать и назначать роли пользователям; 
- менеджер имеет возможность:
  - создавать, удалять, просматривать и изменять свойства переговорных комнат;
  - просматривать, отклонять или подтверждать бронирования переговорных.
- рядовые пользователи имеют возможность:
  - просматривать переговорные;
  - просматривать все бринорования, создавать, удалять и изменять свои бронирования.
- анонимные пользователи имеют возможность:
  - регистрироваться с целью создания своей учетной записи рядового сотрудника;

-------------------
### Для запуска необходимы:
- java
- maven
- docker


### Порядок запуска:
<pre>
git clone https://github.com/boomzin/roomscheduler
cd roomscheduler/
bash startDatabase.sh
mvn package -Dmaven.test.skip
java -jar target/roomscheduler-0.0.1-SNAPSHOT.jar
</pre>


### После запуска приложения станет доступна REST API докумментация

- Open API докумментация [link](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/):
- учетные записи пользователей:
    * администратор:
      * email: admin@gmail.com
      * password: admin
    * менеджер:
        * email: manager@gmail.com
        * password: manager
    * рядовой сотрудник:
        * email: user1@gmail.com
        * password: user1pass
        * email: user2@gmail.com
        * password: user2pass

-------------------

### Подробности реализации

#### база данных postgres в контейнере доступная на порту 15432 с таблицами:
- **USERS** - содержит сущности пользователей
  - соответствует `User.class`
  - имеет ограничение уникальности значений поля `email` 
- **USER_ROLES** - содержит роли пользователей
  - соответствует `Role.enum`
  - имеет ограничение уникальности значений полей `(user_id, role)`
- **ROOM** - содержит сущности переговорных
  - соответствует `Room.class`
  - имеет ограничение уникальности значений поля `description`
- **EVENT** - содержит сущности бронирований
  - соответствует `Event.class`
  - имеет ограничение уникальности значений поля `duration` - не допускается пересечение во времени бронирований со статусом `CONFIRMED` 


#### процесс бронирования:
- авторизованному пользователю с ролью сотрудника доступны:
  - список всех переговорных;
  - описание конкретной переговорноцй;
  - фильтр списка переговорных по их вместимости и оборудованию;
  - список свободных переговорных в заданный диапазон времени.
- после выбора переговорной сотрудник может создать бронирование переговорной, просмотреть чужие бронирования или проверить статус своих ранее созданых бронирований.
- допускается создание пересекающихся во времени бронирований без статуса `STATELESS`.
- допускается удаление собственных бронирований если они не имеют статус утверждено `CONFIRMED`;
- пользователь со статусом менеджера может просмотреть список бронирований без статуса и
  - либо отказать в бронировании, установив бронированию статус отвергнуто `REJECTED`;
  - либо утвердить бронирование, при этом не допускается пересечение во времени бронирований со статусом утверждено `CONFIRMED`;
- менеджер не может сменить статус уже утвержденному бронированию, но может его удалить;
- менеджер не может удалить утвержденное бронирование если событие уже завершилось


-------------------

> 2021. Ernest Aidinov. <a href="mailto:ernest.aidinov@gmail.com">ernest.aidinov@gmail.com</a>

-------------------