package projections.Tools.Timeline;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import projections.gui.Util;

/** The class that draws the top time axis on the top of the timeline window */
class AxisPanel extends JPanel 
{

	/** Desired height of the whole JPanel */
	private int totalHeight() {
		if(data.useMinimalView())
			return axispos()+largeTickHalfLength;
		else
			return 14+axispos()+largeTickHalfLength;
	}
	
	/** Distance from top to the horizontal line **/
	private int axispos() {
		if(data.useMinimalView())
			return 2 + largeTickHalfLength + textpos();
		else
			return 5 + largeTickHalfLength + textpos();
	}
	
	/** Distance from top to the bottom of the text label */
	private int axisLabelPositionY(){
		if(data.useMinimalView())
			return 0;
		else
			return 15;
	}
		
	/** Distance from top to the baseline for the timestamps */
	private int textpos() {
		if(data.useMinimalView())
			return data.axisFont.getSize()+axisLabelPositionY();
		else
			return 5+data.axisFont.getSize()+axisLabelPositionY();
	}
	
	/** The distance the small tick marks extend from the horizontal line */
	private int smallTickHalfLength = 2;

	/** The distance the large tick marks extend from the horizontal line */
	private int largeTickHalfLength = 5;

	private Data  data;

	private DecimalFormat format_= new DecimalFormat();

	protected AxisPanel(Data data)
	{
		this.data = data;
		format_.setGroupingUsed(true);
	}   


	/** Get the preferred size. The Width provided should be ignored */
	public Dimension getPreferredSize() {
		int preferredWidth = 200;
		int preferredHeight = totalHeight();
		return new Dimension(preferredWidth, preferredHeight);
	}

	/** Paint the axis in its panel */

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setFont(data.axisFont);
		FontMetrics fm = g.getFontMetrics();
		
		g.setColor(data.getBackgroundColor());
		Rectangle clipBounds = g.getClipBounds();
		g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);

		int maxx = getWidth();

		// Determine the left and right pixel coordinates where we will be drawing the timeline
		int xLeft = data.offset();
		int xRight = maxx-data.offset();
		
		// Draw horizontal line
		g.setColor(data.getForegroundColor());
		g.drawLine(xLeft, axispos(), xRight, axispos());

		
		// Draw the big tick marks
		for(int i=0;i<numBigTicks()+1;i++){
		
			double timeForTick = (startTimePretty()+i*smallTickTimeIncrement()*smallTicksPerBigTick());
			int pixelForTickX = data.timeToScreenPixel(timeForTick);

			if(pixelForTickX >= xLeft && pixelForTickX <= xRight){
				String label = format_.format(timeForTick);	
//				String label = U.humanReadableString(timeForTick);	
				g.drawLine(pixelForTickX, axispos()-largeTickHalfLength, pixelForTickX, axispos() + largeTickHalfLength);
				g.drawString(label, pixelForTickX - fm.stringWidth(label)/2, textpos());
			}
		}
				
		
		// Draw the small tick marks
		double timeForFirstBigTick = startTimePretty();
		for(int i=0;i<numSmallTicks()+1;i++){
			
			double timeForTick = timeForFirstBigTick + i*smallTickTimeIncrement();
			int pixelForTickX = data.timeToScreenPixel(timeForTick);
			if(pixelForTickX >= xLeft && pixelForTickX <= xRight){
				g.drawLine(pixelForTickX, axispos()-smallTickHalfLength, pixelForTickX, axispos()+smallTickHalfLength);			
			}
		}
				
		
		// Draw the label for the axis
		g.drawString(axisLabel(), getWidth()/2 - fm.stringWidth(axisLabel())/2, axisLabelPositionY());

	}


	/** Round the left offset to a multiple of the pretty timeIncrement() */
	private long startTimePretty() {
		return (long)(((long)data.startTime()/(long)smallTickTimeIncrement())*smallTickTimeIncrement());
	}

	private String axisLabel(){
		return "Time In Microseconds";
	}
	

	/** Number of microseconds per tickmark */
	private int smallTickTimeIncrement(){
		int actualDisplayWidth = getWidth();
		double pixelPerMicrosecond =  (double) data.lineWidth(actualDisplayWidth) / data.totalTime();
		double pixelsPerSmallTick = 5.0;
		int microsecondPerTick = Util.getBestIncrement( (int) Math.ceil(pixelsPerSmallTick / pixelPerMicrosecond ) );
		return microsecondPerTick;
	}
	
	
	/** The number of ticks we can display on the timeline in the given sized window */
	private int numSmallIntervals(){
		return (int) (data.totalTime() / smallTickTimeIncrement() + 1);
	}

	private int numSmallTicks(){
		return 1+numSmallIntervals();
	}
	
	private int numBigIntervals(){
		return numSmallIntervals() / smallTicksPerBigTick();
	}
	
	private int numBigTicks(){
		return 1 + numBigIntervals();
	}
	
	/** number of pixels per tick mark */
	private double pixelsPerTickMark(){
		return  ((double) data.lineWidth(getWidth())) / ((double)numSmallIntervals());
	}
	
	/** The number of tickmarks between the labeled big ticks */
	private int smallTicksPerBigTick() {
		return Util.getBestIncrement((int)(Math.ceil(data.maxLabelLen() / pixelsPerTickMark())));
	}
	


}
