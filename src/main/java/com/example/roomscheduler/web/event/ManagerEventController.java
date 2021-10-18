package com.example.roomscheduler.web.event;

import com.example.roomscheduler.error.IllegalRequestDataException;
import com.example.roomscheduler.model.Event;
import com.example.roomscheduler.model.Status;
import com.example.roomscheduler.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = ManagerEventController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ManagerEventController {
    static final String REST_URL = "/api/manager/events";

    private EventRepository eventRepository;

    @GetMapping("/actual-stateless")
    public List<Event> getAllActualStateless() {
        log.info("get all actual stateless events");
        return eventRepository.getAllActualStatelessEvents();
    }

    @Transactional
    @PatchMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable int id) {
        log.info("reject event {}", id);
        Event rejectedEvent = checkStatelessAndGet(id);
        rejectedEvent.setStatus(Status.REJECTED);
        eventRepository.save(rejectedEvent);
    }

    @Transactional
    @PatchMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@PathVariable("id") int eventId) {
        log.info("confirm event {}", eventId);
        Event confirmedEvent = checkStatelessAndGet(eventId);
        if (LocalDateTime.now().isAfter(confirmedEvent.getDuration().lower())) {
            throw new IllegalRequestDataException("You cannot confirm event " + eventId + " that has already started");
        }
        int roomId = eventRepository.getRoomId(eventId);
        List<Integer> confirmedIntersections = eventRepository.getConfirmedIntersections(roomId, confirmedEvent.getDuration().asString());
        if (confirmedIntersections.size() > 0) {
            throw new IllegalRequestDataException("The timing of the event has intersection with events, which already have been confirmed "
                    + confirmedIntersections.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ")) +
                    " in this room: " + roomId);
        }
        List<Event> statelessIntersections = eventRepository.getActualStatelessIntersections(roomId, confirmedEvent.getDuration().asString());
        if (statelessIntersections.size() > 0) {
            statelessIntersections.forEach(event -> {
                event.setStatus(Status.REJECTED);
                eventRepository.save(event);
            });
        }
        confirmedEvent.setStatus(Status.CONFIRMED);
        eventRepository.save(confirmedEvent);
    }

    private Event checkStatelessAndGet(int id){
        return eventRepository.getStatelessById(id).orElseThrow(
                () -> new IllegalRequestDataException("You can handle only stateless events. Event " + id + " has another status"));
    }
}
