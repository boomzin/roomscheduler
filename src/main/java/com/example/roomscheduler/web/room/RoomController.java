package com.example.roomscheduler.web.room;

import com.example.roomscheduler.model.Room;
import com.example.roomscheduler.repository.RoomRepository;
import com.example.roomscheduler.repository.specification.RoomEquipment;
import com.example.roomscheduler.repository.specification.SearchCriteria;
import com.vladmihalcea.hibernate.type.range.Range;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/free-in-range")
    public List<Room> getAllFreeInRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("get all rooms free in time range");
        Range<LocalDateTime> duration = Range.closedOpen(start, end);
        return roomRepository.getFreeInRange(duration.asString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> get(@PathVariable int id) {
        log.info("get room id {}", id);
        return ResponseEntity.of(roomRepository.findById(id));
    }

    @GetMapping("/filtered-by-equipment")
    public List<Room> getFilteredByEquipment(@RequestParam(required = false) Optional<Integer> capacity,
                                             @RequestParam(required = false) Optional<Boolean> hasProjector,
                                             @RequestParam(required = false) Optional<Boolean> hasWhiteBoard) {
        log.info("get rooms filtered by equipment");
        RoomEquipment roomEquipment = new RoomEquipment();
        capacity.ifPresent(integer -> roomEquipment.add(new SearchCriteria("capacity", integer)));
        hasProjector.ifPresent(aBoolean -> roomEquipment.add(new SearchCriteria("hasProjector", aBoolean)));
        hasWhiteBoard.ifPresent(aBoolean -> roomEquipment.add(new SearchCriteria("hasWhiteBoard", aBoolean)));
        return roomRepository.findAll(roomEquipment);
    }
}
