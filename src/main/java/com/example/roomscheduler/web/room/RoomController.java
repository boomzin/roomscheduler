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

    @GetMapping("with-confirmed-events")
    public List<Room> getAllWithConfirmedEvents() {
        log.info("get all rooms with confirmed events");
        return roomRepository.getAllWithConfirmedEvents();
    }

    //todo: authorized user should be able to see his own not Confirmed events
    @GetMapping("with-confirmed-actual-events")
    public List<Room> getAllWithConfirmedEventsActualEvents() {
        log.info("get all rooms with confirmed events");
        return roomRepository.getAllWithConfirmedEventsActualEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> get(@PathVariable int id) {
        log.info("get room id {}", id);
        return ResponseEntity.of(roomRepository.findById(id));
    }

    @GetMapping("/{id}/with-confirmed-events")
    public ResponseEntity<Room> getWithConfirmedEvents(@PathVariable int id) {
        log.info("get room id {} with confirmed events", id);
        return ResponseEntity.of(roomRepository.getWithConfirmedEvents(id));
    }

    //todo: authorized user should be able to see his own not Confirmed events
    @GetMapping("/{id}/with-confirmed-actual-events")
    public ResponseEntity<Room> getWithConfirmedEventsActualEvents(@PathVariable int id) {
        log.info("get room id {} with confirmed actual events", id);
        return ResponseEntity.of(roomRepository.getWithConfirmedEventsActualEvents(id));
    }
}
