package edu.ucsd.dbmi.drugtarget.chemicstrc;

import java.util.ArrayList;



public class TestRunnable implements Runnable {
	
	public ArrayList<String> list;
	public int index;
	public String name;
	public TestRunnable(int index, ArrayList<String> list,String name) throws InterruptedException{
		
		this.list=list;
		this.index=index;
		this.name=name;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = index; i < index+100&i<list.size(); i++) {
			System.out.println(name+" "+list.get(i));
	
		}
		try {
			Thread.sleep(1000);
			System.out.println(name+" is going to sleep for 1000 ms ...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
