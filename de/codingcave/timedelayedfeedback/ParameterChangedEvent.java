package de.codingcave.timedelayedfeedback;

public class ParameterChangedEvent {
	private int _tau;
	private int _omega;
	private int _lambda;
	ParameterChangedEvent(int tau, int omega, int lambda){
		_tau=tau;
		_omega  = omega;
		_lambda = lambda;
	}
	/**
	 * @return the _tau
	 */
	public int get_tau() {
		return _tau;
	}
	/**
	 * @return the _omega
	 */
	public int get_omega() {
		return _omega;
	}
	/**
	 * @return the _lambda
	 */
	public int get_lambda() {
		return _lambda;
	}
	
}
