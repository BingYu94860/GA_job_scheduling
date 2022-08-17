import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyGA extends MyOperation {
	final double Alpha = 0.85; // >0.5
	final double Beta = 0.1; // <0.5
	int ITERATION_NUMBER;// = 50;
	int POPULATION_SIZE;// = 400;
	final int BEST_POPULATION;// = (int) (Beta * POPULATION_SIZE);
	final int BAD_POPULATION;// = (int) ((1.0 - Beta) * POPULATION_SIZE);
	ArrayList<Solution> population = new ArrayList<>();
	ArrayList<Solution> children = new ArrayList<>();
	private int childrenCount = 0;
	double CROSSOVER_RATE = 0.9;
	double MUTATION_RATE = 0.1;

	public MyGA(double[][] cost, int[][] line, int iterationsNumber, int populationSize) {
		super(cost, line);
		// TODO 自動產生的建構子 Stub
		ITERATION_NUMBER = iterationsNumber;
		POPULATION_SIZE = populationSize;
		BEST_POPULATION = (int) (Beta * POPULATION_SIZE);
		BAD_POPULATION = (int) ((1.0 - Beta) * POPULATION_SIZE);
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population.add(new Solution());
			children.add(new Solution());
		}
	}

	// initialize================================================================================

	void initialize() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			getRandomSolution(population.get(i));
		}
	}

	void Immigration(Solution x) {
		population.get(POPULATION_SIZE - 1).copy(x);
	}
	
	
	
	// mutation================================================================================

	void mutation() {
		int a, b;
		Solution t;
		for (int i = 0; i < POPULATION_SIZE && childrenCount < POPULATION_SIZE; i++) {
			if (random.nextDouble() <= MUTATION_RATE) {
				t = children.get(childrenCount++);
				t.copy(population.get(i));
				a = random.nextInt(V);
				b = random.nextInt(V);
				t.ss_swap(a, b);
				t.ms[a] = random.nextInt(P);
				t.ms[b] = random.nextInt(P);
				getRepairSolution(t);
			}
		}
	}// End of void mutation()

	// crossover================================================================================

	void crossover() {
		int p1, p2;
		while (childrenCount < POPULATION_SIZE - 1) {
			p1 = random.nextInt(POPULATION_SIZE);
			do {
				p2 = random.nextInt(POPULATION_SIZE);
			} while (p2 == p1);

			if (random.nextDouble() <= CROSSOVER_RATE) {				
				switch (p1 % 3) {
				case 0:
					matingPBX(population.get(p1), population.get(p2), children.get(childrenCount++),
							children.get(childrenCount++));
					break;
				case 1:
					matingPMX(population.get(p1), population.get(p2), children.get(childrenCount++),
							children.get(childrenCount++));
					break;
				case 2:
					matingLOX(population.get(p1), population.get(p2), children.get(childrenCount++),
							children.get(childrenCount++));
					break;
				default:
					break;
					
				}

			} else {
				children.get(childrenCount++).copy(population.get(p1));
				children.get(childrenCount++).copy(population.get(p2));
			}
		}
		if (childrenCount < POPULATION_SIZE)
			getRandomSolution(children.get(childrenCount));
	}// End of crossover()

	// select================================================================================

	void select() {
		int pos;
		sort(population);
		sort(children);
		for (int i = 0; i < POPULATION_SIZE; i++) {
			pos = (random.nextDouble() < Alpha) ? random.nextInt(BEST_POPULATION)
					: BEST_POPULATION + random.nextInt(BAD_POPULATION);
			population.get(i).copy(children.get(pos));
		}
		childrenCount = 0;
	}

	void sort(ArrayList<Solution> x) {
		Collections.sort(x, new Comparator<Solution>() {
			@Override
			public int compare(Solution arg0, Solution arg1) {
				if (arg0.finshTime < arg1.finshTime)
					return -1;
				else if (arg0.finshTime == arg1.finshTime)
					return 0;
				else
					return 1;
			}
		});
	}

	// print================================================================================

	void printPopulation() {
		for (int i = 0; i < population.size(); i++)
			population.get(i).printSolution();
		System.out.printf("\n");
	}

	void printChildren() {
		for (int i = 0; i < children.size(); i++)
			children.get(i).printSolution();
		System.out.printf("\n");
	}
}
