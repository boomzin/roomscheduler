package com.example.roomscheduler.web.event;

import com.example.roomscheduler.error.IllegalRequestDataException;
import com.example.roomscheduler.model.Event;
import com.example.roomscheduler.model.Room;
import com.example.roomscheduler.model.Status;
import com.example.roomscheduler.repository.EventRepository;
import com.example.roomscheduler.repository.RoomRepository;
import com.example.roomscheduler.to.EventTo;
import com.example.roomscheduler.web.AuthUser;
import com.vladmihalcea.hibernate.type.range.Range;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = EventController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class EventController {
    static final String REST_URL = "/api/events";

    private EventRepository eventRepository;
    private RoomRepository roomRepository;

    @GetMapping
    public List<Event> getAll() {
        log.info("get all events");
        return eventRepository.findAll(Sort.by(Sort.Direction.ASC, "duration"));
    }

    @GetMapping("actual")
    public List<Event> getAllActual() {
        log.info("get all actual events");
        return eventRepository.getAllActualEvents();
    }

    @GetMapping("confirmed-actual")
    public List<Event> getAllConfirmedActual() {
        log.info("get all confirmed actual events");
        return eventRepository.getAllConfirmedActualEvents();
    }

    @GetMapping("actual-with-own")
    public List<Event> getAllActualForUser(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get all confirmed actual events with actual events for user id");
        return eventRepository.getAllActualEventsForUser(authUser.id());
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> createWithLocation(@Valid @RequestBody EventTo eventTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user {} books event for room {}", authUser.getUser().getId(), eventTo.getRoomId());
        Room room = checkAndGetRoom(eventTo);
        Range<LocalDateTime> duration = checkTimeAndGetRange(eventTo);
        Event newEvent = new Event();
        newEvent.setDescription(eventTo.getDescription());
        newEvent.setDuration(duration);
        newEvent.setUser(authUser.getUser());
        newEvent.setRoom(room);
        newEvent.setStatus(Status.STATELESS);
        Event created = eventRepository.save(newEvent);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody EventTo eventTo, @AuthenticationPrincipal AuthUser authUser, @PathVariable("id") int eventId) {
        log.info("user {} update event {}", authUser.getUser().getId(), eventId);
        Event updatedEvent = eventRepository.checkBelong(eventId, authUser.id());
        if (Status.CONFIRMED.equals(updatedEvent.getStatus())) {
            throw new IllegalRequestDataException("Confirmed events are unavailable to update." +
                    " To change a confirmed event, delete it " + eventId + " and create new one");
        }
        Room room = checkAndGetRoom(eventTo);
        Range<LocalDateTime> duration = checkTimeAndGetRange(eventTo);
        updatedEvent.setDescription(eventTo.getDescription());
        updatedEvent.setDuration(duration);
        updatedEvent.setRoom(room);
        eventRepository.save(updatedEvent);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable("id") int eventId) {
        log.info("delete event {} for user {}", eventId, authUser.id());
        Event removedEvent = eventRepository.checkBelong(eventId, authUser.id());
        if (LocalDateTime.now().isAfter(removedEvent.getDuration().upper()) && Status.CONFIRMED.equals(removedEvent.getStatus())) {
            throw new IllegalRequestDataException("You cannot delete a confirmed event that has already ended");
        }
        eventRepository.delete(removedEvent);
    }

    private Room checkAndGetRoom(@RequestBody EventTo eventTo) {
        return roomRepository.findById(eventTo.getRoomId()).orElseThrow(
                () -> new IllegalRequestDataException("Room with id=" + eventTo.getRoomId() + " does not exist"));
    }

    private Range<LocalDateTime> checkTimeAndGetRange(@RequestBody EventTo eventTo) {
        if (LocalDateTime.now().isAfter(eventTo.getLower()) || eventTo.getLower().isAfter(eventTo.getUpper()) || eventTo.getLower().equals(eventTo.getUpper())) {
            throw new IllegalRequestDataException("Invalid  time bounds");
        }
        Range<LocalDateTime> duration = Range.closedOpen(eventTo.getLower(), eventTo.getUpper());
        List<Integer> intersections = eventRepository.getConfirmedIntersections(eventTo.getRoomId(), duration.asString());
        if (intersections.size() > 0) {
            throw new IllegalRequestDataException("The timing of the event has intersection with events, which already have been confirmed "
                    + intersections.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ")) +
                    " in this room: " + eventTo.getRoomId());
        }
        return duration;
    }
}
