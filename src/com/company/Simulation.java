package com.company;

import java.util.*;

public class Simulation {


    enum Mode{APTITUDE, INTEREST, MIXED, MATCHED};

    //parameters
    int FINAL_STEP = 100;
    Scheme SCHEME = new Scheme();
    int NUM_USER_DISTRIBUTIONS = 3;
    int NUM_EMPLOYER_DISTRIBUTIONS = 3;
    double STOPPING_PROBABILITY = 0.3;
    double RESPONSE_VARIANCE = 1;
    int APT_N = 100;
    int INT_M = 100;
    int NUM = 3;


    EventLoop loop;
    AgentDistribution[] agentDistributions;
    int step = 0;
    List<Agent> activeUsers = new LinkedList<>();
    int hirings = 0;
    Mode mode;
    int employerCount = 0;
    int userCount = 0;

    public Simulation(Mode M) {

        mode = M;

        createDistributions();

        //create mock users
        for(int i = 0; i < APT_N + INT_M - 1; i++){
           int index = (int)(Math.random() * NUM_USER_DISTRIBUTIONS);
            Agent mock = agentDistributions[index].generateAgent();
            mock.makeProfile();
           activeUsers.add(mock);
        }

        loop = new EventLoop();
        loop.queueEvent(new StepEvent(this));
        loop.run();

        //System.out.println(activeUsers.size());
        //System.out.println("Hirings: " + hirings);
        //System.out.println("Remaining agents: " + activeUsers.size());
        //System.out.println("Users: " +  userCount);
        //System.out.println("Employers: " +  employerCount);
    }

    public void doStep(){
        //System.out.println(step);
        step++;
        if(step >= FINAL_STEP){
            loop.active = false;
        }

        //geometrically generate users
        boolean generate = Math.random() > STOPPING_PROBABILITY;
        while(generate){
            int index = 0;
            if(Math.random() > 0.5){
                employerCount++;
                index += NUM_USER_DISTRIBUTIONS;
                index += (int)(Math.random() *  NUM_EMPLOYER_DISTRIBUTIONS);
            } else {
                index += (int)(Math.random() *  NUM_USER_DISTRIBUTIONS);
                userCount++;

            }
            loop.queueEvent(agentDistributions[index].generateAgent().makeProfile(this));
            generate = Math.random() > STOPPING_PROBABILITY;
        }

        loop.queueEvent(new StepEvent(this));


    }

    public void createDistributions(){

        agentDistributions = new AgentDistribution[NUM_USER_DISTRIBUTIONS + NUM_EMPLOYER_DISTRIBUTIONS];

        for(int i = 0; i < NUM_USER_DISTRIBUTIONS; i++){
            agentDistributions[i] = new  AgentDistribution(false, SCHEME, RESPONSE_VARIANCE);
        }
        for(int i = 0; i < NUM_EMPLOYER_DISTRIBUTIONS; i++){
            agentDistributions[i + NUM_USER_DISTRIBUTIONS] = new  AgentDistribution(true, SCHEME, RESPONSE_VARIANCE);
        }

    }

    public List<EmployerAgent> getMatches(UserAgent targetUser){
        //Build the working set
        List<ProfileVector> users = new LinkedList<>();
        List<ProfileVector> employers = new LinkedList<>();
        Map<Integer, Agent> idToUsername = new HashMap<>();
        ProfileVector target = targetUser.estimated_vector;
        for (Agent a : activeUsers) {
            if(target.getId() != a.estimated_vector.getId()) {
                idToUsername.put(a.estimated_vector.getId(), a);
                if (a instanceof EmployerAgent && !targetUser.rejected.contains((EmployerAgent) a)) {
                    employers.add(a.estimated_vector);
                } else if (a instanceof UserAgent) {
                    users.add(a.estimated_vector);
                }
            }
        }
        //find matches
        //System.out.println("Finding matches");
        int[] matches = null;
        switch (mode){
            case MATCHED:
                matches = MatchFinder.getRanking(targetUser.estimated_vector, employers, users, APT_N, INT_M, NUM);
                break;
            case INTEREST:
                matches = MatchFinder.getInterestRanking(targetUser.estimated_vector, employers, NUM);
                break;
            case APTITUDE:
                matches = MatchFinder.getAptitudeRanking(targetUser.estimated_vector, employers, NUM);
                break;
            case MIXED:
                matches = MatchFinder.getMixedRanking(targetUser.estimated_vector, employers, NUM);
                break;

        }

        List<EmployerAgent> canidates = new LinkedList<>();
        for(int i = 0; i < matches.length; i++){

            EmployerAgent match = (EmployerAgent) idToUsername.get(matches[i]);
           canidates.add(match);
        }
        return canidates;
    }

}
