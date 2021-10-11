package com.example.roomscheduler.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room")
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
    @OrderBy("start ASC")
    private List<Event> events;
}
