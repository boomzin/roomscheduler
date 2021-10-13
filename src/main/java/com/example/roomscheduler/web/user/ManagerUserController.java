package com.example.roomscheduler.web.user;

import com.example.roomscheduler.model.Role;
import com.example.roomscheduler.model.User;
import com.example.roomscheduler.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ManagerUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ManagerUserController {
    static final String REST_URL = "/api/manager/users";

    private UserRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable int id) {
        log.info("get user {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping
    public List<User> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @GetMapping("/{id}/with-events")
    public ResponseEntity<User> getWithVotes(@PathVariable int id) {
        log.info("get user with events {}", id);
        return ResponseEntity.of(repository.getWithEvents(id));
    }
//todo: filter user role
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addOrRemoveRole(@PathVariable int id, @RequestParam String role) {
        User user = repository.getById(id);
        Role switchedRole = Role.valueOf(role.toUpperCase());
        log.info(user.getRoles().contains(switchedRole) ? "delete role {} for {}" : "add role {} for {}", switchedRole, id);
        user.addOrRemoveRole(switchedRole);
    }
}
