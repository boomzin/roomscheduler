package com.example.roomscheduler.util;

import com.example.roomscheduler.model.Event;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class EventSerializer extends StdSerializer<Event> {

    public EventSerializer() {
        this(null);
    }

    public EventSerializer(Class<Event> t) {
        super(t);
    }

    @Override
    public void serialize(
            Event value, JsonGenerator jsonGenerator, SerializerProvider provider)
            throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", value.getId());
        jsonGenerator.writeNumberField("room.id", value.getRoom().getId());
        jsonGenerator.writeNumberField("user.id", value.getUser().getId());
        jsonGenerator.writeStringField("description", value.getDescription());
        jsonGenerator.writeStringField("duration.lower", value.getDuration().lower().toString().replace("T", " "));
        jsonGenerator.writeStringField("duration.upper", value.getDuration().upper().toString().replace("T", " "));
        jsonGenerator.writeStringField("status", value.getStatus().name());
        jsonGenerator.writeEndObject();
    }
}