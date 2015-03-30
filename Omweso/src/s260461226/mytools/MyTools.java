package s260461226.mytools;

import omweso.CCBoardState;
import omweso.CCBoardState.Direction;
import omweso.CCMove;


public class MyTools {

    public static double getSomething(){
        return Math.random();
    }
    /*
     * check if a capture is possible
     */
    public static boolean checkCapture(CCMove m, int[] my_pits, int[] op_pits, CCBoardState board_state, int playerID){
    	//figure out the last pit
    	int seeds = my_pits[m.getPit()];
    	int origin = m.getPit();
    	int dest=0;
    	int temp=0;
    	for(int i=0; i<seeds; i++){
    		temp = origin;
    		origin = dest;
    		dest = board_state.getNextPit(temp, Direction.CCW);
    	}
    	//if the last pit is in the closer row, then capture cannot take place
    	//return false
    	if(dest<board_state.SIZE || dest>=2*board_state.SIZE){
    		return false;
    	}
    	/*
    	 * if the last pit is in the farther row, either of the pits in the same line of opponent
    	 * are empty, return false
    	 * pit in farther row -> x (between 8 and 15)
    	 * 7  6  5  4  3  2  1  0
    	 * 8  9  10 11 12 13 14 15
    	 * 15 14 13 12 11 10 9  8
    	 * 0  1  2  3  4  5  6  7
    	 * 
    	 * then opp pit -> 8+(15-x), and 7-(15-x) 
    	 */
    	if(op_pits[8+(15-dest)]==0 || op_pits[7-(15-dest)]==0){
    		return false;
    	}
    	return true;
    }
    
    /*
     * get max capture
     */
    public static int calcCapture(CCMove m, int[] my_pits, int[] op_pits, CCBoardState board_state, int playerID){
    	//figure out the last pit
    	int seeds = my_pits[m.getPit()];
    	int origin = m.getPit();
    	int dest=0;
    	int temp=0;
    	for(int i=0; i<seeds; i++){
    		temp = origin;
    		origin = dest;
    		dest = board_state.getNextPit(temp, Direction.CCW);
    	}
    	//return capture
    	return op_pits[8+(15-dest)]+op_pits[7-(15-dest)];
    }
    
    /*
     * check for successive moves
     */
    public static boolean checkSucc(CCMove m, int[] my_pits, int[] op_pits, CCBoardState board_state, int playerID){
    	int origin = m.getPit();
    	int seeds = my_pits[m.getPit()];
    	int dest=0;
    	int temp=0;
    	for(int i=0; i<seeds; i++){
    		temp = origin;
    		origin = dest;
    		dest = board_state.getNextPit(temp, Direction.CCW);
    	}
    	if(my_pits[dest]>1){
    		return true;
    	}
    	return false;
    }
    
    /*
     * number of successive moves
     */
    public static int countSucc(CCMove m, int[] my_pits, int[] op_pits, CCBoardState board_state, int playerID){
    	int sumSeeds = 0;
    	//int maxMoves = 0;
    	int origin = m.getPit();
    	int dest = 0;
    	int temp = 0;
    	int seeds = my_pits[m.getPit()];
    	for(int i=0; i<seeds; i++){
    		temp = origin;
    		origin = dest;
    		dest = board_state.getNextPit(temp, Direction.CCW);
    	}
    	sumSeeds = seeds;
    	if(my_pits[dest]>1){
    		sumSeeds += countHelp(dest, my_pits, op_pits, board_state, playerID);
    	}
    	return sumSeeds;
    }
    
    /*
     * count helper
     */
    public static int countHelp(int m, int[] my_pits, int[] op_pits, CCBoardState board_state, int playerID){
    	int sumSeeds = 0;
    	//int maxMoves = 0;
    	int origin = m;
    	int dest = 0;
    	int temp = 0;
    	int seeds = my_pits[m];
    	for(int i=0; i<seeds; i++){
    		temp = origin;
    		origin = dest;
    		dest = board_state.getNextPit(temp, Direction.CCW);
    	}
    	//first check if capture is possible
    	CCMove move = new CCMove(m);
    	if(checkCapture(move,my_pits,op_pits,board_state,playerID)){
    		sumSeeds = calcCapture(move,my_pits,op_pits,board_state,playerID);
    	}
    	sumSeeds = seeds;
    	if(my_pits[dest]>1){
    		sumSeeds += countHelp(dest, my_pits, op_pits, board_state, playerID);
    	}
    	return sumSeeds;
    }
}
