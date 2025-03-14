package com.group3.sp25hsf302group3se1889vj.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CollectionUtils {

    public Map<String, String> createPairs(List<String> fields, List<String> fieldTitles) {
        Map<String, String> pairs = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            pairs.put(fields.get(i), fieldTitles.get(i));
        }
        return pairs;
    }
}
