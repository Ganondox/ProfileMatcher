package com.company;

import java.util.LinkedList;
import java.util.List;

public class MatchFinder {

    /*static final int EMPLOYERS = 1000;
    static final int USERS = 1000;
    static final int APT_N = 100;
    static final int INT_M = 50;

     */

    static final double K = 0.2;



    public static void main(String[] args) {



            TestCase testCase = TestCaseGenerator.generateTestCase(-1, 3);

            List<ProfileVector> employers = testCase.total_employers;
            List<ProfileVector> users = testCase.other_users;
            ProfileVector user = testCase.target_user;



            int[] matches = getRanking(user, employers, users, testCase.apt_n, testCase.int_m, 10);

            for(int i = 0; i < matches.length; i++){
                System.out.println(matches[i]);
            }




    }

    public static int findMatch(ProfileVector target, List<ProfileVector> employers, List<ProfileVector> users, int apt_n, int int_m){

        if(employers.size() == 1) return employers.get(0).getId();

        //filter profiles
        EmployerFilter ef = new EmployerFilter(apt_n, int_m);
        List<ProfileVector> filterEmployers = ef.filter(employers, target);
        UserFilter uf = new UserFilter(filterEmployers.size() - 1);
        List<ProfileVector> filterUsers = uf.filter(users, target);
        filterUsers.add(0, target);

        //match profiles
        int[] matches = Matcher.match(filterUsers, filterEmployers);

        return filterEmployers.get(matches[0]).getId();




    }

    public static int[] getRanking(ProfileVector target, List<ProfileVector> employers, List<ProfileVector> users, int apt_n, int int_m, int num){

        if( num > employers.size()){
            num = employers.size();
        }

        int[] ranking = new int[num];

        //copy employers and target so they don't mutate
        double[] intCopy = new double[target.interests.length];
        for(int i = 0; i < target.interests.length; i++){
            intCopy[i] = target.interests[i];
        }
        ProfileVector target2 = new ProfileVector(intCopy, target.aptitude);
        List<ProfileVector> employers2 = new LinkedList<>();
        for(ProfileVector pv: employers){
            employers2.add(pv);
        }

        for(int i =0; i < num; i++){
            ranking[i] = findMatch(target2, employers2, users, apt_n, int_m);

            //prepare for next round by updating employers2 and target2
            double[] ref = new double[ProfileVector.I_DIM];
            for(int j = 0; j < employers2.size(); j++){
                if(employers2.get(j).getId() == ranking[i]){
                    ref = employers2.get(j).interests;
                    employers2.remove(j);
                    break;
                }
            }
            for(int j = 0; j < target2.interests.length; j++){
                target2.interests[j] -= ref[j] * K;
            }


        }

        return ranking;
    }
}
