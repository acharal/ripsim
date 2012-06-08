package gr.uoa.di.acharal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.math.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class PaintableLink extends BiLink
                           implements Paintable, Markable {

	private boolean marked;
	Screen scr;
	final static Color onc = Color.BLACK;
	final static Color offc = Color.RED;
	private JDialog dlg;
	
	PaintableLink(PaintableRouter l1, PaintableRouter l2, Screen scr) {
		super(l1, l2);
		this.scr = scr;
	}
	
	public void paint(Graphics g) { 
		  shape(g, this.onc);
	}
	
	protected void shape(Graphics g, Color c){
		PaintableRouter first = (PaintableRouter) super.getLink1().connectedFrom();
		PaintableRouter second = (PaintableRouter) super.getLink1().connectedTo();
		Point start = first.getPosition();
		Point end = second.getPosition();
		
	    int dx = start.x - end.x;
	    int dy = start.y - end.y;
	    int len =  (int)Math.sqrt((dx*dx) + (dy*dy));

		double r = (double)first.DIAM/2.0;

	    double xdird = (double)(end.x - start.x);
	    xdird /= (double)len;

	    double ydird = (double)(end.y - start.y);
	    ydird /= (double)len;
	 
		int xdir = (int)(xdird*r);
		int ydir = (int)(ydird*r);

		Point spaint = new Point(start.x + xdir, start.y + ydir);
		Point epaint = new Point(end.x - xdir, end.y - ydir);
		g.setColor(c);
		if(super.getLink1().isEnabled()){
			g.setColor(c);
		}
		else {
			g.setColor(offc);
		}
		g.drawLine(spaint.x, spaint.y, epaint.x, epaint.y);
		//this.drawAddress(g, c);
	}
	  
	public boolean isMarked() { 
		return marked;
	}
	  
	public void setPosition(Point p) { 
		
	}

	public Point getPosition() { return null; }
	
	public void mark(boolean m) { marked = m; }

	public boolean contains(Point point) { 
		PaintableRouter first = (PaintableRouter) super.getLink1().connectedFrom();
		PaintableRouter second = (PaintableRouter) super.getLink1().connectedTo();
		Point start = first.getPosition();
		Point end = second.getPosition();
		Point p = (Point)point.clone();
		Point left = (start.x < end.x) ? start : end;
		Point right = (left.equals(start)) ? end : start;
		Point higher = (start.y < end.y) ? start : end;
		Point lower = (higher.equals(start)) ? end : start;

		double dx = (double)(right.x - left.x);
		double dy = (double)(lower.y - higher.y);

		if(dx < dy){
		      double x = p.y - left.y;
		      if(left.equals(lower))
		        p.x = (int)((double)p.x + (dx*x)/dy);
		      else
		        p.x = (int)((double)p.x - (dx*x)/dy);
		      if(p.y >= (higher.y + 10) && p.y <= (lower.y - 10) && p.x > (left.x - 3) && p.x < (left.x + 3))
		        return true;
		      else return false;
		}
		else{
		      double x = p.x - left.x;
		      if(left.equals(lower))
		        p.y = (int)((double)p.y + (dy*x)/dx);
		      else
		        p.y = (int)((double)p.y - (dy*x)/dx);
		      if(p.x >= (left.x + 10) && p.x <= (right.x - 10) && p.y > (left.y - 3) && p.y < (left.y + 3))
		            return true;
		      else return false;
		}
	}

	public void delete() { 
		
	}

	public JDialog showDetails() { 
		if (dlg == null)
			dlg = new LinkDialog(null,this);
		
		dlg.setVisible(true);
		dlg.requestFocus();
		return dlg;
	}
	
	public String getAddress() { return null; }

	public void setScreen(Screen s) { scr = s; }
	
}



class LinkDialog extends JDialog {
	
	final private PaintableLink lnk;
	final private JButton okbut = new JButton("Ok");

	LinkDialog(JFrame fr, PaintableLink lnk1) {
		super(fr, "Link details", true);
		this.lnk = lnk1;

		JCheckBox box = new JCheckBox("enable");
		box.setSelected(lnk1.isEnabled());
		
		box.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent ae){
					JCheckBox cb = (JCheckBox)ae.getSource();
					
					if (cb.isSelected())
						lnk.enable();
					else 
						lnk.disable();
					
					lnk.paint(lnk.scr.getGraphics());
				}
			});

		okbut.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent ae) {
					setVisible(false);
				}
		});
	    this.setSize(100,100);
	    this.getContentPane().setLayout(new BorderLayout());
	    this.getContentPane().add(box, BorderLayout.CENTER);
	    this.getContentPane().add(okbut, BorderLayout.SOUTH);
		
	}
	
	
}
