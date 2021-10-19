package com.example.roomscheduler.web.room;

import com.example.roomscheduler.model.Room;
import com.example.roomscheduler.repository.RoomRepository;
import lombok.AllArgsConstructor;
import com.example.roomscheduler.error.IllegalRequestDataException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class UniqueDescriptionValidator implements org.springframework.validation.Validator {

    private final RoomRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Room.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Room room = ((Room) target);
        if (StringUtils.hasText(room.getDescription())) {
            repository.getByDescription(room.getDescription())
                    .ifPresent(dbRoom -> {
                        if (request.getMethod().equals("PUT") && request.getRequestURI().endsWith("/" + dbRoom.id()))
                            return;
                        throw new IllegalRequestDataException("The room with name "
                                + dbRoom.getDescription()
                                + " already exist with id "
                                + dbRoom.getId());
                    });
        }
    }
}
