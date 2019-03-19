package cs445.a3;

import java.util.List;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Sudoku {
    
    /*
    returns true if it is a complete, valid solution
    */
    static boolean isFullSolution(int[][] board) {     
        // if there is an empty cell, return false
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++){
                if (board[i][j]==0)
                    return false;
            }
        }

        // check ea. value is not repeated in the row, col, box
        for (int k = 0; k < 9; k++){    
            for (int val = 1; val <=9; val++) {
                if ((rowCheck(board, k, val)!=1) || (colCheck(board, k, val)!=1) ||
                        (boxCheck(board, k, val)!=1)){
                    return false;
                }
            }
        }

        return true;
    }
    
    // there's only one "val" in the ith row
    private static int rowCheck (int[][] board, int i, int val){
        int total = 0;
        for (int j = 0; j < 9; j++) {
            if ((board[i][j]%10 == 0) && board[i][j]!=0) {
                if ((board[i][j])/10 == val)
                    total++;
            } else {
                if (board[i][j] == val)
                    total++;
            }
        }
        return total;
    }
 
    // there's only one "val" in the jth col  
    private static int colCheck (int[][] board, int j, int val){
        int total = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i][j]%10 == 0 && board[i][j]!=0) {
                if ((board[i][j])/10 == val)
                    total++;
            } else {
                if (board[i][j] == val)
                    total++;
            }
        }
        return total;
    }
    
    // there's only one "val" in the box
    private static int boxCheck(int[][] board, int k, int val) {
        int total = 0;
        for (int i = k-k%3; i <= (k-k%3)+2; i++){
            for (int j = (k%3)*3; j <= ((k%3)*3)+2; j++) {
                if ((board[i][j]%10 == 0) && board[i][j]!=0) {
                    if ((board[i][j])/10 == val)
                        total++;
                } else {
                    if (board[i][j] == val)
                        total++;
                }
            }  
        }
        return total;
    }
        
    /*
    returns true if it should be rejected
    */
    static boolean reject(int[][] board) {
        for (int k = 0; k < 9; k++){
            for (int val = 1; val <= 9; val++) {
                if ((rowCheck(board, k, val)>1) || (colCheck(board, k, val)>1) ||
                        (boxCheck(board, k, val)>1)){
                    return true;
                }
            }
        }
        return false;
    }
    
    /*
    returns another partial solution that includes one additional choice added on
    return null if no more choices to add to
    */
    static int[][] extend(int[][] board) {
        // initialize new partial solution
        int[][] temp = new int[9][9];
        for (int i = 0; i < 9; i++) {
            temp[i] = board[i].clone();
        }
        
        // find an empty cell & add "10"
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if ((!(1<=temp[i][j] && temp[i][j]<=9)) && 
                        ((temp[i][j]%10!=0) || (temp[i][j]==0))){
                    temp[i][j] = 10;
                    return temp;
                }
            }
        }       
        // if cannot add i.e., the board is full
        return null;
    }

    
    /*
    returns partial solution in which the most recent choice changed to its next option
    returns null if no more options for the most recent choice
    */
    static int[][] next(int[][] board) {
        // find the most recent change made
        int i = 8, j = 8;
        outerloop:
        while (i >= 0){
            while (j >= 0){
                if (board[i][j]%10==0 && board[i][j]!=0)
                    break outerloop;
                j--;
            }
            i--;
            j = 8;
        }
        // no cell has been extended
        if (i == -1)    return null;
        
        // change to its next option
        if (board[i][j] != 90){
            board[i][j]+=10;
            return board;
        }
        
        // no more options left
        return null;
    }

    
    static void testIsFullSolution() {
        System.err.println("Testing isFullSolution()");
        /* Not a full solution: return false */
        // multiple duplicates
        testIsFullSolutionUnit(new int[][] {{1,2,3,4,5,6,7,8,9},
                                           {1,2,3,4,5,6,7,8,9},
                                           {1,2,3,4,5,6,7,8,9},
                                           {1,2,3,4,5,6,7,8,9},
                                           {1,2,3,4,5,6,7,8,9},
                                           {1,2,3,4,5,6,7,8,9},
                                           {1,2,3,4,5,6,7,8,9},
                                           {1,2,3,4,5,6,7,8,9},
                                           {1,2,3,4,5,6,7,8,9},});
        // empty
        testIsFullSolutionUnit(new int[][] {{0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0},});
        // one blank
        testIsFullSolutionUnit(new int[][] {{9,7,4,2,3,6,1,5,8},
                                            {6,3,8,5,9,1,7,4,2},
                                            {1,2,5,4,8,7,9,3,6},
                                            {3,1,6,7,5,4,2,8,9},
                                            {7,4,2,9,1,8,5,6,3},
                                            {5,8,9,3,6,2,4,1,7},
                                            {8,6,7,1,2,5,0,9,4},
                                            {2,5,3,6,4,9,8,7,1},
                                            {4,9,1,8,7,3,6,2,5}});
        // multiple blank
        testIsFullSolutionUnit(new int[][] {{0,4,0,1,0,0,3,5,0},
                                            {0,0,0,0,0,0,0,0,0},
                                            {0,0,0,2,0,5,0,0,0},
                                            {0,0,0,4,0,8,9,0,0},
                                            {0,6,0,0,0,0,0,1,2},
                                            {0,5,0,3,0,0,0,0,7},
                                            {0,0,4,0,0,0,1,6,0},
                                            {6,0,0,0,0,7,0,0,0},
                                            {0,1,0,0,8,0,0,2,0}});
        // one duplicate
        testIsFullSolutionUnit(new int[][] {{9,7,4,2,3,6,1,5,8},
                                            {6,3,8,5,9,1,7,4,2},
                                            {1,2,5,4,8,7,9,3,6},
                                            {3,1,6,7,5,4,2,8,9},
                                            {7,4,2,9,1,8,5,6,3},
                                            {5,8,9,3,6,2,4,3,7},
                                            {8,6,7,1,2,5,3,9,4},
                                            {2,5,3,6,4,9,8,7,1},
                                            {4,9,1,8,7,3,6,2,5}});       
        /* Full solution: return true */
        // Full solution
        testIsFullSolutionUnit(new int[][] {{9,7,4,2,3,6,1,5,8},
                                            {6,3,8,5,9,1,7,4,2},
                                            {1,2,5,4,8,7,9,3,6},
                                            {3,1,6,7,5,4,2,8,9},
                                            {7,4,2,9,1,8,5,6,3},
                                            {5,8,9,3,6,2,4,1,7},
                                            {8,6,7,1,2,5,3,9,4},
                                            {2,5,3,6,4,9,8,7,1},
                                            {4,9,1,8,7,3,6,2,5}});
        // Full solution (solved)
        testIsFullSolutionUnit(new int[][] {{4,10,7,3,60,9,8,20,50},
                                            {6,30,2,1,5,8,9,40,7},
                                            {90,5,8,7,2,4,30,1,60},
                                            {80,2,5,4,3,70,1,6,9},
                                            {70,9,1,50,8,60,4,3,2},
                                            {3,4,60,9,1,20,7,5,80},
                                            {2,80,9,60,4,3,5,7,1},
                                            {50,70,3,2,9,1,6,8,40},
                                            {1,6,4,8,7,5,2,9,3}});
    }

    static void testReject() {
        System.err.println("\nTesting reject()");
        /* should be rejected: return true */
        // all filled with one duplicate cell[7][7]
        testRejectUnit(new int[][] {{4,1,7,3,6,9,8,2,5},
                                    {6,3,2,1,5,8,9,4,7},
                                    {9,5,8,7,2,4,3,1,6},
                                    {8,2,5,4,3,7,1,6,9},
                                    {7,9,1,5,8,6,4,3,2},
                                    {3,4,6,9,1,2,7,5,8},
                                    {2,8,9,6,4,3,5,7,1},
                                    {5,7,3,2,9,1,6,3,4},
                                    {1,6,4,8,7,5,2,9,3}});
        // all filled with multiple duplicates
        testRejectUnit(new int[][] {{2,4,3,7,1,6,9,5,8},
                                    {9,8,5,2,3,4,7,1,6},
                                    {7,1,9,8,9,5,3,4,2},
                                    {6,3,9,1,2,7,5,8,4},
                                    {4,5,7,9,6,8,1,3,3},
                                    {8,3,1,5,4,3,6,7,9},
                                    {1,6,2,4,7,9,8,3,5},
                                    {3,7,8,6,5,2,4,9,1},
                                    {5,9,4,3,2,1,2,6,7}});
        // duplicate in the middle box
        testRejectUnit(new int[][] {{0,0,9,0,7,0,0,0,5},
                                    {0,0,2,1,0,0,9,0,0},
                                    {1,0,0,0,2,8,0,0,0},
                                    {0,7,0,0,0,5,0,0,1},
                                    {0,0,8,5,1,0,0,0,0},
                                    {0,5,0,0,0,0,3,0,0},
                                    {0,0,0,0,0,3,0,0,6},
                                    {8,0,0,0,0,0,0,0,0},
                                    {2,1,0,0,0,0,0,8,7}});
        // duplicate in row[4]
        testRejectUnit(new int[][] {{0,4,0,1,0,0,3,5,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,2,0,5,0,0,0},
                                    {0,0,0,4,0,8,9,0,0},
                                    {2,6,0,0,0,0,0,1,2},
                                    {0,5,0,3,0,0,0,0,7},
                                    {0,0,4,0,0,0,1,6,0},
                                    {6,0,0,0,0,7,0,0,0},
                                    {0,1,0,0,8,0,0,2,0}});
        // duplicate in column[4]
        testRejectUnit(new int[][] {{6,0,1,5,9,0,0,0,0},
                                    {0,9,0,0,1,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,4},
                                    {0,7,0,3,1,4,0,0,6},
                                    {0,2,4,0,0,0,0,0,5},
                                    {0,0,3,0,0,0,0,1,0},
                                    {0,0,6,0,0,0,0,0,3},
                                    {0,0,0,9,0,2,0,4,0},
                                    {0,0,0,0,0,1,6,0,0}});   
        
        /* should not be rejected : return false */
        // full solution I
        testRejectUnit(new int[][] {{9,7,4,2,3,6,1,5,8},
                                    {6,3,8,5,9,1,7,4,2},
                                    {1,2,5,4,8,7,9,3,6},
                                    {3,1,6,7,5,4,2,8,9},
                                    {7,4,2,9,1,8,5,6,3},
                                    {5,8,9,3,6,2,4,1,7},
                                    {8,6,7,1,2,5,3,9,4},
                                    {2,5,3,6,4,9,8,7,1},
                                    {4,9,1,8,7,3,6,2,5}});
        // full solution (solved)
        testRejectUnit(new int[][] {{2,4,3,70,1,6,90,50,8},
                                    {9,80,5,20,3,4,7,1,6},
                                    {7,10,6,8,9,50,3,4,2},
                                    {6,30,9,1,2,70,5,8,4},
                                    {4,5,70,9,60,8,1,2,3},
                                    {8,2,1,50,4,3,6,70,9},
                                    {1,60,2,4,7,9,8,3,5},
                                    {30,70,8,6,5,20,40,9,1},
                                    {50,9,40,3,8,10,2,6,7}}); 
        // not a full solution, but still solvable
        testRejectUnit(new int[][] {{3,0,0,0,7,0,0,0,0},
                                    {7,4,6,0,8,1,0,0,2},
                                    {0,8,2,0,0,3,0,0,7},
                                    {0,2,0,3,1,0,0,9,0},
                                    {0,0,0,6,0,7,0,0,0},
                                    {0,1,0,0,2,5,0,7,0},
                                    {5,0,0,2,0,0,9,1,0},
                                    {1,0,0,7,5,0,2,6,3},
                                    {0,0,0,0,4,0,0,0,8}});
        // in the middle of solving
        testRejectUnit(new int[][] {{2,4,3,70,1,6,90,50,8},
                                    {9,80,5,20,3,4,7,1,6},
                                    {7,10,6,8,9,50,3,4,2},
                                    {6,30,9,1,2,70,5,8,4},
                                    {4,5,70,9,0,8,1,2,3},
                                    {8,2,1,0,4,3,6,0,9},
                                    {1,0,2,4,7,9,8,3,5},
                                    {0,0,8,6,5,0,0,9,1},
                                    {0,9,0,3,8,0,2,6,7}});         
        // empty
        testRejectUnit(new int[][] {{0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0}});
        // single given
        testRejectUnit(new int[][] {{0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,1,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0}});        
    }
    
    static void testExtend() {
        System.err.println("\nTesting extend()");
        /* Can be extended: returns board */
        // empty: cell[0][0] should be "10"
        testExtendUnit(new int[][] {{0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0,0}}); 
        // single empty cell at the end: cell[8][8] should be "10"
        testExtendUnit(new int[][] {{9,7,4,2,3,6,1,5,8},
                                    {6,3,8,5,9,1,7,4,2},
                                    {1,2,5,4,8,7,9,3,6},
                                    {3,1,6,7,5,4,2,8,9},
                                    {7,4,2,9,1,8,5,6,3},
                                    {5,8,9,3,6,2,4,1,7},
                                    {8,6,7,1,2,5,3,9,4},
                                    {2,5,3,6,4,9,8,7,1},
                                    {4,9,1,8,7,3,6,2,0}});
        // single empty cell in the middle: cell[4][4] should be "10"
        testExtendUnit(new int[][] {{2,4,3,7,1,6,9,5,8},
                                    {9,8,5,2,3,4,7,1,6},
                                    {7,1,6,8,9,5,3,4,2},
                                    {6,3,9,1,2,7,5,8,4},
                                    {4,5,7,9,0,8,1,2,3},
                                    {8,2,1,5,4,3,6,7,9},
                                    {1,6,2,4,7,9,8,3,5},
                                    {3,7,8,6,5,2,4,9,1},
                                    {5,9,4,3,8,1,2,6,7}});
        // multiple empty cells: cell[1][2] should be "10"
        testExtendUnit(new int[][] {{4,1,7,3,6,9,8,2,5},
                                    {6,3,0,1,5,8,9,4,7},
                                    {9,5,8,7,2,4,0,1,6},
                                    {8,2,5,4,3,7,1,6,0},
                                    {7,9,1,5,0,6,4,3,2},
                                    {3,0,6,9,1,2,7,5,0},
                                    {2,8,0,6,4,3,5,7,1},
                                    {5,7,3,2,9,0,6,8,4},
                                    {0,6,4,8,7,5,2,9,3}});
                     
        /* Cannot be extended: returns null */
        // no empty cell
        testExtendUnit(new int[][] {{9,7,4,2,3,6,1,5,8},
                                    {6,3,8,5,9,1,7,4,2},
                                    {1,2,5,4,8,7,9,3,6},
                                    {3,1,6,7,5,4,2,8,9},
                                    {7,4,2,9,1,8,5,6,3},
                                    {5,8,9,3,6,2,4,1,7},
                                    {8,6,7,1,2,5,3,9,4},
                                    {2,5,3,6,4,9,8,7,1},
                                    {4,9,1,8,7,3,6,2,5}});  
        // no empty cell (solved)
        testExtendUnit(new int[][] {{40,1,7,3,6,9,80,2,5},
                                    {6,3,2,1,5,8,90,4,70},
                                    {9,5,8,7,20,4,3,1,60},
                                    {8,2,5,4,30,70,1,6,9},
                                    {70,9,1,50,8,6,40,3,2},
                                    {3,40,6,90,1,2,7,50,8},
                                    {2,8,9,60,4,3,5,7,10},
                                    {5,70,3,2,90,1,6,80,4},
                                    {10,6,4,8,7,50,20,9,3}});
    }

    static void testNext() {
        System.err.println("\nTesting next()");
        /* Can be nexted: returns board */
        // cell[3][5] should be "90"
        testNextUnit(new int[][] {{2,9,5,7,4,30,8,6,1},
                                  {4,3,10,8,6,5,9,20,7},
                                  {8,70,6,1,90,2,5,4,3},
                                  {3,8,7,4,5,80,2,1,0},
                                  {6,1,2,3,8,7,4,0,5},
                                  {5,0,9,2,1,6,0,3,8},
                                  {7,6,3,0,2,4,0,8,9},
                                  {9,0,8,6,7,1,3,5,4},
                                  {1,5,0,9,3,8,0,7,2}});
        // cell[0][8] should be "50"
        testNextUnit(new int[][] {{4,10,7,3,60,9,8,2,40},
                                  {6,3,2,1,5,8,0,4,0},
                                  {9,0,8,7,2,4,3,1,6},
                                  {8,2,0,4,3,7,1,6,9},
                                  {7,9,1,0,8,6,4,3,0},
                                  {3,4,6,9,1,0,7,5,8},
                                  {0,8,9,6,4,3,0,7,1},
                                  {5,0,3,2,9,1,0,8,4},
                                  {1,6,4,0,0,5,2,9,3}});
        // cell[8][8] should be "20"
        testNextUnit(new int[][] {{80,1,2,70,50,30,6,4,9},
                                  {90,4,3,60,8,2,10,70,5},
                                  {6,70,5,40,9,1,20,80,3},
                                  {1,50,4,20,30,7,80,90,60},
                                  {30,6,9,8,40,5,7,20,10},
                                  {20,8,7,1,6,9,5,30,4},
                                  {5,20,1,90,7,4,30,60,8},
                                  {4,3,80,5,2,60,9,10,7},
                                  {7,9,6,30,1,80,4,5,10}});
        
        /* Cannot be nexted: returns null */
        // empty
        testNextUnit(new int[][] {{0,0,0,0,0,0,0,0,0},
                                  {0,0,0,0,0,0,0,0,0},
                                  {0,0,0,0,0,0,0,0,0},
                                  {0,0,0,0,0,0,0,0,0},
                                  {0,0,0,0,0,0,0,0,0},
                                  {0,0,0,0,0,0,0,0,0},
                                  {0,0,0,0,0,0,0,0,0},
                                  {0,0,0,0,0,0,0,0,0},
                                  {0,0,0,0,0,0,0,0,0}});
        // all "90"
        testNextUnit(new int[][] {{90,90,90,90,90,90,90,90,90},
                                  {90,90,90,90,90,90,90,90,90},
                                  {90,90,90,90,90,90,90,90,90},
                                  {90,90,90,90,90,90,90,90,90},
                                  {90,90,90,90,90,90,90,90,90},
                                  {90,90,90,90,90,90,90,90,90},
                                  {90,90,90,90,90,90,90,90,90},
                                  {90,90,90,90,90,90,90,90,90},
                                  {90,90,90,90,90,90,90,90,90}});
        // the original problem
        testNextUnit (new int[][] {{3,0,0,0,7,0,0,0,0},
                                    {7,4,6,0,8,1,0,0,2},
                                    {0,8,2,0,0,3,0,0,7},
                                    {0,2,0,3,1,0,0,9,0},
                                    {0,0,0,6,0,7,0,0,0},
                                    {0,1,0,0,2,5,0,7,0},
                                    {5,0,0,2,0,0,9,1,0},
                                    {1,0,0,7,5,0,2,6,3},
                                    {0,0,0,0,4,0,0,0,8}});    
    }

    static void testIsFullSolutionUnit(int[][] test){
        if (isFullSolution(test)) {
            System.err.println("Full sol'n:\t" + Arrays.deepToString(test));
        } else {
            System.err.println("Not full sol'n:\t" + Arrays.deepToString(test));
        }
    }
    
    static void testRejectUnit(int[][] test) {
        if (reject(test)) {
            System.err.println("Rejected:\t" + Arrays.deepToString(test));
        } else {
            System.err.println("Not rejected:\t" + Arrays.deepToString(test));
        }
    }
    
    static void testExtendUnit(int[][] test) {
        System.err.println("Extended to " + Arrays.deepToString(extend(test)));
    }
    
    static void testNextUnit(int[][] test) {
        System.err.println("Nexted  to " + Arrays.deepToString(next(test)));
    }
    
    static void printBoard(int[][] board) {
        if (board == null) {
            System.out.println("No assignment");
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (i == 3 || i == 6) {
                System.out.println("----+-----+----");
            }
            for (int j = 0; j < 9; j++) {
                if (j == 2 || j == 5) {
                    System.out.print(board[i][j] + " | ");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.print("\n");
        }
    }

    static int[][] readBoard(String filename) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
        int[][] board = new int[9][9];
        int val = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    val = Integer.parseInt(Character.toString(lines.get(i).charAt(j)));
                } catch (Exception e) {
                    val = 0;
                }
                board[i][j] = val;
            }
        }
        return board;
    }
    
    // convert newly written values by dividing by 10
    static int[][] convert(int[][] board){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (board[i][j] > 9)
                    board[i][j]/=10;
            }
        }
        return board;
    }

    static int[][] solve(int[][] board) {
        if (reject(board)) return null;
        if (isFullSolution(board)) return convert(board);
        int[][] attempt = extend(board);
        while (attempt != null) {
            int[][] solution = solve(attempt);
            if (solution != null) return solution;
            attempt = next(attempt);
        }
        return null;
    }

    public static void main(String[] args) {
        if (args[0].equals("-t")) {
            testIsFullSolution();
            testReject();
            testExtend();
            testNext();
        } else {
            int[][] board = readBoard(args[0]);
            printBoard(solve(board));
        }
    }
}
