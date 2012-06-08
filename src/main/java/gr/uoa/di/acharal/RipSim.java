package gr.uoa.di.acharal;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/* TODO 
 * 
 * Display real shortest paths
 * Display graphically the links
 * UI for deleting nodes and disabling links
 * 
 */

public class RipSim extends JFrame {

	PaintMenu paintMenu;
	Screen scr = new Screen(this);
	Controller controller = new Controller(this);
	
	public RipSim(String title) {
		super(title);
		Container cp = super.getContentPane();
	    cp.setLayout(new BorderLayout());
		
	    setIconImage(new ImageIcon(this.getClass().getResource("/icons/programmIcon01.png")).getImage());

	    //paintMenu = new PaintMenu(null);
	    paintMenu = new PaintMenu(controller);
	    scr.addMouseListener(controller);
	    scr.addMouseMotionListener(controller);
		cp.add(paintMenu);
	    cp.add(paintMenu, BorderLayout.WEST);

		cp.add(scr, BorderLayout.EAST);

	}

	public static void main(String[] args) {
		JFrame frame = new RipSim("RIP Simulator");
		frame.setSize(500,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
