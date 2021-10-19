package com.example.roomscheduler.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
public class Room extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 2, max = 60)
    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "capacity", nullable = false)
    @Range(min = 2, max = 100)
    private int capacity;

    @Column(name = "has_projector")
    private boolean hasProjector;

    @Column(name = "has_white_board")
    private boolean hasWhiteBoard;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    @JsonManagedReference(value = "roomEvents")
    @OrderBy("duration ASC")
    private List<Event> events;
}
