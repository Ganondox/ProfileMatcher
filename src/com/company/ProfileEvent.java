package com.company;

public class ProfileEvent extends Event{

    Agent a;

    public ProfileEvent(Simulation sim, Agent agent) {
        super(sim);
        a = agent;
    }

    @Override
    void run() {

        a.makeProfile();
        sim.activeUsers.add(a);
        sim.loop.queueEvent(a.getNextStep(sim));

    }


}
