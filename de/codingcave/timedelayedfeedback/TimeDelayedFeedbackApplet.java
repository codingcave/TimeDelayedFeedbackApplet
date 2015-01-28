package de.codingcave.timedelayedfeedback;

import java.awt.GridLayout;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;  

import javax.swing.JApplet;  
import javax.swing.JButton;  
import javax.swing.JTextField;  

public class TimeDelayedFeedbackApplet extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6186041220032639199L;
	
	private JButton Button;  
    private JTextField Textfield;  
	public void init(){  
	//wird beim laden aufgerufen   
	  
	    setLayout(new GridLayout(2, 1));  
	  
	    Button= new JButton("test");  
	    add(Button);  
	    Textfield= new JTextField("test");  
	    add(Textfield);  
	
	    
	}
}