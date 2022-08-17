import java.util.ArrayList;
import java.util.Random;

public class MyEvaluator {
	Random random = new Random();
	final double nullDag = Double.MAX_VALUE;
	int V;
	int P;
	double[][] cost = null;
	int[][] line = null;
	double[][] dag;
	int[][] workIn;
	int[][] workOut;

	public MyEvaluator(double[][] cost, int[][] line) {
		V = cost.length;
		P = cost[0].length;
		this.cost = cost;
		this.line = line;
		initDag(line);
		initWorkInOut();
	}

	// best=======================================================================
	
	Solution best = new Solution();
	int updateBestCount = 0, bestCount = 0;

	// 更新最佳解
	void updateBest(Solution x) {
		updateBestCount++;
		if (x.finshTime < best.finshTime) {
			bestCount = updateBestCount;
			for (int i = 0; i < V; i++) {
				best.ss[i] = x.ss[i];
				best.ms[i] = x.ms[i];
			}
			best.finshTime = x.finshTime;
		}
	}

	// 列印最佳解
	void printBestSolution() {
		//System.out.printf("在第 %d 個解 找到最佳解\n", bestCount);
		best.printSolution();
	}

	// 重置最佳解
	void setBest() {
		best.finshTime = Double.MAX_VALUE;
		updateBestCount = 0;
		bestCount = 0;
	}
	
	// init=======================================================================

	void initDag(int[][] line) {
		dag = new double[V][V];
		for (int i = 0; i < V; i++)
			for (int j = 0; j < V; j++)
				dag[i][j] = nullDag;
		int[] t;
		for (int i = 0; i < line.length; i++) {
			t = line[i];
			dag[t[0]][t[1]] = t[2];
		}
	}// End of void initDag()

	void initWorkInOut() {
		workIn = new int[V][];
		workOut = new int[V][];
		ArrayList<Integer> wIn = new ArrayList<>();
		ArrayList<Integer> wOut = new ArrayList<>();
		for (int nowWork = 0; nowWork < V; nowWork++) {
			wIn.clear();
			wOut.clear();
			for (int work = 0; work < V; work++) {
				if (work != nowWork) {
					if (dag[work][nowWork] != nullDag) {
						wIn.add(work);
					} else if (dag[nowWork][work] != nullDag) {
						wOut.add(work);
					}
				}
			}
			workIn[nowWork] = new int[wIn.size()];
			workOut[nowWork] = new int[wOut.size()];
			for (int i = 0; i < wIn.size(); i++)
				workIn[nowWork][i] = wIn.get(i);
			for (int i = 0; i < wOut.size(); i++)
				workOut[nowWork][i] = wOut.get(i);
		}
	}// End of void initWorkInOut()

	// print=======================================================================
	void printCost() {
		System.out.printf("double cost[][] = {\n");
		for (int i = 0; i < cost.length; i++) {
			System.out.printf("{");
			for (int j = 0; j < cost[i].length; j++) {
				System.out.printf("%.1f", cost[i][j]);
				if (j != cost[i].length - 1)
					System.out.printf(", ");
			}
			System.out.printf("}%s\n", (i != cost[i].length - 1) ? ", " : "");
		}
		System.out.printf("};\n");
	}

	void printLine() {
		System.out.printf("int line[][] = {\n");
		for (int i = 0; i < line.length; i++) {
			System.out.printf("{");
			for (int j = 0; j < line[i].length; j++) {
				System.out.printf("%d", line[i][j]);
				if (j != line[i].length - 1)
					System.out.printf(", ");
			}
			System.out.printf("}%s\n", (i != line[i].length - 1) ? ", " : "");
		}
		System.out.printf("};\n");
	}

}
