import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
	Board board;
	int value;
	List<NQueen.Coords> queens;
	
	public Node(Board board, int value, ArrayList<NQueen.Coords> queens){
		this.board = board;
		this.value = value;
		this.queens = queens;
	}
	
	public Node(Node copy){
		this.board = new Board(copy.board);
		this.value = copy.value;
		this.queens = new ArrayList<NQueen.Coords>(copy.queens);
	}
	
	public void update(Board board, int value, ArrayList<NQueen.Coords> queens){
		this.board = board;
		this.value = value;
		this.queens = queens;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public String toString(){
		return (this.board.toString() + '\n' + this.value);
	}
	
	@Override
	public int compareTo(Node o) {
		return Integer.compare(this.value, o.value);
	}
}
