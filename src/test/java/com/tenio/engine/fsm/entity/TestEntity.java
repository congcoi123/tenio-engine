package com.tenio.engine.fsm.entity;

public class TestEntity extends AbstractEntity {
    private String id;
    private boolean handleMessageCalled;
    private boolean updateCalled;
    private Telegram lastMessage;
    private float lastDeltaTime;

    public TestEntity(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        handleMessageCalled = true;
        lastMessage = msg;
        return true;
    }

    @Override
    public void update(float deltaTime) {
        updateCalled = true;
        lastDeltaTime = deltaTime;
    }

    public boolean wasHandleMessageCalled() {
        return handleMessageCalled;
    }

    public boolean wasUpdateCalled() {
        return updateCalled;
    }

    public Telegram getLastMessage() {
        return lastMessage;
    }

    public float getLastDeltaTime() {
        return lastDeltaTime;
    }

    public void reset() {
        handleMessageCalled = false;
        updateCalled = false;
        lastMessage = null;
        lastDeltaTime = 0;
    }
} 