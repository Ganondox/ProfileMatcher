package com.company;

import java.security.Provider;
import java.util.LinkedList;
import java.util.List;

public class TestCaseGenerator {

    static final int EMPLOYERS = 1000;
    static final int USERS = 1000;
    static final int APT_N = 100;
    static final int INT_M = 50;

    public static TestCase generateTestCase(int case_n, int parameter){
        List<ProfileVector> employers = new LinkedList<>();
        List<ProfileVector> users = new LinkedList<>();
        double[] interests = new double[ProfileVector.I_DIM];
        double[] aptitudes = new double[ProfileVector.A_DIM];
        ProfileVector user = null;
        switch (case_n){

            case 1:
                //Strict ordinal ranking, no filtering, parameter pullers user out at that position
                //generate  profile vectors
                for(int i = 0; i < APT_N + INT_M; i++){
                    double[] interests2 = new double[ProfileVector.I_DIM];
                    interests2[0] = i;
                    double[] aptitudes2 = new double[ProfileVector.A_DIM];
                    employers.add(new ProfileVector(interests2, aptitudes2));
                }

                for(int i = 0; i < APT_N + INT_M; i++){
                    if(i != parameter) {
                        double[] interests2 = new double[ProfileVector.I_DIM];
                        double[] aptitudes2 = new double[ProfileVector.A_DIM];
                        aptitudes2[0] = -i;
                        users.add(new ProfileVector(interests2, aptitudes2));
                    }
                }

                aptitudes[0] = -parameter;
                user = new ProfileVector(interests, aptitudes);

                //return corresponding test case
                return new TestCase(employers, users, APT_N, INT_M, user);

            case 2:
                //Each user has a different top pick, nothing else matters, no filtering parameter pulls user with that preference
                //generate  profile vectors
                for(int i = 0; i < APT_N + INT_M; i++){
                    double[] interests2 = new double[ProfileVector.I_DIM];
                    interests2[0] = i;
                    double[] aptitudes2 = new double[ProfileVector.A_DIM];
                    aptitudes2[0] = i;
                    employers.add(new ProfileVector(interests2, aptitudes2));
                }

                for(int i = 0; i < APT_N + INT_M; i++){
                    if(i != parameter) {
                        double[] interests2 = new double[ProfileVector.I_DIM];
                        interests2[0] = i;
                        double[] aptitudes2 = new double[ProfileVector.A_DIM];
                        aptitudes2[0] = i;
                        users.add(new ProfileVector(interests2, aptitudes2));
                    }
                }

                interests[0] = parameter;
                aptitudes[0] = parameter;
                user = new ProfileVector(interests, aptitudes);

                //return corresponding test case
                return new TestCase(employers, users, APT_N, INT_M, user);
            case 3:
                //Some arbitrary contrived case, not filtering

                //I'm going to try to simulate Hannah Huang's example from https://medium.com/@yunhanh/gale-shapley-algorithm-and-stable-matching-dbf1bf748541
                //parameter encodes both if A lies and which user to return

                int n = 3;


                ProfileVector A = new ProfileVector(new double[]{0,1,0,0,0},new double[]{2,1,0,0,0});
                ProfileVector B = new ProfileVector(new double[]{1,0,0,0,0},new double[]{1,2,0,0,0});
                ProfileVector C = new ProfileVector(new double[]{0,0,0,0,0},new double[]{1,2,0,0,0});

                if( n > 2){
                    //A lies
                    A = new ProfileVector(new double[]{0,1,0,0,0},new double[]{2,1,3,0,0});

                }

                employers.add(A);
                employers.add(B);
                employers.add(C);

                ProfileVector D = new ProfileVector(new double[]{1,2,0,0,0},new double[]{0,1,0,0,0});
                ProfileVector E = new ProfileVector(new double[]{2,1,0,0,0},new double[]{1,0,0,0,0});
                ProfileVector F = new ProfileVector(new double[]{1,2,0,0,0},new double[]{0,-1,1,0,0});

                switch (parameter % 3){
                    case 0:
                        user = D;
                        users.add(E);
                        users.add(F);
                        break;
                    case 1:
                        users.add(D);
                        user = E;
                        users.add(F);
                        break;
                    case 2:
                        users.add(D);
                        users.add(E);
                        user = F;


                }
                return new TestCase(employers, users, APT_N, INT_M, user);



            case 4:
                //Test filtering somehow 

            default:
                //Random
                //generate random profile vectors
                employers = new LinkedList<>();
                for(int i = 0; i < EMPLOYERS; i++){
                    employers.add(new ProfileVector());
                }

                for(int i = 0; i < USERS; i++){
                    users.add(new ProfileVector());
                }

                //randomly generate user profile
                user = new ProfileVector();

                //return corresponding test case
                return new TestCase(employers, users, APT_N, INT_M, user);
        }
    }

}
