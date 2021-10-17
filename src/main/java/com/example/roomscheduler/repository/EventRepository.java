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

    @Query("SELECT e FROM Event e WHERE upper(e.duration) > current_timestamp AND e.isAccepted=TRUE")
    List<Event> getAllAcceptedActualEvents();

    @Query(value = "SELECT * FROM event e WHERE upper(e.duration) > current_timestamp AND (e.is_accepted=TRUE OR e.user_id=:id)", nativeQuery = true)
    List<Event> getAllActualEventsForUser(int id);

    @Query(value = "SELECT e.id FROM event e WHERE e.room_id=:roomId AND e.is_accepted=TRUE AND e.duration && CAST(:duration AS tsrange)", nativeQuery = true)
    List<Integer> getIntersections(int roomId, String duration);

    @Query(value = "SELECT e FROM Event e WHERE e.id = :id and e.user_id = :userId", nativeQuery = true)
    Optional<Event> get(int id, int userId);

    default Event checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new IllegalRequestDataException("Meal id=" + id + " doesn't belong to User id=" + userId));
    }

}
