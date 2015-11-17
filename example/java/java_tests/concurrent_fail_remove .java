
import java.sql.Time;
import java.util.Random;


public class concurrent_remove {

	public static int runTest(int numOfThreads, int[] keys, hopscotch obj) {

		int numberOfElem = 1000000;
		int totalFound = 0;
		Time start, end;

		MyThread[] threads = new MyThread[numOfThreads];
		int t;
		start = new Time(System.currentTimeMillis());
		for(t = 0; t < numOfThreads; t++) {
			threads[t] = new MyThread(obj, keys, t*(numberOfElem/numOfThreads), (t+1)*(numberOfElem/numOfThreads));
			threads[t].start();
			
			if (threads[t] == null) {
				System.out.println("ERROR; return code from new Thread()" + t + "is null");
			}
		}  
		end = new Time(System.currentTimeMillis());
		long totalTime = end.getTime() - start.getTime();
		while(totalTime < 5) {
			end = new Time(System.currentTimeMillis());
			totalTime = end.getTime() - start.getTime();
		}
		
		for(t = 0; t < numOfThreads; t++) {
			threads[t].interrupt();
		}
		
		for(t = 0; t < numOfThreads; t++) {
			try {
				threads[t].join();
				totalFound += threads[t].count;
			}
			catch (InterruptedException e) {System.out.println("ERROR;"); };
		}
		
		//System.out.println("total running time is " + end.getTime() + " - " + start.getTime() + " = " + totalTime + " milliseconds");
		return totalFound;
	}
	
	public static void main(String[] args) {
		hopscotch obj = new hopscotch();
		int numberOfTests = 1;
		int numOfThreads = Integer.parseInt(args[0]);
		int numberOfElem = 1000000;
		int[] results = new int[numberOfTests];
		
		Random rand = new Random(2661656);
		final int[] keys = new int[numberOfElem];
		final int[] notKeys = new int[numberOfElem];
		int i;
		for(i = 0; i < numberOfElem; i++) {
			keys[i] = rand.nextInt(1000000);
			obj.add(keys[i], 1); 
		}
				for(i = 0; i < numberOfElem; i++) {
			notKeys[i] = rand.nextInt(1000000)+1000000;
			obj.add(notKeys[i], 1);
		}

		for(int j = 0; j < numberOfTests; j++) {
			int totalFound = runTest(numOfThreads, notKeys, obj);
			results[j] = totalFound;
		}
		
		long avg = 0;
		for(int j = 0; j < numberOfTests; j++) {
			avg += results[j]/numberOfTests;
		}
		System.out.println("Average failed removed objects is " + avg/5);
	}
	
}