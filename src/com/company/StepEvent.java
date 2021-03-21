package com.company;

public class StepEvent extends Event {

    public StepEvent(Simulation sim) {
        super(sim);
    }

    @Override
    void run() {
        sim.doStep();
    }
}
