package projections.gui.Timeline;

import java.awt.*;

import javax.swing.*;

import projections.gui.MainWindow;

public class UserEventObject extends JComponent
{

	private static final long serialVersionUID = 1L;

	// Temporary hardcode. This variable will be assigned appropriate
	// meaning in future versions of Projections that support multiple
	// runs.
	static int myRun = 0;

	public static final int SINGLE=1;   // if this just marks one point in time
	public static final int PAIR=2;  // if this has a begin and end point

	public int    Type;
	public long   BeginTime;
	public long   EndTime;
	public int    UserEventID;  
	public int    CharmEventID; // for matching with end time
	private Color  color;
	public String Name;
	private Data data;
	
	private int ylocation;

	public UserEventObject(long t, int e, int event, int type) {
		Type=type;
		BeginTime=EndTime=t;
		UserEventID=e;
		CharmEventID=event;
		color=MainWindow.runObject[myRun].getUserEventColor(UserEventID);
		Name=MainWindow.runObject[myRun].getUserEventName(UserEventID);
	}

	public void setWhichTimeline(int whichTimeline){
		ylocation = whichTimeline;
	}
	
	/** Called by the layout manager to put this in the right place */
	public void setLocationAndSize(Data data, int actualDisplayWidth) {
		this.data = data;

		int leftCoord = data.timeToScreenPixel(BeginTime, actualDisplayWidth);
		int rightCoord = data.timeToScreenPixel(EndTime, actualDisplayWidth);

		if(EndTime > data.endTime())
			rightCoord = data.timeToScreenPixel(data.endTime(), actualDisplayWidth) - 5;

		if(BeginTime < data.beginTime())
			leftCoord = data.timeToScreenPixel(data.beginTime(), actualDisplayWidth) + 5;
		
		int width = rightCoord-leftCoord+1;
		
		/** The y coordinate of the top of the rectangle */
		int rectHeight = data.userEventRectHeight();
		double yTop = ((double)ylocation+0.5)*data.singleTimelineHeight() - data.barheight()/2 - rectHeight;
		
		this.setBounds( leftCoord,  
						(int)yTop,
						width, 
						rectHeight );

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if(data.showUserEvents()){
			g.setColor(color);
			g.fillRect(0, 0, getWidth(), getHeight());
		}		
		
	}

	public Color getColor() {
		return color;
	}

}

