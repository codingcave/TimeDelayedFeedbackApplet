package de.codingcave.timedelayedfeedback;

import java.util.*;

public class TimeList<E> implements Iterable<E> {
	int _size;
	double _dt;
	double _maxT;
	E _default;
	TimedListElement _root;
	TimedListElement _last;
	Object[] _pointer;
	double[] _pointerTimes;
	double _currentTime;
	TimeListInterpolation<E> _interpolation;
	boolean _interpolationEnabled;
	
	public TimeList(double dt, double maxT) {
		this(dt, maxT, null, null);
		_interpolationEnabled = false;
	}
	
	public TimeList(double dt, double maxT, E def) {
		this(dt, maxT, def, null);
		_interpolationEnabled = false;
	}
	
	public TimeList(double dt, double maxT, TimeListInterpolation<E> inter) {
		this(dt, maxT, null, inter);
	}
	
	public TimeList(double dt, double maxT, E def, TimeListInterpolation<E> inter) {
		this._default = def;
		this._dt = dt;
		this._maxT = maxT;
		this._size = (int)(maxT / dt);		
		this._root = new TimedListElement(def);
		this._interpolation = inter;
		this._interpolationEnabled = true;
		TimedListElement last = _root, next;
		
		this._pointer = new Object[1];
		this._pointer[0] = _root;
		
		this._pointerTimes = new double[1];
		this._pointerTimes[0] = 0;
		
		this._currentTime = 0;
		
		for(int i = 1; i < _size; i++) {
			next = new TimedListElement(def);
			last.setNext(next);
			last = next;
		}
		
		_last = last;
	}
	
	public void Add(E element) {
		TimedListElement next = new TimedListElement(element);
		_root.setPrevios(next);
		movePointer();
		_root = next;
		_last = _last._prev;
		_last.unsetNext();
		_currentTime += _dt;
	}
	
	@SuppressWarnings("unchecked")
	private void movePointer() {
		TimedListElement current;
		for(int i = 0; i < _pointer.length; i++) {
			current = ((TimedListElement)_pointer[i]);
			_pointer[i] = current._prev;
		}
	}
	
	public E getRoot() {
		return _root.getValue();
	}
	
	public E getAt(int index) {
		TimedListElement x = _root;
		for(int i = 0; i < index; i++){
			x = x._next;
		}
		return x.getValue();
	}
	
	/*
	@SuppressWarnings("unchecked")
	public E getAtTime(double time) {
		double diff = Math.abs(_currentTime - time);
		int pointerIndex = -1;
		double diff2;
		
		TimedListElement item = _root;
		for(int i = 0; i < _pointer.length; i++){
			diff2 = Math.abs(_currentTime - time);
			if(diff2 > diff) {
				diff = diff2;
				pointerIndex = i;
				item = (TimedListElement)_pointer[i];
			}
		}
		
		double pointerTime = pointerIndex == -1 ? 0 : _pointerTimes[pointerIndex]; 
		
		if(time - _currentTime + pointerTime > 0) {
			
		}
		
		
		return x.getValue();
	}
	*/
	
	@SuppressWarnings("unchecked")
	public E getPointer(int i) {
		return ((TimedListElement)_pointer[i]).getValue();
	}
	
	public void setPointer(int p, double t) {
		int index = (int)(t / _dt); 
		TimedListElement x = _root;
		for(int i = 0; i < index; i++){
			x = x._next;
		}
		_pointer[p] = x;
		_pointerTimes[p] = -(double)index * _dt;
	}
	
	public double getCurrentTime() {
		return this._currentTime;
	}
	
	public double getTimeStep() {
		return this._dt;
	}
	
	public double getSize() {
		return this._size;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			TimedListElement item = null;
			
			@Override
			public boolean hasNext() {
				return _root != null && (item == null || item.getNext() != null) ;
			}

			@Override
			public E next() {
				item = (item == null) ? _root : item._next;				 
				return item.getValue();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 				
			}			
		};
	}
	
	private class TimedListElement {
		private E _el;
		TimedListElement _prev;
		TimedListElement _next;
		
		public TimedListElement(E element) {
			this._el = element;
			_prev = null;
			_next = null;
		}
		
		public void setPrevios(TimedListElement x) {
			_prev = x;
			x._next = this;
		}
		
		public TimedListElement getPrevious() {
			return this._prev;
		}
		
		public void setNext(TimedListElement x) {
			_next = x;
			x._prev = this;
		}
		
		public TimedListElement getNext() {
			return this._next;
		}
		
		public void unsetPrevios() {
			_prev._next = null;
			_prev = null;
		}
		
		public void unsetNext() {
			_next._prev = null;
			_next = null;
		}
		
		public E getValue() {
			return _el;
		}
		
	}
	
}