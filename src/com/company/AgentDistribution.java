package com.company;

import java.util.List;
import java.util.Random;

public class AgentDistribution {

    double[] means_i;
    double[] variances_i;
    double[] means_a;
    double[] variances_a;

    boolean isEmployer;
    double rv;
    Scheme s;




    public AgentDistribution(boolean isEmployer, Scheme scheme, double response_variance) {
        this.isEmployer = isEmployer;
        rv = response_variance;
        s = scheme;

        means_i = new double[scheme.interests_max.length];
        variances_i  = new double[scheme.interests_max.length];
        for(int i = 0; i < scheme.interests_max.length; i++){
            means_i[i] = (Math.random() * (scheme.interests_max[i] - scheme.interests_min[i])) + scheme.interests_min[i];
            variances_i[i] = Math.random() * 2 * scheme.interests_grain[i];
        }

        means_a = new double[scheme.aptitude_max.length];
        variances_a  = new double[scheme.aptitude_max.length];
        for(int i = 0; i < scheme.aptitude_max.length; i++){
            means_a[i] = (Math.random() * (scheme.aptitude_max[i] - scheme.aptitude_min[i])) + scheme.aptitude_min[i];
            variances_a[i] = Math.random() * 2 * scheme.aptitude_grain[i];
        }
    }

    public Agent generateAgent(){
        Random r = new Random();

        double[] interest = new double[means_i.length];
        for(int i = 0; i < interest.length; i++){

            interest[i] = r.nextGaussian()*variances_i[i] + means_i[i];
        }
        double[] aptitude = new double[variances_a.length];
        for(int i = 0; i < aptitude.length; i++){
            aptitude[i] = r.nextGaussian()*variances_a[i] + means_a[i];
        }
        ProfileVector vector = new ProfileVector(interest, aptitude);
        if(isEmployer) return new EmployerAgent(vector, rv, s); else return new UserAgent(vector, rv, s);

    }

}
