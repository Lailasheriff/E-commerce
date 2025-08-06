package com.project.ecommerce.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class GenericSearchUtil {

    public <T> List<T> search(List<T> data, String query, String... fields) {
        String loweredQuery = query.toLowerCase(Locale.ROOT);

        return data.stream()
                .filter(item -> {
                    for (String fieldName : fields) {
                        try {
                            Field field = item.getClass().getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Object value = field.get(item);
                            if (value != null && value.toString().toLowerCase(Locale.ROOT).contains(loweredQuery)) {
                                return true;
                            }
                        } catch (NoSuchFieldException | IllegalAccessException ignored) {
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
}
