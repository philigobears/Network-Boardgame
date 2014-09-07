package player;
import List.*;

public class Board {


	private int[][] board;	
	public int round; //round is updated after this play makes his move
	private static final int EMPTY=0;
	private static final int WHITE=1;
	private static final int BLACK=2;
	private static final int INVALID=3;
	public static final int MAX =  2147483647;
	public static final int MIN = -2147483648;

	// constructor, constructs a new Board
	public Board(){
		board = new int[8][8];
		board[0][0] = INVALID;
		board[0][7] = INVALID;
		board[7][0] = INVALID;
		board[7][7] = INVALID;
		round = 0;
	}

	// return a new copy of this Board
	public Board copy(){
		Board newb = new Board();
		newb.round = round;
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				newb.set(i, j, get(i, j));
			}
		}
		return newb;
	}

	// return the current round
	public int getRound(){
		return round;
	}	

	// return which player has a piece at position i, j. return 3 if it is not a valid position, return 0 if empty
	public int get(int i, int j){
		if(i>7||j>7||i<0||j<0){
			return INVALID;
		}else{
			return board[i][j];
		}		
	}
	// set the position i, j to player. It does not check if this move or player is valid or not.
	private void set(int i, int j, int player){
		board[i][j] = player;
	}


	/**
	 *  setMove method performs a move by an indicated player. It does not check if this move is valid or not.
	 *  It will update the internal board (perform the move m and update the round value).
	 *
	 *  @param m a move that will be performed.
	 *  @param player the player that perform this move m. 
	 *  
	 **/
	protected void setMove(Move m, int player){
		if(m.moveKind==1){
			set(m.x1,m.y1,player);
			round++;
		}else if(m.moveKind==2){
			set(m.x2, m.y2, EMPTY);
			set(m.x1, m.y1, player);
			round++;
		}
	}

	/**
	 *  unsetMove method will erase a move by an indicated player. It does not check if this move is valid or not.
	 *  It will update the internal board (erase the move m and update the round value).
	 *
	 *  @param m a move that will be performed.
	 *  @param player the player that perform this move m. 
	 *  
	 **/
	protected void unsetMove(Move m, int player){
		if(m.moveKind==1){
			set(m.x1,m.y1,EMPTY);
			round--;
		}else if(m.moveKind==2){
			set(m.x2, m.y2, player);
			set(m.x1, m.y1, EMPTY);
			round--;
		}
	}


	// convert this Board to String
	public String toString() {
		String a="";
		for(int j=0;j<8;j++){
			for(int i=0;i<8;i++){

				a=a+" | "+Integer.toString(board[i][j]);
			}
			a+="\n";
		}
		return a;
	}


	// return the enemy of player
	private int giveEnemy(int player) {

		int enemy;
		if (player==WHITE){
			enemy=BLACK;
		}else{
			enemy=WHITE;
		}
		return enemy;
	}


	/**
	 *  isValidMove method determines whether a move on the board is valid for player 1 or 2.  
	 *  (This is the interface)
	 *
	 *  @param move the move that is checking by the indicated player.
	 *  @param player an integer representing the current side of player.
	 *  @return true if the move is valid;
	 *          false otherwise.
	 **/
	public boolean isValidMove(Move move, int player){
		if (move.moveKind==1){
			return isValidMove(move.x1, move.y1, player);
		}else if (move.moveKind==2){
			return isValidMove(move.x1, move.y1, move.x2, move.y2, player);
		}return true;
	}


	/**
	 *  isValidMove method determines whether putting a chip at the 
	 *  position (i,j) on the board is valid for player 1 or 2.  (This is not an interface)
	 *
	 *  @param i first index on the board.
	 *  @param j second index on the board.
	 *  @param player an integer representing the current side of player.
	 *  @return true if place on position(i,j) is legal;
	 *          false otherwise.
	 **/
	protected boolean isValidMove(int i, int j, int player){
		if(round>19){
			return false;
		}
		if(get(i,j)!=EMPTY){
			return false;
		}else if(player!=WHITE&&player!=BLACK){
			return false;
		}else{
			if (player==WHITE){
				if (j==0||j==7){
					return false;
				}
			}else if (player==BLACK){
				if (i==0||i==7){
					return false;
				}
			}
			return (!checkAround(i, j, player));
		}
	}

	/**
	 *  isValidMove method determines whether moving a chip from 
	 *  position (i2,j2) to position (i1,j1) on the board is valid for player 1 or 2.  
	 *  (This is not an interface)
	 *
	 *  @param i1 first index of the target position on the board.
	 *  @param j1 second index of the target position on the board.
	 *  @param i2 first index of the source position on the board.
	 *  @param j2 second index of the source position on the board.
	 *  @param player an integer representing the current side of player.
	 *  @return true if move a chip from position(i2,i2) to position(i1,j1) is legal;
	 *          false otherwise.
	 **/
	protected boolean isValidMove(int i1, int j1, int i2, int j2, int player){
		if(round<20){
			return false;
		}
		if(get(i2,j2)!=player){
			return false;
		}
		if(i1==i2&&j1==j2){
			return false;
		}
		Board temp = copy();
		temp.set(i2, j2, EMPTY);
		temp.round = 0;
		return temp.isValidMove(i1, j1, player);
	}

	// return true if position(i, j) connects to two other chips or it connect to a group of two chips
	private boolean checkAround(int i, int j, int player){
		int [][] a = new int [8][2];
		int c = 0;
		for (int x=-1;x<2;x++){
			if(i+x<0||i+x>7){
				continue;
			}
			for(int y=-1;y<2;y++){
				if(j+y<0||j+y>7){
					continue;
				}
				if(x==0&&y==0){
					continue;
				}
				if(get(i+x,j+y)==player){
					a[c][0] = i+x;
					a[c][1] = j+y;
					c++;					
				}
			}
		}
		if (c>1){
			return true;
		}else if (c==0){
			return false;
		}else{
			int d = 0;
			Board temp = copy();
			temp.set(i, j, player);
			do{
				if (temp.checkAround2(a[d][0], a[d][1], player)){
					return true;
				}d++;
				if (d>7){
					break;
				}
			}while(!temp.checkAround2(a[d][0], a[d][1], player));
			return false;
		}
	}

	// return true if position(i, j) connects to two other chips
	private boolean checkAround2(int i, int j, int player){
		int c=0;
		for (int x=-1;x<2;x++){
			if(i+x<0||i+x>7){
				continue;
			}
			for(int y=-1;y<2;y++){
				if(j+y<0||j+y>7){
					continue;
				}
				if(x==0&&y==0){
					continue;
				}
				if(get(i+x,j+y)==player){
					c++;					
				}
			}
		}
		if (c>1){
			return true;
		}else{
			return false;
		}
	}




	/**
	 *  allValidMoves method generates a list of all the legal moves for player 1 or 2.  
	 *
	 *  @param player an integer representing the current side of player which is 
	 *        checking all legal moves right now
	 *  @return List a list of moves which are legal for player 1 or 2
	 **/
	protected DList allValidMoves(int player){
		DList list = new DList();
		if (round<20){
			for (int i=0;i<8;i++){
				for (int j=0;j<8;j++){
					if(isValidMove(i,j,player)){
						list.insertBack(new Move(i, j));
					}
				}
			}
		}else{
			for (int i2=0;i2<8;i2++){
				for (int j2=0;j2<8;j2++){
					if(get(i2, j2)==player){
						for (int i1=0;i1<8;i1++){
							for (int j1=0;j1<8;j1++){
								if (isValidMove(i1, j1, i2, j2, player)){
									list.insertBack(new Move(i1,j1,i2,j2));
								}
							}
						}
					}
				}
			}
		}
		return list;
	}



	/**
	   *  hasNetwork method determines whether "this" GameBoard has a valid network
	   *  for player 1 or 2.  
	   *
	   *  Unusual conditions:
	   *    If the player is neither MachinePlayer.COMPUTER nor MachinePlayer.OPPONENT,
	   *          returns false.
	   *    If GameBoard squares contain illegal values, the behavior of this
	   *          method is undefined (i.e., don't expect any reasonable behavior).
	   *
	   *  @param player the player whose board evaluated for, either WHITE or BLACK.
	   *  @return true if this player has a winning network in "this" GameBoard;
	   *          false otherwise.
	   **/
	protected boolean hasNetwork(int player){
		DList list;
		int[] start = new int [2];
		if(player==WHITE){ //white
			if(get(7,1)+get(7,2)+get(7,3)+get(7,4)+get(7,5)+get(7,6)==0){
				return false; //no chip at the right goal area
			}
			for (int j=0;j<8;j++){
				if(get(0,j)==player){
					start[0] = 0;
					start[1] = j;
					list = new DList();
					list.insert(start);
					if(pass(start, 0, WHITE, list)){
						return true;
					}
				}				
			}
		}else if (player==BLACK){ //black
			if(get(1,7)+get(2,7)+get(3,7)+get(4,7)+get(5,7)+get(6,7)==0){
				return false; //no chip at the bottom goal area
			}
			for (int i=0;i<8;i++){
				if(get(i,0)==player){
					start[0] = i;
					start[1] = 0;
					list = new DList();
					list.insert(start);
					if(pass(start, 2, BLACK, list)){
						return true;
					}
				}				
			}
		}
		return false;
	}


	// it is a helper function which will be recursively called to move along a linked group of chips to see if a particular branch can form a network
	private boolean pass(int[] position, int direction, int player, DList list){
		if(position==null){
			return false;
		}
		if(player==WHITE){
			if(position[0]==7&&list.length()>5){
				return true;
			}else if(position[0]==7){
				return false;
			}else if(position[0]==0&&list.length()>1){
				return false;
			}
		}else if(player==BLACK){
			if(position[1]==7&&list.length()>5){
				return true;
			}else if(position[1]==7){
				return false;
			}else if(position[1]==0&&list.length()>1){
				return false;
			}
		}
		int[][] a = new int[8][2];
		DList nlist;
		for (int i=0;i<8;i++){
			if (i==direction||i==reverse(direction)){ //make sure it turns
				a[i] = null;
			}else{
				a[i] = shoot(position, i, player);
			}			
		}
		for (int i=0;i<8;i++){
			if(a[i]==null){
				continue;
			}else{
				nlist = list.copy();
				if(nlist.insert(a[i])){
					if(pass(a[i], i, player, nlist)){
						return true;
					}
				}				
			}
		}
		return false;
	}

	// it shoots to a direction and return the coordinates a the first friendly chip it sees. If it sees the edge of the Board or an enemy chip, it returns null.
	private int[] shoot(int[] position, int direction, int player){
		int m = position[0];
		int n = position[1];
		int [] r = new int[2];
		int enemy;
		if (player==WHITE){
			enemy=BLACK;
		}else{
			enemy=WHITE;
		}
		switch(direction){
		case 0: while (n>0 && this.get(m,n-1)!=enemy){
			n=n-1;
			if (this.get(m,n)==player){
				r[0] = m;
				r[1] = n;
				return r;
			}
		}return null;

		case 1: while (m<7 && n>0 && this.get(m+1,n-1)!=enemy){
			m=m+1;
			n=n-1;
			if (this.get(m,n)==player){
				r[0] = m;
				r[1] = n;
				return r;
			}
		}return null;

		case 2: while (m<7 && this.get(m+1,n)!=enemy){
			m=m+1;
			if (this.get(m,n)==player){
				r[0] = m;
				r[1] = n;
				return r;
			}
		}return null;

		case 3: while (n<7 && m<7 && this.get(m+1,n+1)!=enemy){
			n=n+1;
			m=m+1;
			if (this.get(m,n)==player){
				r[0] = m;
				r[1] = n;
				return r;
			}
		}return null;

		case 4: while (n<7 && this.get(m,n+1)!=enemy){
			n=n+1;
			if (this.get(m,n)==player){
				r[0] = m;
				r[1] = n;
				return r;
			}
		}return null;

		case 5: while (m>0 && n<7 && this.get(m-1,n+1)!=enemy){
			m=m-1;
			n=n+1;
			if (this.get(m,n)==player){
				r[0] = m;
				r[1] = n;
				return r;
			}
		}return null;

		case 6: while (m>0 && this.get(m-1,n)!=enemy){
			m=m-1;
			if (this.get(m,n)==player){
				r[0] = m;
				r[1] = n;
				return r;
			}
		}return null;

		case 7: while (n>0 && m>0 && this.get(m-1,n-1)!=enemy){
			n=n-1;
			m=m-1;
			if (this.get(m,n)==player){
				r[0] = m;
				r[1] = n;
				return r;
			}
		}
		}
		return null;
	}


	// returns the reverse of direction
	private int reverse(int direction){
		direction+=4;
		if(direction>8){
			direction-=8;
		}
		return direction;
	}


	private Board flip(){
		Board newb = new Board();
		newb.round = round;
		for (int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if(get(i,j)==WHITE){
					newb.set(j,i,BLACK);
				}else if(get(i,j)==BLACK){
					newb.set(j,i,WHITE);
				}
			}
		}return newb;
	}



	/**
	   *  evaluate method assigns a score to "this" board after playing a move. It can estimate how well my 
	   *  machine player can do, so that I can choose the best move in this turn.
	   *
	   *  Unusual conditions:
	   *    If the current board is an empty board, then the return score should be zero.
	   *    If your MachinePlayer has a valid network, then the return score should be the maximum 
	   *           value in 32bit integer. (By declaring public static final int MAX_VALUE)
	   *
	   *  @param move a move that will be evaluated
	   *  @param player the player whose board is evaluating for, either WHITE or BLACK.
	   *  @return an integer which is the score of "this" board after performing the move.
	   **/
	protected int evaluate(Move move, int player) {
		setMove(move, player);
		int score;
		if (hasNetwork(player)){
			score = MAX;
		}else{
			int myConn = connections(player);
			int enemy = this.giveEnemy(player);
			int enemyConn = connections(enemy);
			score = myConn - enemyConn;
		}
		unsetMove(move, player);
		return score;
	}


	// counts the connections of the indicated player.
	private int connections(int player) {

		int count = 0;
		int enemy = this.giveEnemy(player);

		for (int j=0; j<8; j++){
			for (int i=0; i<8; i++){

				int temp1=0;
				int temp2=0;	
				int temp3=0;
				int temp4=0;

				int m=i;
				int n=j;
				while (this.get(i,j)==player && n>0 && m>0 && this.get(m-1,n-1)!=enemy){
					n=n-1;
					m=m-1;
					if (this.get(m,n)==player){
						temp1+=1;
						break;
					}
				}
				m=i;
				n=j;
				while (this.get(i,j)==player && temp1==0 && n<7 && m<7 && this.get(m+1,n+1)!=enemy){
					n=n+1;
					m=m+1;
					if (this.get(m,n)==player){
						temp1+=1;
						break;
					}
				}
				m=i;
				n=j;
				while (this.get(i,j)==player && n>0 && this.get(m,n-1)!=enemy){
					n=n-1;
					if (this.get(m,n)==player){
						temp2+=1;
						break;
					}
				}
				m=i;
				n=j;
				while (this.get(i,j)==player && temp2==0 && n<7 && this.get(m,n+1)!=enemy){
					n=n+1;
					if (this.get(m,n)==player){
						temp2+=1;
						break;
					}
				}
				m=i;
				n=j;
				while (this.get(i,j)==player && m<7 && n>0 && this.get(m+1,n-1)!=enemy){
					m=m+1;
					n=n-1;
					if (this.get(m,n)==player){
						temp3+=1;
						break;
					}
				}
				m=i;
				n=j;
				while (this.get(i,j)==player && temp3==0 && m>0 && n<7 && this.get(m-1,n+1)!=enemy){
					m=m-1;
					n=n+1;
					if (this.get(m,n)==player){
						temp3+=1;
						break;
					}
				}
				m=i;
				n=j;
				while (this.get(i,j)==player && m<7 && this.get(m+1,n)!=enemy){
					m=m+1;
					if (this.get(m,n)==player){
						temp4+=1;
						break;
					}
				}
				m=i;
				n=j;
				while (this.get(i,j)==player && temp4==0 && m>0 && this.get(m-1,n)!=enemy){
					m=m-1;
					if (this.get(m,n)==player){
						temp4+=1;
						break;
					}
				}

				count=count+temp1+temp2+temp3+temp4;
			}
		}
		return count;
	}

}
