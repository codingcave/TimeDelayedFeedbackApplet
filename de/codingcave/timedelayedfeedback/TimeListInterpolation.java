package de.codingcave.timedelayedfeedback;

public interface TimeListInterpolation<E> {
	E interpolate(E x1, E x2);
	E ExtrapolateBefore(E x1, E x2, double dt, double t);
	E ExtrapolateAfter(E x1, E x2, double dt, double t);
}
