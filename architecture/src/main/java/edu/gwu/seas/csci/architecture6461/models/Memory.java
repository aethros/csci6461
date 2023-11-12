package edu.gwu.seas.csci.architecture6461.models;

public final class Memory {
    private static Memory instance;

    private Memory() { }

    public void initialize()
    {
        // .
    }

    public static Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }

        return instance;
    }

}
