package com.company;

import java.util.List;

public class Matcher {

    public static int[] match(List<ProfileVector> users, List<ProfileVector> employers){
        double[][] intDiv = new double[users.size()][employers.size()];
        for(int i = 0; i < users.size(); i++){
            for(int j = 0; j < employers.size(); j++){
                intDiv[i][j] = users.get(i).intDivergence(employers.get(j));
            }
        }
        int[][] intRank = new int[users.size()][employers.size()];
        for(int i = 0; i < users.size(); i++){
            for(int j = 0; j < employers.size(); j++){
                intRank[i][j] = j;
            }
        }
        //sort each row, and apply the same swaps to rank matrix
        for(int i = 0; i < users.size(); i++){
            quickSortHorizonal(intDiv, intRank, i ,0, employers.size() - 1);
        }



        double[][] aptDiv = new double[users.size()][employers.size()];
        for(int i = 0; i < users.size(); i++){
            for(int j = 0; j < employers.size(); j++){
                aptDiv[i][j] = users.get(i).aptDivergence(employers.get(j));
            }
        }
        int[][] aptRank = new int[users.size()][employers.size()];
        for(int i = 0; i < users.size(); i++){
            for(int j = 0; j < employers.size(); j++){
                aptRank[i][j] = i;
            }
        }
        //sort each column, and apply the same swaps to rank matrix
        /*for(int i = 0; i < employers.size(); i++){
            quickSortVertical(intDiv, intRank, i ,0, employers.size() - 1);
        }


         */
        int[] matchIndices = new int[users.size()];
        int[] tenativeMatch = new int[employers.size()];
        for(int i = 0; i < employers.size(); i++){
            tenativeMatch[i] = -1;
        }




        //Gale-Shapely Loop
        boolean unfinished = true;
        while(unfinished){

            for(int i = 0; i < users.size(); i++){
                //proposes to highest ranked that they haven't proposed to yet
                int target = intRank[i][matchIndices[i]];
                if(tenativeMatch[target] == -1 || tenativeMatch[target] == i){
                    tenativeMatch[target] = i;
                } else {
                    //suitors compete, loser must move to next preference
                    if (aptDiv[i][target] < aptDiv[tenativeMatch[target]][target]) {
                        matchIndices[tenativeMatch[target]]++;
                        tenativeMatch[target] = i;

                    } else {
                        matchIndices[i]++;
                    }
                }
            }

            unfinished = false;
            for(int i = 0; i < employers.size(); i++){
                unfinished |= tenativeMatch[i] == -1;
            }

        }

        //convert rank indices to position indices
        int[] matches = new int[users.size()];
        for(int i = 0; i < users.size() ; i++){
            matches[i] = intRank[i][matchIndices[i]];
        }

        return matches;


    }

    private static void quickSortHorizonal(double[][] values , int[][] indices, int row, int start, int end){
        if(start < end - 1) {
            int pivot = start;
            int buffer = end - 1;
            double ref = values[row][pivot];
            while (pivot < buffer) {

                if (values[row][pivot + 1] < ref) {
                    synchronizedSwap(values, indices, row, pivot, row, pivot + 1);
                    pivot++;
                } else {
                    synchronizedSwap(values, indices, row, buffer, row, pivot + 1);
                    buffer--;
                }

            }
            quickSortHorizonal(values, indices, row, start, pivot);
            quickSortHorizonal(values, indices, row, pivot + 1, end);
        }

    }

    private static void quickSortVertical(double[][] values , int[][] indices, int column, int start, int end){
        if(start < end - 1) {
            int pivot = start;
            int buffer = end - 1;
            double ref = values[pivot][column];
            while (pivot < buffer) {

                if (values[pivot + 1][column] < ref) {
                    synchronizedSwap(values, indices, pivot, column, pivot + 1, column);
                    pivot++;
                } else {
                    synchronizedSwap(values, indices, buffer, column, pivot + 1, column);
                    buffer--;
                }

            }
            quickSortVertical(values, indices, column, start, pivot);
            quickSortVertical(values, indices, column, pivot + 1, end);
        }

    }

    private static void synchronizedSwap(double[][] A, int[][] B, int ax, int ay, int bx, int by){
        double temp = A[ax][ay];
        A[ax][ay] = A[bx][by];
        A[bx][by] = temp;
        int temp2 = B[ax][ay];
        B[ax][ay] = B[bx][by];
        B[bx][by] = temp2;
    }


}
