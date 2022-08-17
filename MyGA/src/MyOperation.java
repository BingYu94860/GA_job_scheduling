import java.util.LinkedList;

public class MyOperation extends MyEvaluator {
	public MyOperation(double[][] cost, int[][] line) {
		super(cost, line);
		// TODO 自動產生的建構子 Stub
	}
	
	// random=====================================================================================

	// 隨機(貪心)產生隨機解
	void getRandomSolution(Solution x) {
		int nowWork, nowCPU, waitWork[] = new int[V], waitCount, waitIndex;
		double ft[] = new double[V];
		int workCount[] = new int[V];
		double pt[] = new double[P], runTime, min_ft;
		waitWork[0] = 0;
		waitCount = 1;
		for (int i = 0; i < V; i++) {
			waitIndex = random.nextInt(waitCount);
			x.ss[i] = nowWork = waitWork[waitIndex];
			waitWork[waitIndex] = waitWork[--waitCount];
			nowCPU = 0;
			min_ft = Double.MAX_VALUE;
			for (int ip = 0; ip < P; ip++) {
				runTime = 0.0;
				for (int beforeWork : workIn[nowWork]) {
					if (workCount[beforeWork] == -1) {
						runTime = Math.max(runTime,
								ft[beforeWork] + (ip == x.ms[beforeWork] ? 0.0 : dag[beforeWork][nowWork]));
					} else {
						x.finshTime = Double.MAX_VALUE;
						return;
					}
				}
				runTime = Math.max(runTime, pt[ip]) + cost[nowWork][ip];
				if (runTime < min_ft) {
					nowCPU = ip;
					min_ft = runTime;
				}
			}
			x.ms[nowWork] = nowCPU;
			ft[nowWork] = pt[nowCPU] = min_ft;
			workCount[nowWork] = -1;
			for (int nextWork : workOut[nowWork]) {
				workCount[nextWork]++;
				if (workCount[nextWork] == workIn[nextWork].length)
					waitWork[waitCount++] = nextWork;
			}
		}
		x.finshTime = ft[V - 1];
		updateBest(x);
	}// End of void getRandomSolution()

	// 隨機(半貪心)產生隨機解
	void getRandomSolution2(Solution x) {
		int nowWork, nowCPU, waitWork[] = new int[V], waitCount, waitIndex;
		double ft[] = new double[V];
		int workCount[] = new int[V];
		double pt[] = new double[P], runTime, min_ft, max_pt = 0.0;
		waitWork[0] = 0;
		waitCount = 1;
		for (int i = 0; i < V; i++) {
			waitIndex = random.nextInt(waitCount);
			x.ss[i] = nowWork = waitWork[waitIndex];
			waitWork[waitIndex] = waitWork[--waitCount];
			nowCPU = 0;
			min_ft = Double.MAX_VALUE;
			for (int ip = 0; ip < P; ip++) {
				runTime = 0.0;
				for (int beforeWork : workIn[nowWork]) {
					if (workCount[beforeWork] == -1) {
						runTime = Math.max(runTime,
								ft[beforeWork] + (ip == x.ms[beforeWork] ? 0.0 : dag[beforeWork][nowWork]));
					} else {
						x.finshTime = Double.MAX_VALUE;
						return;
					}
				}
				runTime = Math.max(runTime, pt[ip]) + cost[nowWork][ip];
				if (runTime < min_ft) {
					nowCPU = ip;
					min_ft = runTime;
					if (runTime < max_pt && random.nextBoolean())
						break;// add
				}
			}
			x.ms[nowWork] = nowCPU;
			ft[nowWork] = pt[nowCPU] = min_ft;
			max_pt = Math.max(max_pt, min_ft);// add
			workCount[nowWork] = -1;
			for (int nextWork : workOut[nowWork]) {
				workCount[nextWork]++;
				if (workCount[nextWork] == workIn[nextWork].length)
					waitWork[waitCount++] = nextWork;
			}
		}
		x.finshTime = ft[V - 1];
		updateBest(x);
	}// End of void getRandomSolution()

	// repair=====================================================================================

	// 修復解(ss 不能有重複)
	void getRepairSolution(Solution x) {
		int nowWork, nowCPU;
		int workCount[] = new int[V];
		double st[] = new double[V], pt[] = new double[P], endTime = Double.MAX_VALUE;
		for (int i = 0, j, t; i < V; i++) {
			nowWork = x.ss[i];
			if (workCount[nowWork] != workIn[nowWork].length) {
				j = i;
				do {
					nowWork = x.ss[++j];
				} while (workCount[nowWork] != workIn[nowWork].length);
				t = x.ss[i];
				x.ss[i] = x.ss[j];
				x.ss[j] = t;
			}
			nowCPU = x.ms[nowWork];
			st[nowWork] = Math.max(st[nowWork], pt[nowCPU]);
			pt[nowCPU] = endTime = st[nowWork] + cost[nowWork][nowCPU];
			for (int nextWork : workOut[nowWork]) {
				workCount[nextWork] += 1;
				st[nextWork] = Math.max(st[nextWork],
						nowCPU == x.ms[nextWork] ? endTime : endTime + dag[nowWork][nextWork]);
			}
		}
		x.finshTime = endTime;
		updateBest(x);
	}// End of void getRepairSolution()

	// mating=====================================================================================

	private int swap_int;

	void swap(int[] x, int i, int j) {
		swap_int = x[i];
		x[i] = x[j];
		x[j] = swap_int;
	}

	void matingPMX(Solution a, Solution b, Solution x, Solution y) {
		int p1, p2, temp_int, ix, iy;
		int[] xarss = new int[V], ybrss = new int[V];
		// find 2 point
		p1 = random.nextInt(V);
		do {
			p2 = random.nextInt(V);
		} while (p2 == p1);
		if (p2 < p1) {
			temp_int = p1;
			p1 = p2;
			p2 = temp_int;
		}
		// map(value->index)
		for (int i = 0; i < V; i++) {
			x.ss[i] = a.ss[i];// copy
			y.ss[i] = b.ss[i];
			xarss[a.ss[i]] = ybrss[b.ss[i]] = i;
		}
		for (int i = p1; i <= p2; i++) {
			ix = xarss[b.ss[i]];
			x.ss_swap(i, ix);// swap(x.ss, i, ix);
			swap(xarss, x.ss[i], x.ss[ix]);
			iy = ybrss[a.ss[i]];
			y.ss_swap(i, iy);// swap(y.ss, i, iy);
			swap(xarss, y.ss[i], y.ss[iy]);
			x.ms[i] = b.ms[i];
			y.ms[i] = a.ms[i];
		}
		for (int i = 0; i < p1; i++) {
			x.ms[i] = a.ms[i];
			y.ms[i] = b.ms[i];
		}
		for (int i = p2 + 1; i < V; i++) {
			x.ms[i] = a.ms[i];
			y.ms[i] = b.ms[i];
		}
		getRepairSolution(x);
		getRepairSolution(y);
	}

	void matingLOX(Solution a, Solution b, Solution x, Solution y) { // 2
		LinkedList<Integer> xss = new LinkedList<>();
		LinkedList<Integer> yss = new LinkedList<>();
		int p1, p2, temp_int;
		// find 2 point
		p1 = random.nextInt(V);
		do {
			p2 = random.nextInt(V);
		} while (p2 == p1);
		if (p2 < p1) {
			temp_int = p1;
			p1 = p2;
			p2 = temp_int;
		}
		//
		for (int i = 0; i < V; i++) {// copy
			xss.add(a.ss[i]);
			yss.add(b.ss[i]);
		}
		for (int i = p1; i <= p2; i++) { // [p1,p2]
			xss.remove(xss.indexOf(b.ss[i]));// delete
			yss.remove(yss.indexOf(a.ss[i]));
			x.ss[i] = b.ss[i];
			y.ss[i] = a.ss[i];
			x.ms[i] = b.ms[i];
			y.ms[i] = a.ms[i];
		}
		for (int i = 0; i < p1; i++) {// [0~p1)
			x.ss[i] = xss.removeFirst();
			y.ss[i] = yss.removeFirst();
			x.ms[i] = a.ms[i];
			y.ms[i] = b.ms[i];
		}
		for (int i = p2 + 1; i < V; i++) {// (p2~V)
			x.ss[i] = xss.removeFirst();
			y.ss[i] = yss.removeFirst();
			x.ms[i] = a.ms[i];
			y.ms[i] = b.ms[i];
		}
		getRepairSolution(x);
		getRepairSolution(y);
	}

	void matingPBX(Solution a, Solution b, Solution x, Solution y) { // K
		boolean[] mask = new boolean[V], xbdel = new boolean[V], yadel = new boolean[V];
		int[] xbrss = new int[V], yarss = new int[V];
		LinkedList<Integer> xass = new LinkedList<Integer>();
		LinkedList<Integer> xbss = new LinkedList<Integer>();
		LinkedList<Integer> yass = new LinkedList<Integer>();
		LinkedList<Integer> ybss = new LinkedList<Integer>();
		for (int i = 0; i < V; i++)
			xbrss[b.ss[i]] = yarss[a.ss[i]] = i;
		for (int i = 0; i < V; i++) {
			mask[i] = random.nextBoolean();
			if (mask[i]) {
				xbdel[xbrss[a.ss[i]]] = yadel[yarss[b.ss[i]]] = true;
				xass.add(a.ss[i]);
				ybss.add(b.ss[i]);
				x.ms[i] = a.ms[i];
			} else {
				x.ms[i] = b.ms[i];
			}
		}
		;
		for (int i = 0; i < V; i++) {
			if (!xbdel[i])
				xbss.add(b.ss[i]);
			if (!yadel[i])
				yass.add(a.ss[i]);
		}
		for (int i = 0; i < V; i++) {
			x.ss[i] = (mask[i]) ? xass.removeFirst() : xbss.removeFirst();
			y.ss[i] = (mask[i]) ? ybss.removeFirst() : yass.removeFirst();
		}
		getRepairSolution(x);
		getRepairSolution(y);
	}
}
