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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.example.roomscheduler.util.ValidationUtil.assureIdConsistent;
import static com.example.roomscheduler.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ManagerRoomController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ManagerRoomController {
    static final String REST_URL = "/api/manager/rooms";

    private RoomRepository roomRepository;
    private UniqueDescriptionValidator descriptionValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(descriptionValidator);
    }

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
    public List<Room> getAllWithActualEvents() {
        log.info("get all rooms with events");
        return roomRepository.getAllWithActualEvents();
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
    public ResponseEntity<Room> getWithEventsActualEvents(@PathVariable int id) {
        log.info("get room id {} with events", id);
        return ResponseEntity.of(roomRepository.getWithEventsActualEvents(id));
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> createWithLocation(@Valid @RequestBody Room room) {
        log.info("create room, description {}", room.getDescription());
        checkNew(room);
        Room created = roomRepository.save(room);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Room room, @PathVariable int id) {
        log.info("update room with id={}", id);
        assureIdConsistent(room, id);
        roomRepository.save(room);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete room {}", id);
        roomRepository.deleteExisted(id);
    }
}
