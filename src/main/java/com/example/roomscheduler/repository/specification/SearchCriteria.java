package com.example.roomscheduler.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchCriteria {
    private String key;
    private Object value;
}
