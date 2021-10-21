package com.example.roomscheduler.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vladmihalcea.hibernate.type.range.Range;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;

@JsonComponent
public class Serializers {

    public static class UnwrappingRangeSerializer extends JsonSerializer<Range<LocalDateTime>> {

        @Override
        public boolean isUnwrappingSerializer() {
            return true;
        }

        @Override
        public void serialize(Range<LocalDateTime> range, JsonGenerator json, SerializerProvider provider) throws IOException {
            json.writeStringField("duration.lower", range.lower().toString().replace("T", " "));
            json.writeStringField("duration.upper", range.upper().toString().replace("T", " "));
        }
    }
}
