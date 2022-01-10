/**
 * This purpose of this file is to solve the trials for the multiple n-queen problems
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

public class NQueen {
	
	class Coords{
		int x;
		int y;

		public Coords(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	private Board board;
	private List<Coords> queens;
	private Map<Tile, ArrayList<Tile>> map;
	private PriorityQueue<Node> boardQ;
	private double trials;
	private double success;
	private long totalTime;
	private long avgTime;
	private boolean debug;
	
	public NQueen(){
		trials = 0;
		success = 0;
		map = new HashMap<Tile, ArrayList<Tile>>();
		queens = new ArrayList<Coords>();
		debug = false;
	}
	
	public NQueen(int n){
		trials = 0;
		success = 0;
		debug = false;
		makeBoard(n);	
	}
	
	public void makeBoard(int n){
		this.board = new Board(n);
		map = new HashMap<Tile, ArrayList<Tile>>(n);
		queens = new ArrayList<Coords>(n);
	}

	public Board getBoard(){
		return this.board;
	}

	public String getRep(){
		return board.toString();
	}

	public void genRandConfig(){
		Random rand = new Random();
		int randX;
		int randY;
		for(int i = 0; i < board.getSize(); i++){
			randX = Math.abs(rand.nextInt()%board.getSize());
			randY = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(randX,randY))
				placeQ(randX, randY);
			else
				i--;
		}
	}
	
	public void genSemiRandConfig(){
		Random rand = new Random();
		int randY;
		for(int i = 0; i < board.getSize(); i++){
			randY = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(i,randY))
				placeQ(i, randY);
			else
				i--;
		}
	}
	
	public void genSemiRandConfig(int size){
		makeBoard(size);
		Random rand = new Random();
		int randY;
		for(int i = 0; i < board.getSize(); i++){
			randY = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(i,randY))
				placeQ(i, randY);
			else
				i--;
		}
	}

	public void nTrials(int n, int size, int mode, int additional1, double additional2, int debug){
		long startTime;
		long endTime;
		long time;
		totalTime = 0;
		avgTime = 0;
		trials = 0;
		success = 0;
		if(debug == 1)
			this.debug = true;
		else if (debug == 0)
			this.debug = false;
		if(board == null){
			makeBoard(size);
			genSemiRandConfig();
		}
		for(int i = 0; i < n; i++){
			switch(mode){
			case 1:
				startTime = System.currentTimeMillis();
				steepestHillClimbing();
				endTime = System.currentTimeMillis();
				time = endTime - startTime;
				totalTime += time;
				break;
			case 2:
				startTime = System.currentTimeMillis();
				minConflicts(additional1);
				endTime = System.currentTimeMillis();
				time = endTime - startTime;
				totalTime += time;
				break;
			default:
				continue;
			}
			avgTime = totalTime/n;
			board = new Board(size);
			queens.clear();
			map.clear();
			if(boardQ != null)
				boardQ.clear();
			genSemiRandConfig();
		}
	}
	
	public String getReport(){
		String toReturn;
		toReturn = "Trials: "+(int)trials+"\nSuccess Rate: "+(double)(success/trials)*100+"%";
		return toReturn;
	}
	
	private void replaceQ(Tile variable, Tile value) {
		removeQ(variable);
		placeQ(value);
	}

	public boolean checkQ(int x, int y){
		return board.getTile(x, y).hasQ();
	}

	public void placeQ(int x, int y){
		if(board.getTile(x, y).hasQ())
			return;
		else{
			board.getTile(x, y).placeQ();
			queens.add(x, new Coords(x,y));
		}
	}
	
	private void placeQ(Tile value) {
		placeQ(value.getX(), value.getY());
	}
	
	public void placeQ(Node current, int x, int y){
		if(current.board.getTile(x, y).hasQ())
			return;
		else{
			current.board.getTile(x, y).placeQ();
			current.queens.add(x, new Coords(x,y));
		}
	}
	
	public void removeQ(int x, int y){
		if(board.getTile(x, y).hasQ()){
			board.getTile(x, y).removeQ();
			queens.remove(x);
			return;
		}
		else
			return;
	}
	
	private void removeQ(int x) {
		@SuppressWarnings("unused")
		Coords coords;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(x, i).hasQ()){
				coords = new Coords(x,i);
				board.getTile(x, i).removeQ();
				queens.remove(x);
				return;
			}
		}
	}
	
	private void removeQ(Tile variable) {
		removeQ(variable.getX());
	}

	public int getAllAttackers(){
		int allAttackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			for(int j = 0; j < board.getSize(); j++){
				if(board.getTile(i,j).hasQ())
					allAttackers += getAttackers(i,j);
			}
		}
		map.clear();
		return allAttackers;
	}
	
	private int getAttackers(int x, int y){
		int toReturn = 0;
		toReturn += getColAttackers(x, y);
		toReturn += getRowAttackers(x, y);
		toReturn += getDiaAttackers(x, y);
		board.getTile(x,y).setValue(toReturn);
		return toReturn;
	}
	
	private int getDiaAttackers(int x, int y) {
		int i = x; int j = y;
		int attackers = 0;
		while(i < board.getSize() && j < board.getSize()){ //down-right
			if(board.getTile(i, j).hasQ() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i++;
			j++;
		}
		i = x; j = y;
		while(i < board.getSize() && j >= 0){ //down-left
			if(board.getTile(i, j).hasQ() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i++;
			j--;
		}
		i = x; j = y;
		while(i >= 0 && j < board.getSize()){ //up-right
		if(board.getTile(i, j).hasQ() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
			attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i--;
			j++;
		}
		i = x; j = y;
		while(i >= 0 && j >= 0){ //up-left
			if(board.getTile(i, j).hasQ() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i--;
			j--;
		}
		return attackers;
	}

	private int getRowAttackers(int x, int y) {
		int attackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(x, i).hasQ() && i != y && !counted(board.getTile(x, y), board.getTile(x,i)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(x,i));
		}
		return attackers;
	}

	private int getColAttackers(int x, int y) {
		int attackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(i, y).hasQ() && i != x && !counted(board.getTile(x, y), board.getTile(i,y)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i,y));
		}
		return attackers;
	}
	
	private int incAttackers(int count, Tile first, Tile second){
		count++;
		if(map.get(first) != null && map.get(second) != null){
			if(!map.get(first).contains(second) && !map.get(second).contains(first)){
				map.get(first).add(second);
				map.get(second).add(first);
			}
		}else if(map.get(first) == null && map.get(second) != null){
			map.put(first, new ArrayList<Tile>());
			map.get(first).add(second);
			map.get(second).add(first);
		}else if(map.get(second) == null && map.get(first) != null){
			map.put(second, new ArrayList<Tile>());
			map.get(first).add(second);
			map.get(second).add(first);
		}else{
			map.put(first, new ArrayList<Tile>());
			map.put(second, new ArrayList<Tile>());
			map.get(first).add(second);
			map.get(second).add(first);
		}
		return count;
	}
	
	private boolean counted(Tile first, Tile second){
		if(map.isEmpty())
			return false;
		else if(map.containsKey(first))
			if(map.get(first).contains(second))
				return true;
			else
				return false;
		else
			return false;
	}

	/**
	 * Runs a steepest-ascent hill-climbing algorithm with the given initial
	 * state and returning the resulting states.
	 */
	public Node steepestHillClimbing(){
		Node current;
		Node neighbor;
		current = new Node(board, getAllAttackers(), (ArrayList<Coords>) queens);
		boardQ = new PriorityQueue<Node>();
		while(true){
			neighbor = genNeighbor(current);
			if(neighbor.value >= current.value){
				trials++;
				if(current.value == 0){
					success++;
					if(debug)
						System.out.println(current.board.toString());
				}
				return current;
			}
			current = neighbor;
		}
	}
	
	private Node genNeighbor(Node current) {
		Coords coords;
		Node copy = new Node(current);
		for(int i = 0; i < copy.board.getSize(); i++){
			for(int j = 0; j < copy.board.getSize(); j++){
				coords = new Coords(i,j);
				if(!copy.board.getTile(i, j).hasQ()){
					this.board = copy.board;			//focus on board
					this.queens = copy.queens;			//get list of queens for that board
					removeQ(i);							//remove queen in row i
					placeQ(i,j);						//move queen to coord i,j
					copy.setValue(getAllAttackers());	//get new value
					boardQ.add(copy);					//enqueue copy node
					copy = new Node(current);			//make new copy
				}
			}
		}
		Node best = boardQ.poll();
		return best;
	}

	/**
	 * Runs the minConflict algorithm
	 * This function will choose a random column and calculate the heuristic for each of the coordinates within that column.
	 * If it finds a heuristic that is less than or equal to the current heuristic the queen will be moved there
	 */
	public Node minConflicts(int maxSteps){
		trials++;
		Node current;
		Tile variable;
		Tile value;
		boolean conflicted = false;
		Random rand = new Random();
		current = new Node(board, getAllAttackers(), (ArrayList<Coords>) queens);
		for(int i = 0; i < maxSteps; i++){
			if(current.value == 0){
				success++;
				if(debug)
					System.out.println(board.toString());
				return current;
			}
			do{
				variable = board.getTile(queens.get(Math.abs(rand.nextInt()%queens.size())));
				if(getThreats(variable) > 0)
					conflicted = true;
				else
					conflicted = false;
			}while(!conflicted);
			value = minimize(variable);
			replaceQ(variable, value);
			variable = value;
			current.update(board, getAllAttackers(), (ArrayList<Coords>) queens);
			conflicted = false;
		}
		return current;
	}

	private Tile minimize(Tile variable) {
		List<Tile> minimums = new ArrayList<Tile>();
		int min = getAttackers(variable.getX(),variable.getY());
		Random rand = new Random();
		Tile focus;
		for(int i = 0; i < board.getSize(); i++){
			focus = board.getTile(variable.getX(), i);
			if(i == variable.getY()){
				continue;
			}else if(getThreats(focus) < min){
				min = getThreats(focus);
				minimums = new ArrayList<Tile>();
				minimums.add(focus);
			} else if (getThreats(focus) == min){
				minimums.add(focus);
			}
		}
		if(!minimums.isEmpty())
			return minimums.get(Math.abs(rand.nextInt()%minimums.size()));
		else
			return variable;
	}

	private int getThreats(Tile variable) {
		int x; int y; int toReturn = 0;
		x = variable.getX();
		y = variable.getY();
		toReturn += getColAttackers(x,y);
		toReturn += getDiaAttackers(x,y);
		variable.setValue(toReturn);
		map.clear();
		return toReturn;
	}

	public long getTime() {
		return this.avgTime;
	}
}
