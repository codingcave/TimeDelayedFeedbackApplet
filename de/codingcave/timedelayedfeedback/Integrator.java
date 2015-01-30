package de.codingcave.timedelayedfeedback;

import org.apache.commons.math3.ode.*;  
import org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator;
import java.util.Timer;
import java.util.TimerTask;

public class Integrator extends TimerTask {
	private ThreeEighthesIntegrator _int;
	private Pyragas_ODE _ode;
	private Timer _timer;
	private double t = 0;
	private double dt = 0.01;
	private double[] v = new double[] { 1, 1, 1, 1, 1 };
	
	/*
	private void printV(double[] V) {
		for(int i = 0; i < V.length; i++) {
			System.out.print(V[i]);
			System.out.print("\t");
		}
		System.out.println();
	}*/
	
	public Integrator() {		
		_timer = new Timer();
		_timer.schedule(this, 0, //initial delay
		        1 * 100); //subsequent rate
		_ode = new Pyragas_ODE();
		_int = new ThreeEighthesIntegrator(.01);
		
		System.out.println("Start");
		long startTime = System.nanoTime();
				
		for(int i = 0; i < 1000; i++) {
			run();
		}
		long endTime = System.nanoTime();
		System.out.println("DONE!");
		
		long duration = (endTime - startTime);
		System.out.println("D: " + ((double)duration/1000000) + "s");
		
	}

	@Override
	public void run() { // Calculate next step
		// TODO Auto-generated method stub
		v = _int.singleStep(_ode, t, v, t + dt);
		t += dt;
		//printV(v);
		_ode.AddPoint(v);
	}
	
	
	
	
}
