package edu.ucsd.dbmi.drugtarget.chemicstrc;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		
		ArrayList<String> list=new ArrayList<>();
		for (int i = 0; i < 500; i++) {
			list.add(String.valueOf(i));
		}
		
		ExecutorService pool = Executors.newFixedThreadPool(5);
		Future future0=pool.submit(new TestRunnable(0, list,"thread "+0));
		Future future1=pool.submit(new TestRunnable(100, list,"thread "+1));
		Future future2=pool.submit(new TestRunnable(200, list,"thread "+2));
		Future future3=pool.submit(new TestRunnable(300, list,"thread "+3));
		Future future4=pool.submit(new TestRunnable(400, list,"thread "+4));
		
		
		pool.shutdown(); // Disable new tasks from being submitted

		while(!pool.awaitTermination(1, TimeUnit.SECONDS))
        {
			if(future0.get()!=null){
				System.out.println("job 0 is running");
			}
			if(future1.get()!=null){
				System.out.println("job 1 is running");
			}
			if(future2.get()!=null){
				System.out.println("job 2 is running");
			}
			if(future3.get()!=null){
				System.out.println("job 3 is running");
			}
			if(future4.get()!=null){
				System.out.println("job 4 is running");
			}
        }
		
		
	}

}
