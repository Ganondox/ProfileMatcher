package com.company;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserAgent extends Agent {

    Set<EmployerAgent> rejected = new HashSet<>();

    public UserAgent(ProfileVector true_vector, double response_variance, Scheme scheme) {
        super(true_vector, response_variance, scheme);
    }

    @Override
    Event getNextStep(Simulation sim) {
        return new MatchEvent(sim, this);
    }

    boolean apply(List<EmployerAgent> employers){

        EmployerAgent best = null;
        double bestScore = Double.MAX_VALUE;

        for(EmployerAgent e: employers){
            if(!e.rejected.contains(this)) {
                double score = true_vector.intDivergence(e.true_vector);
                if (score < bestScore) {
                    bestScore = score;
                    best = e;
                }
            }
        }
        if(best != null) {
            best.applicationQue.add(this);
            return true;
        } else {
            //update profile to reject matches
            rejected.addAll(employers);
            for(int j = 0; j < estimated_vector.interests.length; j++){
                for(EmployerAgent e: employers) {
                    estimated_vector.interests[j] -= (e.estimated_vector.interests[j]  - estimated_vector.interests[j]) * MatchFinder.K;
                }
            }
            //look for new canidates
            return false;

        }

    }
}
