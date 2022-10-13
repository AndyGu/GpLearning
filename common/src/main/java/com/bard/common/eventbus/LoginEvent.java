package com.bard.common.eventbus;

public class LoginEvent {
    public String userName;

    public LoginEvent(String userName) {
        this.userName = userName;
    }
}
