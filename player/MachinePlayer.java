/* MachinePlayer.java */

package player;

import List.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	public int myColor;
	public int depth;
	private Board myBoard; 
	public static final int MAX =  2147483647;
	public static final int MIN = -2147483648;


	// Creates a machine player with the given color.  Color is either 0 (black)
	// or 1 (white).  (White has the first move.)
	public MachinePlayer(int color) {
		this.myColor = color;
		if (myColor == 0){
			myColor = BLACK;
		} 
		this.myBoard = new Board();
		this.depth = 3;

	}


	// Creates a machine player with the given color and search depth.  Color is
	// either 0 (black) or 1 (white).  (White has the first move.)
	public MachinePlayer(int color, int searchDepth) {
		this(color);
		this.depth = searchDepth;
	}


	// return the enemy of player
	public int enemy(int color){
		int enemy;
		if (color == WHITE){
			enemy = BLACK;
		}else{
			enemy = WHITE;
		}
		return enemy;
	}

	/**
	   *  chooseMove method returns a new good move by my machine player after searching and evaluating all 
	   *  the possible moves by my machine player. It also internally records the move (updates
	   *  the internal game board) as a move by "this" player.
	   *
	   *  @return a new good move by my machine player
	   **/
	public Move chooseMove() {
		Move init;
		if(myBoard.getRound()<2){   // make my first move
			for (int i=3;i<5;i++){
				for (int j=3;i<5;j++){
					init = new Move(i, j);
					if (myBoard.isValidMove(init, myColor)){
						myBoard.setMove(init, myColor);
						return init;
					}
				}
			}
		}else if (myBoard.getRound()<4){   // make my second move
			for (int i=4;i<6;i++){
				for (int j=4;i<6;j++){
					init = new Move(i, j);
					if (myBoard.isValidMove(init, myColor)){
						myBoard.setMove(init, myColor);
						return init;
					}
				}
			}
		}

		Best myBest = new Best(); // My best move
		myBest.score = MIN;
		int currScore = 0;
		DList validMoves = myBoard.allValidMoves(myColor);
		DListNode currMove = validMoves.front();
		while (currMove!=null) {
			myBoard.setMove((Move)currMove.item, myColor);
			if (myBoard.hasNetwork(myColor)){
				return (Move)currMove.item;
			}
			if(myBoard.getRound()>20){
				currScore = getMin(enemy(myColor), MIN, MAX, 0, this.depth-1);
			}else{
				currScore = getMin(enemy(myColor), MIN, MAX, 0, this.depth);
			}
			myBoard.unsetMove((Move)currMove.item, myColor);
			if (currScore > myBest.score){
				myBest.score = currScore;
				myBest.move = (Move)currMove.item;
			}
			currMove=validMoves.next(currMove);
		}
		myBoard.setMove(myBest.move, myColor);
		return myBest.move;
	} 

	// getMax method will return the move with the minimal score for my enemy
	public int getMin(int color, int alpha, int beta, int depth, int searchDepth) {

		beta = MAX;
		int currScore = 0;
		DList validMoves = myBoard.allValidMoves(color);
		DListNode currMove = validMoves.front();
		if (depth == searchDepth){
			return myBoard.evaluate((Move)currMove.item, color);
		}

		while (currMove!=null) {
			myBoard.setMove((Move)currMove.item, color);
			if (myBoard.hasNetwork(enemy(myColor))) {
				currScore = MIN/(depth+2);
				myBoard.unsetMove((Move)currMove.item, color);
				return currScore;
			}
			currScore = getMax(enemy(color), alpha, beta, depth+1, searchDepth);
			myBoard.unsetMove((Move)currMove.item, color);

			if (currScore < beta){
				beta = currScore;
			}
			if (alpha>=beta){   // alpha-beta pruning
				return currScore;
			}
			currMove=validMoves.next(currMove);
		}
		return currScore;
	}

	// getMax method will return the move with the maximal score for my player
	public int getMax(int color, int alpha, int beta, int depth, int searchDepth) {

		alpha = MIN;
		int currScore = 0;
		DList validMoves = myBoard.allValidMoves(color);
		DListNode currMove = validMoves.front();
		if (depth == searchDepth){
			return myBoard.evaluate((Move)currMove.item, color);
		}

		while (currMove!=null) {
			myBoard.setMove((Move)currMove.item, color);
			if (myBoard.hasNetwork(myColor)) {
				currScore = MAX/(depth+2);
				myBoard.unsetMove((Move)currMove.item, color);
				return currScore;
			}
			currScore = getMin(enemy(color), alpha, beta, depth+1, searchDepth);
			myBoard.unsetMove((Move)currMove.item, color);
			if (currScore > alpha){
				alpha = currScore;
			}

			if (alpha >= beta){    // alpha-beta pruning
				return currScore;
			}
			currMove=validMoves.next(currMove);
		}
		return currScore;
	}



	/**
	   *  opponentMove method informs me of the move by my opponent.
	   *  It returns true if the Move m is legal, and records the move by opponent and updates 
	   *  the internal board of my opponent.
	   *  It returns false if the move is illegal,  without modifying the internal state of my opponent.
	   *
	   *  @param m the move the opponent made
	   *  @return true if the Move is legal, false otherwise
	   **/
	public boolean opponentMove(Move m) {
		if (myBoard.isValidMove(m,enemy(myColor))){
			myBoard.setMove(m, enemy(myColor));
			return true;
		} else {
			return false;
		}
	}


	/**
	   *  forceMove method records my own legal move.
	   *  It returns true if my move is legal, and records the move and updates the internal board of my machine player.
	   *  It returns false if the move is illegal, without modifying the internal state of my machine player.
	   *
	   *  @param m the move my machine player made
	   *  @return true if the Move is legal, false otherwise
	   **/
	public boolean forceMove(Move m) {
		if (myBoard.isValidMove(m,myColor)){
			myBoard.setMove(m, myColor);
			return true;
		} else {
			return false;
		}
	}


	// convert a Board to String
	public String toString() {
		String a="";
		for(int j=0;j<8;j++){
			for(int i=0;i<8;i++){

				a=a+" | "+Integer.toString(myBoard.get(i,j));
			}
			a+="\n";
		}
		return a;
	}


	//		public static void main (String[] args){
	//			MachinePlayer me= new MachinePlayer(BLACK);
	//			Move new1 = new Move(1,1);
	//			Move new2 = new Move(3,2);
	//			Move new3 = new Move(3,3);
	//			Move new4 = new Move(1,3);
	//			Move new5 = new Move(2,1);
	//			Move new6 = new Move(1,4);
	//			Move new7 = new Move(4,4);
	//			Move new8 = new Move(3,4);
	//			Move new9 = new Move(0,6);
	//			Move new10 = new Move(4,5);
	//			Move new11= new Move(2,6);
	//			Move new12 = new Move(2,7);
	//			Move new13 = new Move(3,6);
	//			me.forceMove(new2);
	//			me.opponentMove(new1);
	//			me.forceMove(new4);
	//			me.opponentMove(new3);
	//			me.forceMove(new6);
	//			me.opponentMove(new5);
	//			me.forceMove(new8);
	//			me.opponentMove(new7);
	//			me.forceMove(new10);
	//			me.opponentMove(new9);
	//			me.forceMove(new12);
	//			me.opponentMove(new11);
	//			me.opponentMove(new13);
	//			
	//			//Move new9 = new Move(6,6);
	////			if(me.myBoard.isValidMove(6,6,WHITE)){
	////				System.out.println("valid");
	////			}
	//			System.out.println(me.myBoard.toString());
	////			DList test = me.myBoard.allValidMoves(BLACK);
	////			System.out.println(test.toString());
	//	
	//		Move test = me.chooseMove();
	//		System.out.println(test.toString());
	////			DList all = me.myBoard.allValidMoves(WHITE);
	////			System.out.println(all.toString());
	////			System.out.println(all.length());
	////			System.out.println(me.toString());
	//		}

}



