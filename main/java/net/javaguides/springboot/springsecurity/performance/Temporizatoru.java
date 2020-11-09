package net.javaguides.springboot.springsecurity.performance;




public class Temporizatoru{
	
	public Temporizatoru() {
		startTime = System.currentTimeMillis();
	}

	private long startTime = System.currentTimeMillis();

	public float getElapsedTime() {
		return System.currentTimeMillis()-startTime/1000F;
	}
	
	public void reset() {
		startTime = System.currentTimeMillis();
	}
	

	
	
	
	
	

	

}
