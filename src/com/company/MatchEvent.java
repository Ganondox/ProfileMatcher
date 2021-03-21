package com.company;

import java.util.List;

public class MatchEvent extends  Event{


    UserAgent user;

    @Override
    void run() {

        List<EmployerAgent> matches = sim.getMatches(user);
        if(!user.apply(matches)){
            sim.loop.queueEvent(new MatchEvent(sim, user));
        }

    }

    public MatchEvent(Simulation sim, UserAgent user) {
        super(sim);
        this.user = user;
    }
}
