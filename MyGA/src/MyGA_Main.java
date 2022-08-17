import java.util.concurrent.TimeUnit;

public class MyGA_Main {
	public static void main(String args[]) {		
		
		//設定
		final int THREAD_N = 4;
		final int ITERATION_NUMBER = 55;
		final int POPULATION_SIZE = 610;
		//多執行緒
		final MyThread ga_Thread[] = new MyThread[THREAD_N];
		Thread thread[] = new Thread[THREAD_N];
		//讀檔
		Problem problem = new Problem();
		problem.readFromFile("n4_00.dag");
		Solution.initV(problem.cost.length);
		
		for (int i = 0; i < THREAD_N; i++) {
			ga_Thread[i] = new MyThread(problem.cost, problem.line, ITERATION_NUMBER, POPULATION_SIZE);
		}
		
		//初始化 多執行緒
		for (int i = 0; i < THREAD_N; i++) {
			MyThread[] communicationThreadClass = new MyThread[THREAD_N - 1];
			int temp = 0;			
			for(int j = 0; j < THREAD_N; j++)
				if(i != j)
					communicationThreadClass[temp++] = ga_Thread[j];
			
			ga_Thread[i].setcommunicationThread(communicationThreadClass);
			
			thread[i] = new Thread(ga_Thread[i], "T" + (i + 1));
		}
		

        
        try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		//開始
		for (int i = 0; i < THREAD_N; i++) {
			thread[i].start();
		}
		//等待結束
		try {
			for (int i = 0; i < THREAD_N; i++)
				thread[i].join();
		} catch (Exception e) {
		}
		//列印訊息
		/*
		for (int i = 0; i < THREAD_N; i++) {
			ga_Thread[i].printTotalTime();
			ga_Thread[i].printBestSolution();
		}
		*/
		Solution result = new Solution();
		result.finshTime = Double.MAX_VALUE;
		double time = 0.0;
		for (int i = 0; i < THREAD_N; i++) {
			if(ga_Thread[i].best.finshTime < result.finshTime) {
				result.copy(ga_Thread[i].best);
			}
			if(time < ga_Thread[i].totalTime) {
				time = ga_Thread[i].totalTime;
			}
		}

		System.out.printf("Execution Time: %.4f ms\n", time);
		result.printSolution();
	}
	
}
