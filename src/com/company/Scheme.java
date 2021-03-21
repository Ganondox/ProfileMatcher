package com.company;

import java.security.PrivateKey;

public class Scheme {

    double[] interests_min;
    double[] aptitude_min;
    double[] interests_max;
    double[] aptitude_max;
    double[] interests_grain;
    double[] aptitude_grain;
    

    public Scheme() {

        interests_min = new double[ProfileVector.I_DIM];
        interests_max = new double[ProfileVector.I_DIM];
        interests_grain = new double[ProfileVector.I_DIM];
        for(int i = 0; i < ProfileVector.I_DIM; i++){
            interests_min[i] = 0;
            interests_grain[i] = 1;
            interests_max[i] = 7;
        }
        aptitude_min = new double[ProfileVector.A_DIM];
        aptitude_max = new double[ProfileVector.A_DIM];
        aptitude_grain = new double[ProfileVector.A_DIM];
        for(int i = 0; i < ProfileVector.A_DIM; i++){
            aptitude_min[i] = 0;
            aptitude_grain[i] = 1;
            aptitude_max[i] = 7;
        }
    }
}
