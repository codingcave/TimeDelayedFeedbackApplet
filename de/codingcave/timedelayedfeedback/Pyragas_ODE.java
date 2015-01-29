package de.codingcave.timedelayedfeedback;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class Pyragas_ODE implements FirstOrderDifferentialEquations {
	private double lambda = 5; // MHz
	private double g0 = 1; // MHz
	private TimeList<ODE_Point> _list;
	private double tau = .02; // ms
	private double k = 8.1; // MHz 
	private double w_0 = 0.05; // MHz 
	private double w = 8; // MHz
	private int N = 100;
	
	public Pyragas_ODE() {
		ODE_Point d = new ODE_Point(0, 0, 0, 0, 0);
		_list = new TimeList<ODE_Point>(tau / 20, tau * 5, d);
		_list.setPointer(0, tau);
	}
	
	public void AddPoint(double[] x) {
		_list.Add(new ODE_Point(x[0], x[1], x[2], x[3], x[4]));
	}
	
	private double g(double x, double y) {
		double[] d_tau = _list.getPointer(0).getPoint();
		return g0 + lambda * (d_tau[0] * d_tau[0] - x * x + d_tau[1] * d_tau[1] - y * y);
	}
	
	private double sqrt2jInv() {
		return 1./Math.sqrt(2 * N);
	}

	@Override
	public void computeDerivatives(double t, double[] y, double[] dy_dt)
			throws MaxCountExceededException, DimensionMismatchException {
		
		double g_value = g(y[0], y[1]);
		double sInv = sqrt2jInv();
		
		dy_dt[0] = -k * y[0] + w * y[1];
		dy_dt[1] = -k * y[1] - w * y[0] - 2 * g_value * sInv * y[2];
		dy_dt[2] = -w_0 * y[4];
		dy_dt[3] = w_0 * y[3] - 4 * g_value * sInv * y[0] * y[4];
		dy_dt[4] = 4 * g_value * sInv * y[0] * y[3];
		
	}

	@Override
	public int getDimension() {
		// TODO Auto-generated method stub
		return 5;
	}

	public class ODE_Point {
		private double[] _p;
		
		public ODE_Point(double x, double y, double J_x, double J_y, double J_z) {
			_p = new double[5];
			_p[0] = x;
			_p[1] = y;
			_p[2] = J_x;
			_p[3] = J_y;
			_p[4] = J_z;
		}
		
		public double[] getPoint() {
			return _p;
		}
	}
}
