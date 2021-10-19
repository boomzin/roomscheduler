package com.example.roomscheduler.model;

import com.example.roomscheduler.util.EventSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vladmihalcea.hibernate.type.range.PostgreSQLRangeType;
import com.vladmihalcea.hibernate.type.range.Range;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Getter
@Setter
//https://vladmihalcea.com/map-postgresql-range-column-type-jpa-hibernate/
@TypeDef(
        typeClass = PostgreSQLRangeType.class,
        defaultForType = Range.class
)
@JsonSerialize(using = EventSerializer.class)
public class Event extends AbstractBaseEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "duration", columnDefinition = "daterange")
    private Range<LocalDateTime> duration;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "roomEvents")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "userEvents")
    private User user;
}
