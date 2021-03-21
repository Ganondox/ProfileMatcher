package com.company;

public abstract class Event {

    Simulation sim;

    public Event(Simulation sim) {
        this.sim = sim;
    }

    abstract void run();
}
