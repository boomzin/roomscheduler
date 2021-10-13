package com.example.roomscheduler.model;

import com.example.roomscheduler.util.EventSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vladmihalcea.hibernate.type.range.PostgreSQLRangeType;
import com.vladmihalcea.hibernate.type.range.Range;
import lombok.Getter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Getter
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

    @Column(name = "is_accepted")
    private boolean isAccepted;

    @ManyToOne
    @JsonBackReference(value = "roomEvents")
    private Room room;

    @ManyToOne
    @JsonBackReference(value = "userEvents")
    private User user ;
}
