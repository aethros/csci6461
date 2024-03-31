package edu.gwu.seas.csci.architecture6461.managers;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.val;

public final class SessionManager {
    private static final Logger LOGGER = Logger.getLogger(SessionManager.class.getName());
    private static SessionManager instance;
    private boolean programLoaded = false;

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

    public void start(boolean startFromPanel) {
        if (startFromPanel || programLoaded) {
            this.controlUnit.start();
        } else {
            LOGGER.warning("Start blocked, no program loaded into memory.");
            this.controlUnit.reset();
        }
    }

    public void loadProgram(Map<Integer, Integer> assembled) {
        this.controlUnit.reset();

        try {
            val entries = new ArrayList<>(assembled.entrySet());
            entries.sort(Map.Entry.comparingByKey());
            for (Map.Entry<Integer, Integer> entry : entries) {
                this.controlUnit.getDataInterface().setValue(entry.getKey(), entry.getValue());
            }
            LOGGER.info("Program loaded into memory.");
            programLoaded = true;
        } catch (Exception e) { LOGGER.severe(e.getMessage()); }
    }

    public boolean isProgramLoaded() {
        return programLoaded;
    }
}
