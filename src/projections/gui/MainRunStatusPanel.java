package projections.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* ********************************************************
 * MainRunStatusPanel.java
 * Chee Wai Lee - 10/29/2002
 *
 * This panel manages a non-modifiable status text
 * box informing the user of the currently selected
 * projections run.
 *
 * This is closely coupled with the MainSummaryGraphPanel
 * object.
 *
 * ********************************************************/

class MainRunStatusPanel extends JPanel 
    implements ChangeListener
{

	private JTextField statusField;
	private JLabel statusLabel;

	private int tabIndex = 0;

    public MainRunStatusPanel() {
	createLayout();
    }
 
    public void stateChanged(ChangeEvent e) {
	if (e.getSource() instanceof MainSummaryGraphPanel) {
	    MainSummaryGraphPanel pane = 
		(MainSummaryGraphPanel)e.getSource();
	    if (pane.isEmpty()) {
		setField(0, "Not Applicable");
	    } else {
		int index = pane.getSelectedIndex();
		if (index == -1) {
		    // do nothing ... bad selection.
		} else {
		    setField(index, pane.getTitleAt(index));
		}
	    }
	}
    }

    private void createLayout() {
	setBackground(Color.black);

	GridBagLayout      gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.BOTH;
	setLayout(gbl);
	
	statusField = new JTextField("Tab Index " + tabIndex + ": " + 
				     "Not Applicable");
	statusField.setEditable(false);

	statusLabel = new JLabel("Active Run: ");
	statusLabel.setForeground(Color.yellow);

	Util.gblAdd(this, statusLabel,  gbc, 0,0, 1,1, 0,0, 0,0,0,0);
	Util.gblAdd(this, statusField,  gbc, 1,0, 1,1, 1,0, 0,0,0,0);
    }

    private void setField(int index, String text) {
	statusField.setText("Tab Index " + index + ": " + text);
    }
}
