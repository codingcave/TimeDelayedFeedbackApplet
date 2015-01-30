package de.codingcave.timedelayedfeedback;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;



public class TimeDelayedFeedbackApplet extends Applet implements Runnable {
	private static final long serialVersionUID = 1L;
	Thread thr;

	// declare Graphics objects
	Graphics g;
	Graphics f;
	Graphics h;
	// declare canvas'
	Canvas canvas;
	Canvas canvas1;
	Canvas canvas2=null;
	Color whiteColor = new Color(255,255,255);
	// connection to the core
	
	// define variables
	
	int canvas1Width = 426;
	int canvas1Height = 250;
	boolean Scale=false;
	
	
	private JLabel errorLabel = null;
	private JLabel graphheadingLabel = null;
	private Button button2 = null;
	private JTextField jxField;
	private JTextField jyField;
	private JTextField jzField;
	private JTextField xField;
	private JTextField yField;
	private JSlider lambdaSlider;
	private JSlider tauSlider;
	private Integrator integrator;
	private JSlider omegaSlider;
	private Vector<ParameterChanged> kekstoaster;
	private int border = 10;
	private int tickSize = 5;
	private int circleSize = 1;
	
	public TimeDelayedFeedbackApplet() {
		super();
	}

	public void init() {
		integrator = new Integrator();
		graphheadingLabel = new JLabel();
		graphheadingLabel.setBounds(new Rectangle(96, 27, 360, 22));
		graphheadingLabel.setText("Deine Mudda");
		errorLabel = new JLabel();
		errorLabel.setBounds(new Rectangle(362, 457, 167, 58));
		errorLabel.setText("<html><p style=\"color:red;\">Wrong input! Please <br>check your numbers.</p></html>");
		
		errorLabel.setBackground(whiteColor);
		errorLabel.setVisible(false);
		// add AWT Controls to the container
		this.setLayout(null);
		this.setSize(1001, 766);
		this.setSize(new Dimension(1001, 584));
		this.setBackground(Color.white);
		this.add(getPaintArea(), null);
		this.add(getCanvas1(), null);
		this.add(errorLabel, null);
		this.add(graphheadingLabel, null);
		this.add(getButton2(), null);
		this.add(getCanvas2(), null);
		
		JLabel lblCooleGrafik = new JLabel("Coole Grafik");
		lblCooleGrafik.setBounds(6, 55, 33, 250);
		lblCooleGrafik.setUI(new VerticalLabelUI(false));
		add(lblCooleGrafik);
		
		JLabel lblNewLabel = new JLabel("<html>&lambda;</html>");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel.setBounds(523, 326, 25, 30);
		add(lblNewLabel);
		
		JLabel lbltau = new JLabel("<html>&tau;</html>");
		lbltau.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lbltau.setBounds(523, 368, 25, 29);
		add(lbltau);
		
		lambdaSlider = new JSlider();
		lambdaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				resimulate();
			}
		});
		lambdaSlider.setBounds(536, 326, 413, 29);
		add(lambdaSlider);
		
		tauSlider = new JSlider();
		tauSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				resimulate();
			}
		});
		tauSlider.setBounds(536, 368, 413, 29);
		add(tauSlider);
		
		JLabel lblInitialwerte = new JLabel("Initialwerte");
		lblInitialwerte.setBounds(541, 451, 100, 16);
		add(lblInitialwerte);
		
		JLabel lblJx = new JLabel("J_x");
		lblJx.setBounds(551, 479, 25, 16);
		add(lblJx);
		
		JLabel lblJy = new JLabel("J_y");
		lblJy.setBounds(551, 507, 25, 16);
		add(lblJy);
		
		JLabel lblJz = new JLabel("J_z");
		lblJz.setBounds(551, 536, 25, 16);
		add(lblJz);
		
		JLabel lblX = new JLabel("x");
		lblX.setBounds(716, 479, 25, 16);
		add(lblX);
		
		JLabel lblY = new JLabel("y");
		lblY.setBounds(716, 507, 25, 16);
		add(lblY);
		
		jxField = new JTextField();
		jxField.setBounds(588, 473, 119, 28);
		add(jxField);
		jxField.setColumns(10);
		
		jyField = new JTextField();
		jyField.setColumns(10);
		jyField.setBounds(588, 499, 119, 28);
		add(jyField);
		
		jzField = new JTextField();
		jzField.setColumns(10);
		jzField.setBounds(588, 530, 119, 28);
		add(jzField);
		
		xField = new JTextField();
		xField.setColumns(10);
		xField.setBounds(726, 473, 119, 28);
		add(xField);
		
		yField = new JTextField();
		yField.setColumns(10);
		yField.setBounds(726, 506, 119, 28);
		add(yField);
		
		JLabel lblomega = new JLabel("<html>&omega;</html>");
		lblomega.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblomega.setBounds(523, 407, 25, 29);
		add(lblomega);
		
		omegaSlider = new JSlider();
		omegaSlider.setBounds(536, 407, 413, 29);
		omegaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				resimulate();
			}
		});
		add(omegaSlider);
		g = canvas.getGraphics();
		f = canvas1.getGraphics();
		h = canvas2.getGraphics();
		
	}
	
	private void paintScaledArrayToCanvas(Canvas canvas,double[] xValues, double[] yValues) throws Exception{
		if(xValues.length != yValues.length)
			throw new Exception();
		double[] extremeX = getMinimumAndMaximumOfArry(xValues);
		double[] extremeY = getMinimumAndMaximumOfArry(yValues);
		int[] numberOfTicks = {10,10};
		double[] minVals = {extremeX[0], extremeY[0]};
		double[] maxVals = {extremeX[1], extremeY[1]};
		double[] scaleFacs = paintScales(canvas, numberOfTicks, minVals, maxVals);
		paintActualPointsToCanvas(canvas, scaleFacs, xValues, yValues);
	}
	
	private void paintActualPointsToCanvas(Canvas canvas, double[] scaleFacs, double[] xVals, double[] yVals){
		int offsetX = border;
		int offsetY = canvas.getHeight()-border;
		Graphics g = canvas.getGraphics();
		for(int i=0; i<xVals.length;i++){
			g.drawOval(offsetX+(int) (xVals[i]*scaleFacs[0]), offsetY+(int) (yVals[i]*scaleFacs[1]), circleSize, circleSize);
			
		}
	}
	private double[] getMinimumAndMaximumOfArry(double[] values){
		double minVal=0.0, maxVal=0.0;
		double[] retVal = new double[2];
		for(int i=0;	i<values.length	;i++){
			if(values[i]<minVal)
				minVal=values[i];
			if(values[i]>maxVal)
				maxVal = values[i];
		}
		retVal[0]=minVal;
		retVal[1]=maxVal;
		return retVal;
	}
	
	private double[] paintScales(Canvas canvas, int[] numberOfTicks, double[] minVal, double[] maxVal) throws Exception{
		double scaleFacs[] = {-1.0,-1.0};
		if(numberOfTicks.length != 2 || minVal.length != 2 || maxVal.length != 2)
			throw new Exception();
		
		scaleFacs[0]=paintXScale(canvas, numberOfTicks[0], minVal[0], maxVal[0]);
		scaleFacs[1]=paintYScale(canvas, numberOfTicks[1], minVal[1], maxVal[1]);
		return scaleFacs;
	}
	
	private double paintXScale(Canvas canvas, int numberOfTicks, double minVal, double maxVal, boolean cutoff){
		int width = canvas.getWidth();
		double deltaX = maxVal - minVal;
		double dX = deltaX/numberOfTicks;
		width -= border;
		if(cutoff)
			dX = (double) (width/numberOfTicks) < dX ? dX : (double) (width/numberOfTicks);
		int heightPixel = canvas.getHeight() - border; // wenn skala oben erscheinen soll auch nur border
		Graphics g = canvas.getGraphics();
		//paint the line
		g.drawLine(border, heightPixel, width, heightPixel);
		
		//paint the ticks and there numbers
		int x;
		for(int i=0; i< numberOfTicks; i++){
			x =  (int) (i*dX);
			g.drawLine(x, (heightPixel - tickSize/2), x, (heightPixel + tickSize/2));
		}
		return deltaX/width;
	}
	private double paintXScale(Canvas canvas, int numberOfTicks, double minVal, double maxVal){
		return paintXScale( canvas,  numberOfTicks,  minVal,  maxVal, false);
	}
	private double paintYScale(Canvas canvas, int numberOfTicks, double minVal, double maxVal,boolean cutoff){
		int height = canvas.getHeight();
		double deltaY = maxVal - minVal;
		double dY = deltaY/numberOfTicks;
		height -= border;
		if(cutoff)
			dY = (double) (height/numberOfTicks) < dY ? dY : (double) (height/numberOfTicks);
		int widthPixel = border; // wenn skala oben erscheinen soll auch nur border
		Graphics g = canvas.getGraphics();
		//paint the line
		g.drawLine(widthPixel, border , widthPixel, height);
		
		//paint the ticks and there numbers
		int y;
		for(int i=0; i< numberOfTicks; i++){
			y= (int) (i*dY);
			g.drawLine(widthPixel - tickSize/2, y, widthPixel+tickSize/2, y);
		}
		return deltaY/height;
	}
	private double paintYScale(Canvas canvas, int numberOfTicks, double minVal, double maxVal){
		return paintYScale( canvas,  numberOfTicks,  minVal,  maxVal, false);
	}
	
	/**
	 * Event handling method for any changes on the sliderz
	 */

	private void resimulate(){
		int lambdaVal = lambdaSlider.getValue();
		int taulVal = tauSlider.getValue();
		int omegaVal = omegaSlider.getValue();
		callParamEventListeners(taulVal, omegaVal, lambdaVal);
	}
	
	/**
	 * Call all the listenerz
	 * @param tau
	 * @param omega
	 * @param lambda
	 */
	private void callParamEventListeners(int tau, int omega, int lambda){
		ParameterChangedEvent args = new ParameterChangedEvent(tau, omega, lambda);
		for(int i=0; i < kekstoaster.size(); i++)
			kekstoaster.get(i).stinkeFinger(this, args);
		
	}
	/**
	 * add another listener who wants to be informed about changes of omega, tau and lambda
	 * @param listener
	 */
	private void registerParameterListener(ParameterChanged listener){
		kekstoaster.add(listener);
	}
	/**
	 * remove a listener from the event chain
	 * @param listener
	 */
	private void removeParameterListener(ParameterChanged listener){
		kekstoaster.remove(listener);
	}
	// define top canvas
	private Canvas getPaintArea() {
		canvas = new Canvas();
		canvas.setBackground(whiteColor);
		canvas.setBounds(new Rectangle(54, 326, 426, 110));
		return canvas;
	}

	// define bottom canvas
	private Canvas getCanvas1() {
		if (canvas1 == null) {
			canvas1 = new Canvas();
			canvas1.setBounds(new Rectangle(54, 55, 426, 250));
			canvas1.setBackground(whiteColor);
		}
		return canvas1;
	}

	public void run() {
	}

	public void start() {
		if (thr == null) {
			thr = new Thread(this);
			thr.start();
		}
	}

	public void stop() {
		thr = null;
	}

	/**
	 * Checks all text fields for valid (not senseless) input
	 * @return true if everything went fine | false if not
	 */
	public boolean checktextfields() {

		return false;
	}
	/*
	
	public void paint(double y1, double y2) {
		g.clearRect(0, 0, 400, 400); // first clear
		// init variables
		double eins = 100.0;
		double factor = 0.0;
		double maxHeight;
		// scales the graph by calculating a factor
		if (eins >= y1 && eins >= y2) {
			factor = 1.0;
			maxHeight = 100.0;
		} else if (y1 >= y2) {
			factor = 100.0 / y1;
			maxHeight = y1;
		} else {
			factor = 100.0 / y2;
			maxHeight = y2;
		}

		// draw the grid
		g.setColor(Color.black);
		g.drawLine(40, 0, 40, 110);
		g.drawString("1.0", 4, 114 - (int) (100 * factor));
		if (!(maxHeight == 100.0)) {
			g.drawString(String.valueOf(maxHeight / 100), 4, 114 - (int) (maxHeight * factor));
			g.drawLine(37, 110 - (int) (maxHeight * factor), 43, 110 - (int) (maxHeight * factor));
		}
		g.drawLine(37, 110 - (int) (100 * factor), 43, 110 - (int) (100 * factor));

		// draw the 'picture'
		g.setColor(Color.blue);
		g.fillRect(60, 110 - (int) (100 * factor), 20, 100);// Y-Koord =
															// maxWert-HöhederSäule
		g.fillRect(160, 110 - (int) (y1 * factor), 20, 100);
		g.fillRect(260, 110 - (int) (y2 * factor), 20, 100);
	}

	// paints the devolution of populations depending on the generation
	public void paintGraph(boolean clear) {
		// only clear if there comes no more input
		if (clear)
			f.clearRect(0, 0, canvas1Width, canvas1Height + 10);
		// draw the curve
		

		// draw grid and threshold
		f.setColor(Color.black);
		if(!Scale){
		paintgraphscale(f);
		Scale = !Scale;
		}
		
		f.setColor(Color.cyan);
		//f.drawLine(40, canvas1Height + 4 - threshold, canvas1Width, canvas1Height + 4 - threshold);
	}

	// painting-directives of the grid for the second canvas
	public void paintgraphscale(Graphics f) {
		f.setColor(Color.black);
		int paddingLeft=40;
		f.drawLine(paddingLeft, 0, paddingLeft, canvas1Height + 10);
		
		//Skala Beschriftung
		f.drawString("1.0", 5, (canvas1Height + 4) - (int) (1.0 * (canvas1Height)) + 4);
		f.drawString("0.9", 5, (canvas1Height + 4) - (int) (0.9 * (canvas1Height)) + 4);
		f.drawString("0.8", 5, (canvas1Height + 4) - (int) (0.8 * (canvas1Height)) + 4);
		f.drawString("0.7", 5, (canvas1Height + 4) - (int) (0.7 * (canvas1Height)) + 4);
		f.drawString("0.6", 5, (canvas1Height + 4) - (int) (0.6 * (canvas1Height)) + 4);
		f.drawString("0.5", 5, (canvas1Height + 4) - (int) (0.5 * (canvas1Height)) + 4);
		f.drawString("0.4", 5, (canvas1Height + 4) - (int) (0.4 * (canvas1Height)) + 4);
		f.drawString("0.3", 5, (canvas1Height + 4) - (int) (0.3 * (canvas1Height)) + 4);
		f.drawString("0.2", 5, (canvas1Height + 4) - (int) (0.2 * (canvas1Height)) + 4);
		f.drawString("0.1", 5, (canvas1Height + 4) - (int) (0.1 * (canvas1Height)) + 4);
		f.drawString("0.0", 5, (canvas1Height + 4) - (int) (0.0 * (canvas1Height)) + 4);
		//Skala
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (1.0 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (1.0 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.9 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.9 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.8 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.8 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.7 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.7 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.6 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.6 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.5 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.5 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.4 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.4 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.3 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.3 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.2 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.2 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.1 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.1 * (canvas1Height)));
		f.drawLine(paddingLeft-3, (canvas1Height + 4) - (int) (0.0 * (canvas1Height)), paddingLeft+3, (canvas1Height + 4) - (int) (0.0 * (canvas1Height)));
		
		//Pfeile an Achse
		f.drawLine(paddingLeft-3,5, paddingLeft,0);
		f.drawLine(paddingLeft+3,5, paddingLeft,0);
	}

	// function which determines whether the input is higher than 1.0
	public boolean isHigherThan(Double d) {
		if (d >= 1.0)
			return true;
		return false;

	}

	// function which determines whether the input is a double
	public boolean isDouble(String string) {
		try {
			Double.valueOf(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	

	/**
	 * This method initializes button2
	 * 
	 * @return java.awt.Button
	 */
	private Button getButton2() {
		if (button2 == null) {
			button2 = new Button();
			button2.setBounds(new Rectangle(495, -3, 8, 439));
		}
		return button2;
	}
	public Color randomColor() {
		Random rand;
		rand = new Random();
		return (new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
	}
	/**
	 * This method initializes canvas2
	 * 
	 * @return java.awt.Canvas
	 */
	private Canvas getCanvas2() {
		if (canvas2 == null) {
			canvas2 = new Canvas();
			canvas2.setBounds(new Rectangle(523, 55, 426, 250));
			canvas2.setBackground(whiteColor);
		}
		return canvas2;
	}
	/*
	public void paintMigrationGraph(double[][] hppg){
		h.clearRect(0, 0, 450, 260);
		for (int i =0;i<hppg[0].length;i++){
			h.setColor(Color.BLUE);
			h.fillOval(40+(int)(450/hppg[0].length*i),(260 + 4) - (int) (hppg[0][i] * (260)), 4, 4);
			h.setColor(Color.ORANGE);
			h.fillOval(40+(int)(450/hppg[1].length*i),(260 + 4) - (int) (hppg[1][i] * (260)), 4, 4);
		}
		paintgraphscale(h);
		h.setColor(Color.BLACK);
		//genetic.setThreshold();
		//int threshold = (int) (genetic.getM_threshold() * 260.0);
		//double th = 1.0 * threshold / 260;
		//thresholdValueLabel.setText(String.valueOf(th));
		h.setColor(Color.cyan);
		//h.drawLine(40, 260 - threshold, 450, 260 - threshold);
	}*/
}