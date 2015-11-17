
import java.lang.Thread;

public class MyThread extends Thread {
	hopscotch obj;
	int keys[];
	int start;
	int end;

	
	public MyThread(hopscotch obj, int keys[], int start, int end) {
		//System.out.println("new thread"+ this.getId());
		this.obj = obj;
		this.keys = keys;
		this.start = start;
		this.end = end;
	}
	
	
	@Override
    public void run() {
		for(int i = start; i < end; i++){
			this.obj.add(keys[i], 1); 
		}
		//this.obj.trial();
		for(int i = start; i < end; i++){
			this.obj.remove(keys[i]);
		}
		//this.obj.trial();
    }
    
}