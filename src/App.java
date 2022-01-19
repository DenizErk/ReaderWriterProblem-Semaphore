
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class App {
	public static void main(String [] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		ReadWriteLock RW = new ReadWriteLock();
	
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Reader(RW));
		
	}
}


class ReadWriteLock{
	private static int readerCount = 0;
	private Semaphore S=new Semaphore(1);
	private Semaphore Q=new Semaphore(1);
	

	public void readLock() throws InterruptedException {
		Q.acquire();
		readerCount++;
		if(readerCount == 1){
			S.acquire();
		}
		Q.release();
		System.out.println(Thread.currentThread().getName() + " is reading!");
		
		
	}
	public void writeLock() throws InterruptedException{
		S.acquire();
		System.out.println(Thread.currentThread().getName() + " is writing!");
	}
	public void readUnLock() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " is ended reading!");
		Q.acquire();
		readerCount--;
		if(readerCount == 0){
			S.release();
		}
		Q.release();
	}
	public void writeUnLock() {
		System.out.println(Thread.currentThread().getName() + " is ended writing!");
		S.release();
	}
}




class Writer implements Runnable
{
   private ReadWriteLock RW_lock;
   

    public Writer(ReadWriteLock rw) {
    	RW_lock = rw;
   }

    public void run() {
      	for(int j = 0;j < 3; j++){
		  	try{
				try { // write lock
					RW_lock.writeLock();

					// 
					for(int i = 0;i < 5; i++){
						System.out.println(Thread.currentThread().getName() + " is writing! " + i);
					}
					// Critical Section

					//

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  	}
		  	finally{ // write unlock
				RW_lock.writeUnLock();
		  	} 
      	}
   	}
}



class Reader implements Runnable
{
   private ReadWriteLock RW_lock;
   
   	public Reader(ReadWriteLock rw) {
    	RW_lock = rw;
   	}
    public void run() {
		for(int j = 0;j < 3; j++){ 	    	  
    	  	try{
				try { // read lock
					RW_lock.readLock();
				
					// 
					for(int i = 0;i < 5; i++){
						System.out.println(Thread.currentThread().getName() + " is reading! " + i);
					}
					// Critical Section

					//

				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		  	}
		  	finally{ // read unlock
				try {
					RW_lock.readUnLock();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  	}
      	}
   	}
}