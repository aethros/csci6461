package edu.gwu.seas.csci.architecture6461.managers;

import lombok.Getter;

public final class SessionManager {
    private static SessionManager instance;

    @Getter
    private final Assembler assembler = new Assembler();
    @Getter
    private final ControlUnit controlUnit = new ControlUnit();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }

        return instance;
    }
}
