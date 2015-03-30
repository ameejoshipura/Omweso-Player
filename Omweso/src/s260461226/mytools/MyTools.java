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
    	my_pits[origin]=0;
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
    	
    	//if pit was empty, capture not allowed
    	else if(my_pits[dest]<=1){
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
    	else if(op_pits[8+(15-dest)]==0 || op_pits[7-(15-dest)]==0){
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
    	my_pits[origin]=0;
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
    	my_pits[origin]=0;
    	for(int i=0; i<seeds; i++){
    		temp = origin;
    		origin = dest;
    		dest = board_state.getNextPit(temp, Direction.CCW);
    		my_pits[dest]++;
    	}
    	//if pit previously not empty, further sowing is allowed
    	if(my_pits[dest]>1){
    		return true;
    	}
    	//if pit was empty, turn ends, and no further sowing
    	return false;
    }
    
    /*
     * number of successive moves
     */
    public static int countSucc(CCMove m, int[] my_pits, int[] op_pits, CCBoardState board_state, int playerID){
    	int sumSeeds = 0;
    	//int maxMoves = 0;
    	int origin = m.getPit();
    	my_pits[origin]=0;
    	int dest = 0;
    	int temp = 0;
    	int seeds = my_pits[m.getPit()];
    	for(int i=0; i<seeds; i++){
    		temp = origin;
    		origin = dest;
    		dest = board_state.getNextPit(temp, Direction.CCW);
    		if(my_pits[dest]==0){
    			//sumSeeds--;
    		}
    		my_pits[dest]++;
    	}
    	sumSeeds += seeds;
    	//if capture is possible at destination, capture the seeds and
    	//count again
    	if(checkCapture(m,my_pits,op_pits,board_state,playerID)){
    		my_pits[m.getPit()]= calcCapture(m,my_pits,op_pits,board_state,playerID);
    		sumSeeds += countHelp(m.getPit(), my_pits, op_pits, board_state, playerID);    		
    	}
    	else if(my_pits[dest]>1){
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
    	my_pits[origin]=0;
    	int dest = 0;
    	int temp = 0;
    	int seeds = my_pits[m];
    	for(int i=0; i<seeds; i++){
    		temp = origin;
    		origin = dest;
    		dest = board_state.getNextPit(temp, Direction.CCW);
    		if(my_pits[dest]==0){
    			//sumSeeds--;
    		}
    		my_pits[dest]++;
    	}
    	sumSeeds += seeds;
    	//first check if capture is possible
    	CCMove move = new CCMove(m);
    	if(checkCapture(move,my_pits,op_pits,board_state,playerID)){
    		my_pits[origin] = calcCapture(move,my_pits,op_pits,board_state,playerID);
    		sumSeeds += countHelp(origin,my_pits,op_pits,board_state,playerID);
    	}
    	else if(my_pits[dest]>1){
    		sumSeeds += countHelp(dest, my_pits, op_pits, board_state, playerID);
    	}
    	return sumSeeds;
    }
    /*
     * avoid capture by opponent
     * for every x between 8 and 15 in opp pit
     * check if capture is possible in my pit
     * that reduces the heuristic
     * so reduce the amount of capture -> move the
     * seeds and distribute them
     */
    
    /*
     * heuristic search
     * +for #captures
     * -for #pits with seeds=1
     * +for #pits with seeds>1
     * +for succsessive moves
     */
    public static int getHeuristic(CCMove m, int[] my_pits, int[] op_pits, CCBoardState board_state, int playerID){
    	int origin = m.getPit();
    	int h = my_pits[origin];
    	//count captures
    	if(checkCapture(m,my_pits,op_pits,board_state,playerID)){
    		h += calcCapture(m,my_pits,op_pits,board_state,playerID);
    	}
    	//count successive moves - this includes pits with one seed
    	if(checkSucc(m,my_pits,op_pits,board_state,playerID)){
    		h += countSucc(m,my_pits,op_pits,board_state,playerID);
    	}
    	//check for capture by opponent
    	//h += my_pits[15-origin];
    	return h;
    }
}
