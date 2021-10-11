package com.example.roomscheduler.model;

import com.vladmihalcea.hibernate.type.range.PostgreSQLRangeType;
import com.vladmihalcea.hibernate.type.range.Range;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@TypeDef(
        typeClass = PostgreSQLRangeType.class,
        defaultForType = Range.class
)
public class Event extends AbstractBaseEntity {

    @Column(name = "description")
    private String description;

//    @Column(name = "start_event")
//    private LocalDateTime start;
//
//    @Column(name = "end_event")
//    private LocalDateTime end;

    @Column(name = "duration", columnDefinition = "daterange")

    private Range<LocalDateTime> duration;

    @Column(name = "is_accepted")
    private boolean isAccepted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user ;
}
