package com.group3.sp25hsf302group3se1889vj.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MetadataExtractor {

    public Map<String, String> getFieldTitles(Class<?> clazz, List<String> fields) {
        return getFieldMetadataValue(clazz, fields, "title");
    }

    public Map<String, String> getFieldClasses(Class<?> clazz, List<String> fields) {
        return getFieldMetadataValue(clazz, fields, "cssClass");
    }

    private Map<String, String> getFieldMetadataValue(Class<?> clazz, List<String> fields, String annotationField) {
        Map<String, String> fieldMetadata = new HashMap<>();
        for (String field : fields) {
            try {
                Field declaredField = getFieldRecursively(clazz, field);
                if (declaredField != null && declaredField.isAnnotationPresent(FieldMetadata.class)) {
                    FieldMetadata metadata = declaredField.getAnnotation(FieldMetadata.class);
                    String value = "title".equals(annotationField) ? metadata.title() : metadata.cssClass();
                    fieldMetadata.put(field, value);
                } else {
                    log.error("Không tìm thấy annotation cho trường: {}", field);
                }
            } catch (NoSuchFieldException e) {
                log.error("Không tìm thấy trường: {}", field);
            }
        }
        return fieldMetadata;
    }

    private Field getFieldRecursively(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // Tìm trong class cha
            }
        }
        throw new NoSuchFieldException("Không tìm thấy trường: " + fieldName);
    }
}
