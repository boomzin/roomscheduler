package com.example.roomscheduler.repository;

import com.example.roomscheduler.error.IllegalRequestDataException;
import com.example.roomscheduler.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e WHERE upper(e.duration) > current_timestamp")
    List<Event> getAllActualEvents();

    @Query("SELECT e FROM Event e WHERE lower(e.duration) > current_timestamp AND e.status='STATELESS'")
    List<Event> getAllActualStatelessEvents();

    @Query("SELECT e FROM Event e WHERE upper(e.duration) > current_timestamp AND e.status='CONFIRMED'")
    List<Event> getAllConfirmedActualEvents();

    @Query(value = "SELECT * FROM event e WHERE upper(e.duration) > current_timestamp AND (e.status='CONFIRMED' OR e.user_id=:id)", nativeQuery = true)
    List<Event> getAllActualEventsForUser(int id);

    @Query(value = "SELECT e.id FROM event e WHERE e.room_id=:roomId AND e.status='CONFIRMED' AND e.duration && CAST(:duration AS tsrange)", nativeQuery = true)
    List<Integer> getConfirmedIntersections(int roomId, String duration);

    @Query(value = "SELECT * FROM event e WHERE e.room_id=:roomId AND e.status='STATELESS' AND e.duration && CAST(:duration AS tsrange) AND lower(e.duration) > current_timestamp", nativeQuery = true)
    List<Event> getActualStatelessIntersections(int roomId, String duration);

    @Query(value = "SELECT * FROM Event e WHERE e.id = :id and e.user_id = :userId", nativeQuery = true)
    Optional<Event> get(int id, int userId);

    default Event checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new IllegalRequestDataException("Event id=" + id + " doesn't belong to User id=" + userId));
    }

    @Query("SELECT e FROM Event e WHERE e.id = :id and e.status = 'STATELESS'")
    Optional<Event> getStatelessById(int id);

    @Query(value = "SELECT e.room_id FROM Event e WHERE e.id = :id", nativeQuery = true)
    int getRoomId(int id);
}
