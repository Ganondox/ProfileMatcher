package com.company;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EmployerAgent extends Agent {

    public static final double ADJUSTMENT_WEIGHT = 0.5;
    public static final int WAIT_TIME = 10;
    public static final double THRESHHOLD_SHIFT = 0;


    List<UserAgent> applicationQue = new LinkedList<>();
    Set<UserAgent> rejected = new HashSet<>();
    UserAgent candidate = null;
    double bar;
    double[] thresholds;
    int time = WAIT_TIME;



    public EmployerAgent(ProfileVector true_vector, double response_variance, Scheme scheme) {
        super(true_vector, response_variance, scheme);
        thresholds = new double[true_vector.aptitude.length];
        for(int i = 0; i < thresholds.length; i++){
            thresholds[i] = true_vector.aptitude[i] - THRESHHOLD_SHIFT;
        }
    }

    @Override
    Event getNextStep(Simulation sim) {
        return new WaitEvent(sim, this);
    }

    public void review(Simulation sim){
        UserAgent applicant = applicationQue.get(0);
        applicationQue.remove(0);

        if(candidate == null){
            test(applicant, sim);

        } else {

            double dist = applicant.true_vector.aptDivergence(true_vector);
            if(dist < bar){
                test(applicant, sim);
            } else {
                reject(applicant, sim);
            }

        }
    }

    private void test(UserAgent applicant, Simulation sim){
        boolean passed = true;

        for(int i = 0; i < thresholds.length; i++){
            if(applicant.true_vector.aptitude[i] < thresholds[i]){
                passed = false;
                thresholds[i] = thresholds[i] * (1 - ADJUSTMENT_WEIGHT) + applicant.true_vector.aptitude[i] * ADJUSTMENT_WEIGHT;
            }
        }

        if(passed) {
            if(candidate != null){
                reject(candidate, sim);
            }

            candidate = applicant;
            bar = applicant.true_vector.aptDivergence(true_vector);
        } else {
            reject(applicant, sim);
        }

    }

    public void reject(UserAgent user, Simulation sim){
        rejected.add(user);
        sim.loop.queueEvent(new MatchEvent(sim,user));

    }

}
