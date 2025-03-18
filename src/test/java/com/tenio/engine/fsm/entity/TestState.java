package com.tenio.engine.fsm.entity;

public class TestState implements State<TestEntity> {
    private boolean enterCalled;
    private boolean executeCalled;
    private boolean exitCalled;
    private boolean onMessageCalled;
    private TestEntity lastEntity;
    private Telegram lastMessage;

    @Override
    public void enter(TestEntity entity) {
        enterCalled = true;
        lastEntity = entity;
    }

    @Override
    public void execute(TestEntity entity) {
        executeCalled = true;
        lastEntity = entity;
    }

    @Override
    public void exit(TestEntity entity) {
        exitCalled = true;
        lastEntity = entity;
    }

    @Override
    public boolean onMessage(TestEntity entity, Telegram telegram) {
        onMessageCalled = true;
        lastEntity = entity;
        lastMessage = telegram;
        return true;
    }

    public boolean wasEnterCalled() {
        return enterCalled;
    }

    public boolean wasExecuteCalled() {
        return executeCalled;
    }

    public boolean wasExitCalled() {
        return exitCalled;
    }

    public boolean wasOnMessageCalled() {
        return onMessageCalled;
    }

    public TestEntity getLastEntity() {
        return lastEntity;
    }

    public Telegram getLastMessage() {
        return lastMessage;
    }

    public void reset() {
        enterCalled = false;
        executeCalled = false;
        exitCalled = false;
        onMessageCalled = false;
        lastEntity = null;
        lastMessage = null;
    }
} 