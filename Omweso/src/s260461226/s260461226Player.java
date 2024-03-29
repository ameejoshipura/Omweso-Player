package s260461226;

import boardgame.Board;
import boardgame.BoardState;
import boardgame.Move;
import boardgame.Player;
import omweso.CCBoardState;
import omweso.CCBoard;
import omweso.CCMove;

import java.util.ArrayList;
import java.util.Random;

import s260461226.mytools.MyTools;

/** A random Omweso player. */
public class s260461226Player extends Player {
    Random rand = new Random();

    /** You must provide a default public constructor like this,
     * which does nothing but call the base-class constructor with
     * your student number. */
    public s260461226Player() { super("260461226"); }
    public s260461226Player(String s) { super(s); }

    /** Leave this method unchanged. */
    public Board createBoard() { return new CCBoard(); }

    /** Use this method to take actions when the game is over. */
    public void gameOver( String msg, BoardState bs) {
        CCBoardState board_state = (CCBoardState) bs;

        if(board_state.haveWon()){
            System.out.println("I won!");
        }else if(board_state.haveLost()){
            System.out.println("I lost!");
        }else if(board_state.tieGame()){
            System.out.println("Draw!");
        }else{
            System.out.println("Undecided!");
        }
    }

    /** Implement a very stupid way of picking moves. */
    public Move chooseMove(BoardState bs)
    {
        // Cast the arguments to the objects we want to work with.
        CCBoardState board_state = (CCBoardState) bs;

        // Get the contents of the pits so we can use it to make decisions.
        int[][] pits = board_state.getBoard();

        // Our pits in first row of array, opponent pits in second row.
        int[] my_pits = pits[0];
        int[] op_pits = pits[1];

        // Use my tool to do precisely nothing
        //MyTools.getSomething();

        if(!board_state.isInitialized()){
            // Code for setting up our initial position. Also, if your agent needs to be
            // set-up in any way (e.g. loading data from files) you should do it here.

            //CCBoardState.SIZE is the width of the board, in pits.
            int[] initial_pits = new int[2 * CCBoardState.SIZE];

            // Make sure your initialization move includes the proper number of seeds.
            int num_seeds = CCBoardState.NUM_INITIAL_SEEDS;

            if(board_state.playFirst()){
                // If we're going to play first in this game, choose one random way of setting up.
                // put 4 in each pit in the second row
                for(int i=CCBoardState.SIZE;i<2*CCBoardState.SIZE;i++ ){
                	initial_pits[i]=4;
                }
            }else{
                // If we're going to play second this game, choose another random way of setting up.
                // put 4 in each pit in the second row
            	for(int i=CCBoardState.SIZE;i<2*CCBoardState.SIZE;i++ ){
                	initial_pits[i]=4;
                }
            }

            return new CCMove(initial_pits);
        }else{
            // Play a normal turn. Choose a random pit to play.
        	
        	// make a clone of the board
        	CCBoardState cloneState = (CCBoardState)board_state.clone();
        	
            ArrayList<CCMove> moves = board_state.getLegalMoves();
            CCMove bestCapMove = null;
            int captureMax = 0;
            int temp = 0 ;
            int max = 0;
            CCMove maxMove = null;
            maxMove = moves.get(0);
            max = MyTools.getHeuristic(maxMove, my_pits, op_pits, cloneState, this.playerID);
            for(CCMove m: moves){
            	//if the move results in a capture, return that move
            	//if a move results in a capture, return the move with max capture
            	/*if(MyTools.checkCapture(m, my_pits, op_pits, cloneState,this.playerID))
            	{ 
            		temp = MyTools.calcCapture(m, my_pits, op_pits, cloneState, this.playerID);
            		if(temp>captureMax){
            			bestCapMove = m;
            			captureMax = temp;
            		}
            	} 
            	//if no captures are possible, then return the move with most seeds
            	//return the move with max consecutive sowings possible
            	else{
            	//check if multiple sowings are possible
            		if(MyTools.checkSucc(m, my_pits, op_pits, cloneState, this.playerID)){
            			temp = MyTools.calcCapture(m, my_pits, op_pits, cloneState, this.playerID);
            			if(temp > max){
            				max = temp;
            				maxMove = m;
            			}
            		}
            		if(my_pits[m.getPit()]>max){
            			max = my_pits[m.getPit()];
            			maxMove = m;
            		}
            	}*/
            	//heuristic
            	temp = MyTools.getHeuristic(m, my_pits, op_pits, cloneState, this.playerID);
            	if(temp>max){
            		max = temp;
            		maxMove = m;
            	}
            }
            /*if(bestCapMove!=null){
            	return bestCapMove;
            }*/
            return maxMove;
            //return moves.get(rand.nextInt(moves.size()));
        }
    }
}
