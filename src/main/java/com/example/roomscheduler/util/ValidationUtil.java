package com.example.roomscheduler.util;

import com.example.roomscheduler.error.IllegalRequestDataException;
import com.example.roomscheduler.model.AbstractBaseEntity;

public class ValidationUtil {

    public static void checkNew(AbstractBaseEntity bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

}
