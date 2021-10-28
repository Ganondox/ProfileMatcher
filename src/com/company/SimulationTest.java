package com.company;

public class SimulationTest {

    public static void main(String[] args){
        int minHirings = Integer.MAX_VALUE;
        int maxHirings = 0;
        int totalHirings = 0;
        int num_Simulations = 100;

        double mean = 0;
        double sum = 0;

        for(int i = 0; i < num_Simulations; i++){
            Simulation sim = new Simulation(Simulation.Mode.MATCHED);
            int hiring = sim.hirings;
            totalHirings += hiring;
            if( hiring < minHirings){
                minHirings = hiring;
            }
            if(hiring > maxHirings){
                maxHirings = hiring;
            }


            // x := samples[k]
            //    oldM := M
            //    M := M + (x-M)/k
            //    S := S + (x-M)*(x-oldM)

            double oldmean = mean;
            mean+= (hiring - mean)/(i+1);
            sum += (hiring - mean)*(hiring - oldmean);

        }
        double variance = sum/(num_Simulations - 1);

        System.out.println("Matched:" );
        System.out.println("min - " + minHirings);
        System.out.println("average - " + totalHirings*1.0/num_Simulations);
        System.out.println("max - " + maxHirings);
        System.out.println("variance - " + variance);

        totalHirings = 0;
        minHirings = Integer.MAX_VALUE;
        maxHirings = 0;

         mean = 0;
         sum = 0;

        for(int i = 0; i < num_Simulations; i++){
            Simulation sim = new Simulation(Simulation.Mode.INTEREST);
            int hiring = sim.hirings;
            totalHirings += hiring;
            if( hiring < minHirings){
                minHirings = hiring;
            }
            if(hiring > maxHirings){
                maxHirings = hiring;
            }
            double oldmean = mean;
            mean+= (hiring - mean)/(i+1);
            sum += (hiring - mean)*(hiring - oldmean);

        }
        variance = sum/(num_Simulations - 1);

        System.out.println("Interest:" );
        System.out.println("min - " + minHirings);
        System.out.println("average - " + totalHirings*1.0/num_Simulations);
        System.out.println("max - " + maxHirings);
        System.out.println("variance - " + variance);

        totalHirings = 0;
        minHirings = Integer.MAX_VALUE;
        maxHirings = 0;

        mean = 0;
        sum = 0;

        for(int i = 0; i < num_Simulations; i++){
            Simulation sim = new Simulation(Simulation.Mode.APTITUDE);
            int hiring = sim.hirings;
            totalHirings += hiring;
            if( hiring < minHirings){
                minHirings = hiring;
            }
            if(hiring > maxHirings){
                maxHirings = hiring;
            }
            double oldmean = mean;
            mean+= (hiring - mean)/(i+1);
            sum += (hiring - mean)*(hiring - oldmean);

        }
         variance = sum/(num_Simulations - 1);

        System.out.println("Aptitude:" );
        System.out.println("min - " + minHirings);
        System.out.println("average - " + totalHirings*1.0/num_Simulations);
        System.out.println("max - " + maxHirings);
        System.out.println("variance - " + variance);

        totalHirings = 0;
        minHirings = Integer.MAX_VALUE;
        maxHirings = 0;

        mean = 0;
        sum = 0;

        for(int i = 0; i < num_Simulations; i++){
            Simulation sim = new Simulation(Simulation.Mode.MIXED);
            int hiring = sim.hirings;
            totalHirings += hiring;
            if( hiring < minHirings){
                minHirings = hiring;
            }
            if(hiring > maxHirings){
                maxHirings = hiring;
            }
            double oldmean = mean;
            mean+= (hiring - mean)/(i+1);
            sum += (hiring - mean)*(hiring - oldmean);

        }
        variance = sum/(num_Simulations - 1);

        System.out.println("Mixed:" );
        System.out.println("min - " + minHirings);
        System.out.println("average - " + totalHirings*1.0/num_Simulations);
        System.out.println("max - " + maxHirings);
        System.out.println("variance - " + variance);


    }

}
