package com.company;

public class WaitEvent extends Event {

   EmployerAgent employer;

    public WaitEvent(Simulation sim, EmployerAgent employer) {
        super(sim);
        this.employer = employer;
    }

    @Override
    void run() {

        if(employer.time > 0){
            sim.loop.queueEvent(new WaitEvent(sim, employer));
            if(employer.candidate == null) {


            } else {
                employer.time--;

            }
            if (employer.applicationQue.size() > 0) {
                employer.review(sim);
            }
        } else {
            //hired
            sim.activeUsers.remove(employer.candidate);
            sim.activeUsers.remove(employer);
            sim.hirings++;
            //System.out.println("Rejected: " + employer.rejected.size());
            //System.out.println("In cue: " + employer.applicationQue.size());
            //reject everyone in cue
            for(UserAgent user: employer.applicationQue){
                employer.reject(user, sim);
            }

        }

    }
}
