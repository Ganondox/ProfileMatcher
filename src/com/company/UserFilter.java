package com.company;

import java.util.LinkedList;
import java.util.List;

public class UserFilter {

    int request;

    public UserFilter(int request) {
        this.request = request;
    }

    public List<ProfileVector> filter(List<ProfileVector> profiles, ProfileVector locus){
        List<ProfileVector> filtered = new LinkedList<>();
        if(profiles.size() > request){

            //find closest vectors
            ProfileVector[] top = new ProfileVector[request];

            double[] divergences = new double[request];
            for(int i = 0; i < divergences.length; i++){
                divergences[i] = Double.MAX_VALUE;
            }
            for(int i = 0; i < profiles.size(); i++){
                double divergence = locus.intDivergence(profiles.get(i));
                if(divergence < divergences[request - 1]){
                    //in top, put in place
                    top[request - 1] = profiles.get(i);
                    divergences[request - 1] = divergence;
                    for(int j = request - 1; j > 0; j--){
                        if(divergence < divergences[j - 1]){
                            divergences[j] = divergences[j - 1];
                            top[j] = top[j - 1];
                            divergences[j-1] = divergence;
                            top[j-1] = profiles.get(i);
                        } else break;
                    }

                }
            }
            //add vectors
            for(int i = 0; i < request; i++){
                filtered.add(top[i]);
            }




        } else{
            for(int i = 0; i < profiles.size(); i++){
                filtered.add(profiles.get(i));
            }
        }
        return filtered;
    }
}
