package com.company;

import java.util.LinkedList;
import java.util.List;

public class EmployerFilter {

      int apt_req;
      int int_req;

    public EmployerFilter(int apt_req, int int_req) {
        this.apt_req = apt_req;
        this.int_req = int_req;
    }

    public List<ProfileVector> filter(List<ProfileVector> profiles, ProfileVector locus){
        List<ProfileVector> filtered = new LinkedList<>();
        if(profiles.size() > apt_req + int_req){

            if(apt_req > 0) {
                //find closest aptitude vectors
                ProfileVector[] top_apt = new ProfileVector[apt_req];

                double[] apt_divergences = new double[apt_req];
                for (int i = 0; i < apt_divergences.length; i++) {
                    apt_divergences[i] = Double.MAX_VALUE;
                }
                for (int i = 0; i < profiles.size(); i++) {
                    double divergence = locus.aptDivergence(profiles.get(i));
                    if (divergence < apt_divergences[apt_req - 1]) {
                        //in top, put in place
                        top_apt[apt_req - 1] = profiles.get(i);
                        apt_divergences[apt_req - 1] = divergence;
                        for (int j = apt_req - 1; j > 0; j--) {
                            if (divergence < apt_divergences[j - 1]) {
                                apt_divergences[j] = apt_divergences[j - 1];
                                top_apt[j] = top_apt[j - 1];
                                apt_divergences[j - 1] = divergence;
                                top_apt[j - 1] = profiles.get(i);
                            } else break;
                        }

                    }
                }
                //add vectors
                for (int i = 0; i < apt_req; i++) {
                    filtered.add(top_apt[i]);
                }
            }

            //find closest interest vectors
            if(int_req > 0) {
                ProfileVector[] top_int = new ProfileVector[int_req];

                double[] int_divergences = new double[int_req];
                for (int i = 0; i < int_divergences.length; i++) {
                    int_divergences[i] = Double.MAX_VALUE;
                }
                for (int i = 0; i < profiles.size(); i++) {
                    //skip ones that have already been selected
                    if (!filtered.contains(profiles.get(i))) {
                        double divergence = locus.intDivergence(profiles.get(i));
                        if (divergence < int_divergences[int_req - 1]) {
                            //in top, put in place
                            top_int[int_req - 1] = profiles.get(i);
                            int_divergences[int_req - 1] = divergence;
                            for (int j = int_req - 1; j > 0; j--) {
                                if (divergence < int_divergences[j - 1]) {
                                    int_divergences[j] = int_divergences[j - 1];
                                    top_int[j] = top_int[j - 1];
                                    int_divergences[j - 1] = divergence;
                                    top_int[j - 1] = profiles.get(i);
                                } else break;
                            }

                        }
                    }
                }
                //add vectors
                for (int i = 0; i < int_req; i++) {
                    filtered.add(top_int[i]);
                }
            }



        } else{
            for(int i = 0; i < profiles.size(); i++){
                filtered.add(profiles.get(i));
            }
        }
        return filtered;
    }
}
