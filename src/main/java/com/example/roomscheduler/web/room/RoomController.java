package com.example.roomscheduler.web.room;

import com.example.roomscheduler.model.Room;
import com.example.roomscheduler.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RoomController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RoomController {
    static final String REST_URL = "/api/rooms";

    private RoomRepository roomRepository;

    @GetMapping
    public List<Room> getAll() {
        log.info("get all rooms");
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "description"));
    }

    @GetMapping("with-accepted-events")
    public List<Room> getAllWithAcceptedEvents() {
        log.info("get all rooms with accepted events");
        return roomRepository.getAllWithAcceptedEvents();
    }

    //todo: authorized user should be able to see his own not accepted events
    @GetMapping("with-accepted-actual-events")
    public List<Room> getAllWithAcceptedEventsActualEvents() {
        log.info("get all rooms with accepted events");
        return roomRepository.getAllWithAcceptedEventsActualEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> get(@PathVariable int id) {
        log.info("get room id {}", id);
        return ResponseEntity.of(roomRepository.findById(id));
    }

    @GetMapping("/{id}/with-accepted-events")
    public ResponseEntity<Room> getWithAcceptedEvents(@PathVariable int id) {
        log.info("get room id {} with accepted events", id);
        return ResponseEntity.of(roomRepository.getWithAcceptedEvents(id));
    }

    //todo: authorized user should be able to see his own not accepted events
    @GetMapping("/{id}/with-accepted-actual-events")
    public ResponseEntity<Room> getWithAcceptedEventsActualEvents(@PathVariable int id) {
        log.info("get room id {} with accepted actual events", id);
        return ResponseEntity.of(roomRepository.getWithAcceptedEventsActualEvents(id));
    }
}
