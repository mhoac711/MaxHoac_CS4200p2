import java.util.Scanner;

public class Main {
	public static void main(String[] args) {		
		NQueen queen = new NQueen();
		Scanner sc = new Scanner(System.in);
		int n;
		int boardsize;
		int mode;
		int debug;
		boolean solved = false;
		int end = 0;
		int def = 0;
		while(!solved){
			n = 1000;
			boardsize = 8;
			debug = 0;
			prompt();
			mode = Integer.parseInt(sc.nextLine());
			promptDebug();
			debug = Integer.parseInt(sc.nextLine());
			promptDefault();
			def = Integer.parseInt(sc.nextLine());
			if(def == 1){
				promptN();
				n = Integer.parseInt(sc.nextLine());
				promptSize();
				boardsize = Integer.parseInt(sc.nextLine());
			}
			switch(mode){
			case 1:
				doSHC(queen, n, boardsize, debug);
				break;
			case 2:
				doCSP(sc, queen, n, boardsize, debug, def);
				break;
			default:
				solved = false;
				break;
			}
			promptFinished();
			end = Integer.parseInt(sc.nextLine());
			if(end == 0)
				solved = true;
			else if (end == 1)
				solved = false;
		}
	}

	private static void promptDefault() {
		System.out.print("Do you want to customize this run? (0 - NO, 1 - YES): ");
	}

	private static void promptFinished() {
		System.out.print("Run Again?? (0 - NO, 1 - YES): ");
	}

	private static void printTime(NQueen queen) {
		System.out.println("Average time (ms): "+ queen.getTime());
	}

	/**
	 * This function will run the steepest hill climb algorithm
	 */
	private static void doSHC(NQueen queen, int n, int boardsize, int debug) {
		queen.nTrials(n, boardsize, 1, 0, 0, debug);
		System.out.println(queen.getReport());
		printTime(queen);
	}

	/**
	 * This function will run the min conflict algorithm.
	 * If customization is chosen, the user will have the additional ability to set a maximum number of steps
	 * for the trial.
	 */
	private static void doCSP(Scanner sc, NQueen queen, int n, int boardsize, int debug, int def) {
		int maxsteps = 0;
		if(def == 1){
			promptMaxSteps();
			maxsteps = Integer.parseInt(sc.nextLine());
		}
		if(maxsteps == 0) {
			maxsteps = 500; //default is set to 500
			queen.nTrials(n, boardsize, 2, maxsteps, 0, debug);
			System.out.println(queen.getReport());
			printTime(queen);
		}
	}

	private static void promptMaxSteps() {
		System.out.print("Maximum number of steps?: (0 - Default): ");
	}

	private static void promptSize() {
		System.out.print("Size of the board?: ");
	}

	private static void promptN() {
		System.out.print("How many puzzles to solve?: ");
	}

	private static void promptDebug() {
		System.out.print("Show completed boards?: (0 - NO, 1 - YES): ");
	}

	private static void prompt() {
		System.out.println("Enter type of search: \n"
				+ "1) Steepest Hill Climbing\n"
				+ "2) Minimum Conflicts (CSP)");
	}
}
