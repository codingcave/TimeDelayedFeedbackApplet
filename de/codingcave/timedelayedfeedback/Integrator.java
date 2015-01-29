package de.codingcave.timedelayedfeedback;

import org.apache.commons.math3.ode.*;  
import org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator;

public class Integrator {
	private ThreeEighthesIntegrator _int;
	private Pyragas_ODE _ode;
	
	private void printV(double[] V) {
		for(int i = 0; i < V.length; i++) {
			System.out.print(V[i]);
			System.out.print("\t");
		}
		System.out.println();
	}
	
	public Integrator() {
		double t = 0;
		double dt = 0.01;
		double[] v = new double[] { 1, 1, 1, 1, 1 };
		
		_ode = new Pyragas_ODE();
		_int = new ThreeEighthesIntegrator(.01);
		for(int i = 0; i < 100; i++) {
			v = _int.singleStep(_ode, t, v, t + dt);
			t += dt;
			printV(v);
			_ode.AddPoint(v);
		}
	}
	
	
	
	
}
