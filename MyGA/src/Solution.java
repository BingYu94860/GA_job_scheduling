
public class Solution{
	// 創建需要的資訊
	static int V;

	static void initV(int v) {
		V = v;
	}
	
	// 一個解
	int ss[] = new int[V];
	int ms[] = new int[V];
	double finshTime = Double.MAX_VALUE;

	// 複製解
	void copy(Solution from) {
		for (int i = 0; i < V; i++) {
			ss[i] = from.ss[i];
			ms[i] = from.ms[i];
		}
		finshTime = from.finshTime;
	}

	// 交換解
	void swap(Solution x) {
		int[] t;
		t = ss;		ss = x.ss;		x.ss = t;
		t = ms;		ms = x.ms;		x.ms = t;
		double d = finshTime;
		finshTime = x.finshTime;
		x.finshTime = d;
	}

	// 解換ss內的資料
	void ss_swap(int i, int j) {
		int t = ss[i];
		ss[i] = ss[j];
		ss[j] = t;
	}

	// 列印一個解
	void printSolution() {
		System.out.printf("%.2f | ", (finshTime != Double.MAX_VALUE) ? finshTime : -1.0);
		for (int i = 0; i < V; i++)
			System.out.printf("%d ", ss[i]);
		System.out.printf(" | ");
		for (int i = 0; i < V; i++)
			System.out.printf("%d ", ms[i]);
		System.out.printf("\n");
	}

	// 列印一個時間
	void printFinshTime() {
		System.out.printf("%.2f\n", (finshTime != Double.MAX_VALUE) ? finshTime : -1.0);
	}

}
