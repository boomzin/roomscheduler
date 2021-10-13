package com.example.roomscheduler.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
public class Room extends AbstractBaseEntity {

    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "is_has_projector")
    private boolean isHasProjector;

    @Column(name = "is_has_white_board")
    private boolean isHasWhiteBoard;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    @JsonManagedReference(value = "roomEvents")
    @OrderBy("duration ASC")
    private List<Event> events;
}
