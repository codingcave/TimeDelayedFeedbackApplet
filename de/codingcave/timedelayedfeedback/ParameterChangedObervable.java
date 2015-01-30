package de.codingcave.timedelayedfeedback;

public interface ParameterChangedObervable {

	void registerParameterListener(ParameterChanged listener);
	void removeParameterListener(ParameterChanged listener);
}
