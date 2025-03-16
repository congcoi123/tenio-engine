package com.tenio.engine.message;

import java.util.HashMap;
import java.util.Map;

public class TestExtraMessage implements ExtraMessage {
    private final long timestamp;
    private final Map<String, Object> content;

    public TestExtraMessage() {
        this.timestamp = System.currentTimeMillis();
        this.content = new HashMap<>();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void putContent(String key, Object value) {
        content.put(key, value);
    }

    @Override
    public Map<String, Object> getContent() {
        return new HashMap<>(content);
    }

    @Override
    public Object getContentByKey(String key) {
        return content.get(key);
    }
} 