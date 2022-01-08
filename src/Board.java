public class Board {
	private Tile[][] board;
	private int size;
	
	public Board(int n){
		this.board = new Tile[n][n];
		this.size = n;
		initBoard();
	}
	
	public Board(Board copy){
		this.size = copy.getSize();
		this.board = copyBoard(copy);
	}
	
	private Tile[][] copyBoard(Board copy) {
		Tile[][] toReturn = new Tile[size][size];
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				toReturn[i][j] = new Tile(copy.board[i][j]);
			}
		}
		return toReturn;
	}

	private void initBoard() {
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				board[i][j] = new Tile(i,j);
			}
		}
	}

	public Tile getTile(int x, int y){
		return board[x][y];
	}
	
	public Tile getTile(NQueen.Coords coords){
		return board[coords.x][coords.y];
	}
	
	public int getSize(){
		return this.size;
	}


	/**
	 * This function is used to show the completed boards of the n-queen problems
	 */
	public String toString(){
		StringBuilder toReturn = new StringBuilder("");
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				if(getTile(i,j).hasQ())
					toReturn.append("|Q|");
				else
					toReturn.append("|_|");
			}
			toReturn.append("\n");
		}
		return toReturn.toString();
	}

}
