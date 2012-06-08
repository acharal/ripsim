package gr.uoa.di.acharal;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Date;

public class PaintableRouter extends Router 
							 implements Paintable, Markable {  
	final static int DIAM = 40;
	private Point pos;
	private Screen scr;
	private static Image img = new ImageIcon(Router.class.getResource("/icons/router.png")).getImage();
	private boolean marked;
	private JDialog rtd;

	PaintableRouter(String name, Point p, Screen scr) { 
		super(name);
		pos = p;
		this.scr = scr;
	}

	public void paint(Graphics g) { 
		Color old = g.getColor();
		shape(g, false);
		g.setColor(old);
	}
	
	public void shape(Graphics g, boolean delete) {
	    if(delete){
	        this.drawAddress(g, this.scr.getBackground());
	        g.setColor(this.scr.getBackground());
	        g.fillOval(pos.x - DIAM/2 -3, pos.y - DIAM/2 - 3, DIAM + 6, DIAM + 6);
	      } else{
	        g.drawImage(this.img, pos.x - DIAM/2, pos.y - DIAM/2, this.scr);
	        this.drawAddress(g, Color.GRAY);
	      }
	}
	
	private void drawAddress(Graphics g, Color c){
		g.setColor(c);
		g.setFont(new Font("SansSerif", Font.PLAIN, 20));
		g.drawString(""+super.getAddress(), pos.x-10, pos.y-DIAM/2-2);
	}
	
	public boolean isMarked() { return marked; }
	public void setPosition(Point p) { pos = p; }
	public Point getPosition() { return pos; }
	public void mark(boolean m) { marked = m ; }

	public boolean contains(Point p){
	      if(p.x > (pos.x - DIAM/2) && p.x < (pos.x + DIAM/2) && 
	         p.y > (pos.y - DIAM/2) && p.y < (pos.y + DIAM/2))
	        return true;
	      else 
	    	return false;
	}
	
	public JDialog showDetails() { 
		if (rtd == null) {
			rtd = new RTDialog(null, this);
		}

		rtd.setVisible(true);
		rtd.requestFocus();
		return rtd;
	}

	public void delete(){ }
	public void setScreen(Screen s) {} 
}


class RTDialog extends JDialog {
	
	private Router router;
	private JList routelist;
	private JLabel timeLabel = new JLabel(""+new Date());
	private JLabel routesCount = new JLabel("");
	private String[] routes;
	private javax.swing.Timer timer;

	final static int UPDATE_PERIOD = 100;
	
	public RTDialog(JFrame parent, Router r) {
	    super(parent, ""+r, false);
	    router = r;
	    routes = router.routeTable.toStringArray();
	    routelist = new JList(routes);
	    
	    setSize(400,300);
	    //routelist.setSize(200,100);
	    //this.setLayout(new BorderLayout());
	    //this.getContentPane().add(timeLabel, BorderLayout.NORTH);
	    JScrollPane sp = new JScrollPane(routelist);
	    this.getContentPane().setLayout(new BorderLayout());
	    this.getContentPane().add(routesCount, BorderLayout.SOUTH);
	    this.getContentPane().add(sp, BorderLayout.CENTER);
	    timer = new javax.swing.Timer(UPDATE_PERIOD, new ActionListener() { 
											public void actionPerformed(ActionEvent e) {
												updateDialog();
											}
	    								});
	    timer.start();
	}
	
	void updateDialog() { 
		if (!isVisible()) return;
		routes = router.routeTable.toStringArray();
		routelist.setListData(routes);
		routesCount.setText(""+routes.length+" routes");
		//timeLabel.setText(""+System.currentTimeMillis());
		//timeLabel.setText(""+new Date());
		//System.out.println("is triggered");
	}
	
}
