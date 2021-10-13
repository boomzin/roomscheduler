package com.example.roomscheduler.web.room;

import com.example.roomscheduler.model.Room;
import com.example.roomscheduler.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = ManagerRoomController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ManagerRoomController {
    static final String REST_URL = "/api/manager/rooms";

    private RoomRepository roomRepository;

    @GetMapping
    public List<Room> getAll() {
        log.info("get all rooms");
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "description"));
    }

    @GetMapping("with-events")
    public List<Room> getAllWithEvents() {
        log.info("get all rooms with events");
        return roomRepository.getAllWithEvents();
    }

    @GetMapping("with-actual-events")
    public List<Room> getAllWithEventsUpFromNow() {
        log.info("get all rooms with events");
        return roomRepository.getAllWithEventsUpFromNow();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> get(@PathVariable int id) {
        log.info("get room id {}", id);
        return ResponseEntity.of(roomRepository.findById(id));
    }

    @GetMapping("/{id}/with-events")
    public ResponseEntity<Room> getWithEvents(@PathVariable int id) {
        log.info("get room id {} with events", id);
        return ResponseEntity.of(roomRepository.getWithEvents(id));
    }

    @GetMapping("/{id}/with-actual-events")
    public ResponseEntity<Room> getWithEventsUpFromNow(@PathVariable int id) {
        log.info("get room id {} with events", id);
        return ResponseEntity.of(roomRepository.getWithEventsUpFromNow(id));
    }

    //todo: add check is new
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> createWithLocation(@RequestBody Room room) {
        log.info("create room id {}, description {}", room.getId(), room.getDescription());
        Room created = roomRepository.save(room);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

//    todo: does`t work, fix
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Room room, @PathVariable int id) {
        log.info("update room with id={}", id);
        roomRepository.save(room);
    }

}
