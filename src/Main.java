
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {		
		NQueen queen = new NQueen();
		Scanner sc = new Scanner(System.in);
		int n; int boardsize; int mode; int debug; 
		boolean satisfied = false;
		int satis = 0;
		int def = 0;
		while(!satisfied){
			n = 1000;
			boardsize = 8;
			debug = 0;
			prompt();
			mode = Integer.parseInt(sc.nextLine());
			promptdebug();
			debug = Integer.parseInt(sc.nextLine());
			promptDefault();
			def = Integer.parseInt(sc.nextLine());
			if(def == 1){
				promptn();
				n = Integer.parseInt(sc.nextLine());
				promptsize();
				boardsize = Integer.parseInt(sc.nextLine());
			}
			switch(mode){
			case 1:
				doSHC(queen, n, boardsize, debug);
				break;
			case 2:
				doCSP(sc, queen, n, boardsize, debug, def);
				break;
			case 3:
				if(def == 0)
					n = 50;
				doGA(sc, queen, n, boardsize, debug, def);
				break;
			default:
				satisfied = false;
				break;
			}
			promptFinished();
			satis = Integer.parseInt(sc.nextLine());
			if(satis == 0)
				satisfied = true;
			else if (satis == 1)
				satisfied = false;
		}
	}

	private static void promptDefault() {
		System.out.print("Do you want to customize this run? (0 - NO, 1 - YES): ");
	}

	private static void promptFinished() {
		System.out.print("Redo? (0 - NO, 1 - YES): ");
	}

	private static void doGA(Scanner sc, NQueen queen, int n, int boardsize, int debug, int def) {
		int popSize = 0;
		double mutafactor = 0;
		if(debug == 0)
			System.out.println("Numbers refer to the left to right row positions of queens.");
		if(boardsize >= 10)
			while(boardsize >= 10){
				System.out.println("Board size cannot be bigger than 10: ");
				boardsize = Integer.parseInt(sc.nextLine());
			}
		if(def == 1){
			promptpopSize();
			popSize = Integer.parseInt(sc.nextLine());
			promptMutafactor();
			mutafactor = Double.parseDouble(sc.nextLine());
		}
		if (popSize == 0)
			popSize = 50;
		if(mutafactor == 0.0)
			mutafactor = 0.125;
		queen.nTrials(n, boardsize, 3, popSize, mutafactor, debug);
		printTime(queen);
	}

	private static void printTime(NQueen queen) {
		System.out.println("Average time (ms): "+queen.getTime());
	}

	private static void promptMutafactor() {
		System.out.print("Mutation factor? (default = 0.0):");
	}

	private static void promptpopSize() {
		System.out.print("Population size? (default = 0): ");
	}

	private static void doCSP(Scanner sc, NQueen queen, int n, int boardsize, int debug, int def) {
		int maxsteps = 0;
		if(def == 1){
			promptmaxsteps();
			maxsteps = Integer.parseInt(sc.nextLine());
		}
		if(maxsteps == 0)
			maxsteps = 500;
		queen.nTrials(n, boardsize, 2, maxsteps, 0, debug);
		System.out.println(queen.getReport());
		printTime(queen);
	}

	private static void promptmaxsteps() {
		System.out.print("Max steps? (0 for default): ");
	}

	private static void doSHC(NQueen queen, int n, int boardsize, int debug) {
		queen.nTrials(n, boardsize, 1, 0, 0, debug);
		System.out.println(queen.getReport());
		printTime(queen);
	}
	
	private static void promptdebug() {
		System.out.print("Show completed boards? (0 - NO, 1 - YES): ");
	}

	private static void promptsize() {
		System.out.print("Size of the board?: ");
	}
	private static void promptn() {
		System.out.print("How many puzzles to solve?: ");
	}

	private static void prompt() {
		System.out.println("Enter type of search: \n"
				+ "  [1] Steepest Hill Climbing\n"
				+ "  [2] Minimum Conflicts (CSP)");
	}

}
