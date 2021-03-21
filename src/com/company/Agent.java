package com.company;

import java.util.Random;

abstract public class Agent {

    ProfileVector true_vector;
    ProfileVector estimated_vector = null;
    double response_variance;
    Scheme scheme;

    public Agent(ProfileVector true_vector, double response_variance, Scheme scheme) {
        this.true_vector = true_vector;
        this.response_variance = response_variance;
        this.scheme = scheme;
    }

    Event makeProfile(Simulation sim){
        return new ProfileEvent(sim, this);
    }

    void makeProfile(){

        Random r = new Random();

        double[] interests = new double[true_vector.interests.length];
        for(int i = 0; i < interests.length; i++){
            double response = r.nextGaussian()*response_variance + true_vector.interests[i];
            if(response < scheme.interests_min[i]) response = scheme.interests_min[i];
            if(response > scheme.interests_max[i]) response = scheme.interests_max[i];
            interests[i] = response;

        }

        double[] aptitude = new double[true_vector.aptitude.length];
        for(int i = 0; i < aptitude.length; i++){
            double response = r.nextGaussian()*response_variance + true_vector.aptitude[i];
            if(response < scheme.aptitude_min[i]) response = scheme.aptitude_min[i];
            if(response > scheme.aptitude_max[i]) response = scheme.aptitude_max[i];
            aptitude[i] = response;
        }

        estimated_vector = new ProfileVector(interests, aptitude);

    }

    abstract Event getNextStep(Simulation sim);
}
