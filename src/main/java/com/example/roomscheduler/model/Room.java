package com.example.roomscheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room extends AbstractBaseEntity {

    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "is_has_projector")
    private boolean isHasProjector;

    @Column(name = "is_has_white_board")
    private boolean isHasWhiteBoard;
}
