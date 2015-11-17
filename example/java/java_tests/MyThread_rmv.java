
import java.lang.Thread;

public class MyThread extends Thread {
	hopscotch obj;
	int keys[];
	int count;
	int start;
	int end;

	
	public MyThread(hopscotch obj, int keys[], int start, int end) {
		//System.out.println("new thread"+ this.getId());
		this.obj = obj;
		this.keys = keys;
		this.start = start;
		this.end = end;
		this.count = 0;
	}
	
	
	@Override
    public void run() {
		int i = start;
		while( i < end && !Thread.currentThread().isInterrupted()){
			if(obj.remove(keys[i]) != -1) {
				this.count++;
			}
			i++;
		}
		
    }
    
}