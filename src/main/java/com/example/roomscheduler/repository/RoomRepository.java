package com.example.roomscheduler.repository;

import com.example.roomscheduler.model.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RoomRepository extends BaseRepository<Room>, JpaSpecificationExecutor<Room> {

    @EntityGraph(attributePaths = {"events"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Room r WHERE r.id=?1")
    Optional<Room> getWithEvents(int id);

    @Query("SELECT r FROM Room r JOIN FETCH r.events e WHERE r.id=?1 AND upper(e.duration) > current_timestamp")
    Optional<Room> getWithEventsActualEvents(int id);

    @Query("SELECT r FROM Room r WHERE r.description=?1")
    Optional<Room> getByDescription(String description);

    @Query("SELECT DISTINCT r FROM Room r JOIN FETCH r.events e WHERE upper(e.duration) > current_timestamp ORDER BY r.description")
    List<Room> getAllWithActualEvents();

    @Query(value = "select * from Room r " +
            "where r.id not in (select e.room_id from event e " +
            "where lower(e.duration)> current_timestamp AND e.duration && CAST(:duration AS tsrange) AND e.status='CONFIRMED')"
            , nativeQuery = true)
    List<Room> getFreeInRange(String duration);

    @EntityGraph(attributePaths = {"events"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Room r ORDER BY r.description")
    List<Room> getAllWithEvents();
}
