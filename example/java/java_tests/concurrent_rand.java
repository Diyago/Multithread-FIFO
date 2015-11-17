
import java.sql.Time;
import java.util.Random;


public class concurrent_rand {

	public static long runTest(int numOfThreads) {

		int numberOfElem = 1000000;
		Random rand = new Random(2661656);
		final int[] keys = new int[numberOfElem];
		Time start, end;
		int key;
		for(int i = 0; i < numberOfElem; i++) {
			key = rand.nextInt(1000000000);
			keys[i] = key;
		}
		
		final hopscotch obj = new hopscotch();
		MyThread[] threads = new MyThread[numOfThreads];
		int t;
		start = new Time(System.currentTimeMillis());
		for(t = 0; t < numOfThreads; t++) {

			threads[t] = new MyThread(obj, t*(numberOfElem/numOfThreads), (t+1)*(numberOfElem/numOfThreads));
			threads[t].start();
			
			if (threads[t] == null) {
				System.out.println("ERROR; return code from new Thread()" + t + "is null");
			}
		}  
		
		for(t = 0; t < numOfThreads; t++) {
			try {
				threads[t].join();
			}
			catch (InterruptedException e) {System.out.println("ERROR;"); };
		}
		end = new Time(System.currentTimeMillis());
		long totalTime = end.getTime() - start.getTime();
		//System.out.println("total running time is " + end.getTime() + " - " + start.getTime() + " = " + totalTime + " milliseconds");
		return totalTime;
	}

	
	public static void main(String[] args) {
		int numberOfTests = 20;
		int numOfThreads = Integer.parseInt(args[0]);
		long[] results = new long[numberOfTests];

		for(int j = 0; j < numberOfTests; j++) {
			long totalTime = runTest(numOfThreads);
			results[j] = totalTime;
		}
		
		long avg = 0;
		for(int j = 0; j < numberOfTests; j++) {
			avg += results[j]/numberOfTests;
		}
		System.out.println("Average running time is " + avg);
	}
	
}