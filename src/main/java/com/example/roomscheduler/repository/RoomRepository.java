package com.example.roomscheduler.repository;

import com.example.roomscheduler.model.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.roomscheduler.util.ValidationUtil.checkModification;

@Transactional(readOnly = true)
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @EntityGraph(attributePaths = {"events"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Room r WHERE r.id=?1")
    Optional<Room> getWithEvents(int id);

    @Query("SELECT r FROM Room r JOIN FETCH r.events e WHERE r.id=?1 AND upper(e.duration) > current_timestamp")
    Optional<Room> getWithEventsActualEvents(int id);

    @Query("SELECT r FROM Room r WHERE r.description=?1")
    Optional<Room> getByDescription(String description);

    @Query("SELECT DISTINCT r FROM Room r JOIN FETCH r.events e WHERE upper(e.duration) > current_timestamp ORDER BY r.description")
    List<Room> getAllWithEventsActualEvents();

    @EntityGraph(attributePaths = {"events"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Room r ORDER BY r.description")
    List<Room> getAllWithEvents();

    @Query("SELECT r FROM Room r JOIN FETCH r.events e WHERE r.id=?1 AND e.status='CONFIRMED'")
    Optional<Room> getWithConfirmedEvents(int id);

    @Query("SELECT r FROM Room r JOIN FETCH r.events e WHERE r.id=?1 AND upper(e.duration) > current_timestamp AND e.status='CONFIRMED'")
    Optional<Room> getWithConfirmedEventsActualEvents(int id);

    @Query("SELECT DISTINCT r FROM Room r JOIN FETCH r.events e WHERE e.status='CONFIRMED' ORDER BY r.description")
    List<Room> getAllWithConfirmedEvents();

    @Query("SELECT DISTINCT r FROM Room r JOIN FETCH r.events e WHERE e.status='CONFIRMED' AND upper(e.duration) > current_timestamp ORDER BY r.description")
    List<Room> getAllWithConfirmedEventsActualEvents();

    @Modifying
    @Query("DELETE FROM Room r WHERE r.id=:id")
    int delete(int id);

    @Transactional
    default void deleteExisted(int id) {
        checkModification(delete(id), id);
    }
}
