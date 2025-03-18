package com.tenio.engine.message;

import java.util.HashMap;
import java.util.Map;

public class TestExtraMessage implements ExtraMessage {
    private final Map<String, Object> content;

    public TestExtraMessage() {
        this.content = new HashMap<>();
    }

    @Override
    public void setValue(String key, Object value) {
        content.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> clazz) {
        Object value = content.get(key);
        if (value != null && clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return content.containsKey(key);
    }

    @Override
    public void removeValue(String key) {
        content.remove(key);
    }

    @Override
    public void clear() {
        content.clear();
    }
} 