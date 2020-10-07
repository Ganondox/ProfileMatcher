package com.company;

public class ProfileVector {

    final static int I_DIM = 5;
    final static int A_DIM = 5;

    double[] interests;
    double[] aptitude;
    private int id;
    static int id_count = 0;

    public ProfileVector() {
        //randomly generates vector by default
        interests = new double[I_DIM];
        for(int i = 0; i < I_DIM; i++){
            interests[i] = Math.random();
        }
        aptitude = new double[A_DIM];
        for(int i = 0; i < A_DIM; i++){
            aptitude[i] = Math.random();
        }
        id = id_count;
        id_count++;

    }

    public ProfileVector(double[] interests, double[] aptitude) {
        this.interests = interests;
        this.aptitude = aptitude;

        id = id_count;
        id_count++;

    }

    public int getId() {
        return id;
    }

    public double aptDivergence(ProfileVector other){
        double sum = 0;
        for(int i = 0; i < A_DIM; i++){
            if(other.aptitude[i] > aptitude[i]){
                sum += Math.pow(other.aptitude[i] - aptitude[i], 2);
            }
        }
        return sum;
    }

    public double intDivergence(ProfileVector other){
        double sum = 0;
        for(int i = 0; i < I_DIM; i++){

            sum += Math.pow(other.interests[i] - interests[i], 2);

        }
        return sum;
    }

}
