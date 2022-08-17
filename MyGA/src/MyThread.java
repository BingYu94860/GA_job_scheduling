import java.util.*;

public class MyThread extends MyGA implements Runnable {
	
	public Queue<Solution> communicationFoundBestSolution;
	MyThread[] communicationThread;
	Solution communicationCurrentBest;
	
	public void setcommunicationThread(MyThread[] thread) {
		
		communicationThread = thread;
	}
	
	public MyThread(double[][] cost, int[][] line, int iterationsNumber, int populationSize) {
		super(cost, line, iterationsNumber, populationSize);
		communicationFoundBestSolution = new  LinkedList<Solution>();
		communicationCurrentBest = new Solution();
		communicationCurrentBest.finshTime = Double.MAX_VALUE;
		// TODO 自動產生的建構子 Stub
	}
	
	public void run() {

		startTime = System.nanoTime();
		initialize();
		
		for (int i = 0; i < ITERATION_NUMBER; i++) {
	
			
			while(communicationFoundBestSolution.size() > 0) {
				Solution temp = communicationFoundBestSolution.poll();
				if(temp.finshTime < communicationCurrentBest.finshTime) {
					communicationCurrentBest.copy(temp);
					Immigration(temp);
					best.copy(temp);
				}
			}
			
			mutation();
			crossover();
			select();			
			
			//System.out.println(i + ": " + best.finshTime);
			
			if(best.finshTime < communicationCurrentBest.finshTime) {
				communicationCurrentBest.copy(best);
				for(int j = 0; j < communicationThread.length; j++) {
					Solution temp = new Solution();
					temp.copy(best);					
					communicationThread[j].communicationFoundBestSolution.offer(temp);
				}
			}
			 
		    //setBest();
			
			//printPopulation();
    	}
		//}
		runTime = System.nanoTime() - startTime;
		totalTime = (double) (runTime / 1000_000_000.0);
		//System.out.printf("Total Time: %.7f \n", totalTime);
		//printBestSolution();

	}
	
	long startTime, runTime;
	double totalTime;
	void printTotalTime() {
		totalTime = (double) (runTime / 1000_000_000.0);
		System.out.printf("Total Time: %.7f \n", totalTime);
	}
	public void runTest() {

		for (int k = 0; k < 100; k++) {
			startTime = System.nanoTime();
			initialize();
			for (int i = 0; i < ITERATION_NUMBER; i++) {
				mutation();
				crossover();
				select();
			}
			runTime = System.nanoTime() - startTime;
			totalTime = (double) (runTime / 1000_000_000.0);

			printBestSolution();
			setBest();
		}
	}



}
