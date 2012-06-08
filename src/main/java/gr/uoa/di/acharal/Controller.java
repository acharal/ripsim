package gr.uoa.di.acharal;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Controller implements ActionListener, 
                                   MouseMotionListener, 
                                   MouseListener, 
                                   KeyListener {
	  protected Screen screen;
	  protected RipSim frame;

	  private static final int BUT1 = 64;  // left mouse button
	  private static final int BUT2 = 128; // middle mouse button
	  private static final int BUT3 = 256; // right mouse button
	  private int currButt = BUT1;

	  private boolean remove = false;
	  private boolean router = false;
	  private boolean connection = false;
	  protected boolean play = false;
	  protected boolean pause = false;
	  protected boolean stop = true;
	  private boolean changed = false;
	  private boolean mark = false;
	  private boolean edit = false;
	  private PaintableRouter node;
	  private int rname = 1;
	  private Paintable marked;
	  
	  public Controller(RipSim frame){
	    this.frame = frame;
	    screen = frame.scr;
	  }


	  public void setChanged(boolean c){
	    this.changed = c;
	  }

	  public boolean getChanged(){
	    return this.changed;
	  }

	  public void setScreen(Screen screen){
	    this.screen = screen;
	  }


	  public void mouseClicked(MouseEvent me){
	    //System.out.println("Mouse clicked ..."+me.getClickCount());
	    //currButt = chooseButton(me.getModifiersEx()); Button got from mousePressed
	    int x = me.getX();
	    int y = me.getY();
	    int clicks = me.getClickCount();
	    if (clicks > 2) clicks = 2;
	    if(x < PaintableRouter.DIAM/2 + 5)
	      x = PaintableRouter.DIAM/2 + 5;
	    if(y < PaintableRouter.DIAM/2 + 5)
	      y = PaintableRouter.DIAM/2 + 5;
	    if(x > screen.getSize().width - PaintableRouter.DIAM/2 - 5)
	      x = screen.getSize().width - PaintableRouter.DIAM/2 - 5;
	    if(y > screen.getSize().height - PaintableRouter.DIAM/2 - 5)
	      y = screen.getSize().height - PaintableRouter.DIAM/2 - 5;


	    if(this.router && currButt == BUT1 && this.stop){
	    	PaintableRouter rtr = new PaintableRouter("R"+rname, new Point(x, y), screen);
	    	screen.insert(rtr); // insert a new router
	    	rname++;
	    	this.setChanged(true);
	    } else if(this.connection && currButt == BUT1 && this.stop) {
	    	
	    } else if(this.mark && currButt == BUT1 && clicks == 1 && this.stop){
	      screen.unmark();
	      screen.marked = (Markable) screen.getObjectAt(me.getPoint());
	      if(screen.marked != null)
	        screen.marked.mark(true);
	      /*
	      if (screen.marked instanceof PaintableRouter) {
	    	  	System.out.println("router selected");
	      } else if (screen.marked instanceof PaintableLink) {
	    	  	System.out.println("link selected");
	      }*/
	      
	    } else if(((this.edit && currButt == BUT1) || clicks >= 2) && (this.stop || this.pause)){
	      Paintable p = screen.getObjectAt(me.getPoint());
	      if(p instanceof PaintableRouter){
	        ((PaintableRouter)p).showDetails();
	        this.setChanged(true);
	      } else if(p instanceof PaintableLink){
	    	  //((PaintableLink)p).showDetails().setLocation(x, y);
	    	  ((PaintableLink)p).showDetails();
	    	  //dlg.setLocationRelativeTo(frame);
	    	  //dlg.setLocation(x, y);
	        this.setChanged(true);
	      }
	    }
	  }

	  private int getButton(int value){
	    switch(value){
	      case InputEvent.BUTTON2_DOWN_MASK:
	        return BUT2;
	      case InputEvent.BUTTON3_DOWN_MASK:
	        return BUT3;
	      default:
	        return BUT1;
	    }
	  }

	  public void mouseEntered(MouseEvent me){}

	  public void mouseExited(MouseEvent me){}

	  public void mousePressed(MouseEvent me){
		  currButt = getButton(me.getModifiersEx()); // also for mouseClicked
		  Point tmp = me.getPoint();

		  if(!this.mark && this.stop)
		    screen.unmark();

		    if((this.connection || this.mark) && currButt == BUT1 && this.stop){
		      if(!(screen.getObjectAt(tmp) instanceof PaintableRouter)){
		        screen.start = null;
		        return;
		      }
		      node = (PaintableRouter)screen.getObjectAt(tmp);
		      screen.start = node.getPosition();
		      screen.old = node.getPosition();
		    }
		    /*if(this.remove && currButt == BUT1 && this.stop){
		      screen.unmark();
		      screen.start = me.getPoint();
		      screen.end = me.getPoint();
		    }*/
	  }

	  public void mouseReleased(MouseEvent me){
	    //System.out.println("Mouse released ...");
	    if(this.connection && currButt == BUT1 && this.stop){
	        screen.end = me.getPoint();
	        if(screen.start != null && screen.getObjectAt(screen.end) instanceof PaintableRouter){
	          //PaintableRouter first = (PaintableRouter)screen.getObjectAt(screen.start);
	        	PaintableRouter first = node;
	        	PaintableRouter second = (PaintableRouter)screen.getObjectAt(screen.end);
	        	PaintableLink lnk = new PaintableLink(first, second, screen);
	        	this.connection = true;
	        	screen.insert(lnk);
	        }
	        screen.paintLine(this.screen.getBackground());
	        screen.start = null;
	        screen.end = null;
	        screen.old = null;
	      }
	      if(this.node != null && this.mark && currButt == BUT1 && this.stop){
	        //System.out.println("after mark");
	        this.node = null;
	        screen.start = null;
	        screen.end = null;
	        screen.old = null;
	      }
	      /*
	      if(screen.start != null && this.remove && currButt == BUT1 && this.stop){
	        screen.paintRect(screen.getBackground());
	        screen.end = me.getPoint();
	        ArrayList toDelete = new ArrayList();
	        for(int i = 0; i < screen.objects.size(); ++i){
	          Object object = screen.objects.get(i);
	          //System.out.println("" + i);
	          if(object instanceof PaintableRouter){
	            PaintableRouter com = (PaintableRouter)object;
	            int x = (screen.start.x < screen.end.x) ? screen.start.x : screen.end.x;
	            int y = (screen.start.y < screen.end.y) ? screen.start.y : screen.end.y;
	            int height = Math.abs(screen.start.y - screen.end.y);
	            int width = Math.abs(screen.start.x - screen.end.x);
	            int xpos = com.getPosition().x;
	            int ypos = com.getPosition().y;
	            if((xpos > x) && (xpos < (x + width)) && (ypos > y) && (ypos < (y + height))){
	              toDelete.add(com);
	            }
	          }
	        }
	        for(int i = 0; i < toDelete.size(); ++i)
	          ((PaintableRouter)toDelete.get(i)).delete();
	        toDelete = null;
	        screen.start = null;
	        screen.end = null;
	        screen.old = null;
	        this.setChanged(true);
	      }*/
	  }

	  public void mouseDragged(MouseEvent me){
		    //System.out.println("Mouse dragged ...");
		    if(this.connection && currButt == BUT1 && this.stop){
		      if(screen.start == null)
		        return;
		      screen.paintLine(screen.getBackground());
		      screen.update(screen.getGraphics()); // paint Objects without painting Background
		      screen.old = me.getPoint();
		      screen.paintLine(Color.GRAY);
		    }
		    if(this.mark && currButt == BUT1 && this.stop){
		      if(screen.start == null)
		        return;
		      //System.out.println("move");
		      screen.unmark();
		      screen.marked = (Markable)node;
		      node.mark(false);

		      node.shape(screen.getGraphics(), true);

		      int x = me.getX();
		      int y = me.getY();
		      if(x < PaintableRouter.DIAM/2 + 5)
		        x = PaintableRouter.DIAM/2 + 5;
		      if(y < PaintableRouter.DIAM/2 + 5)
		        y = PaintableRouter.DIAM/2 + 5;
		      if(x > screen.getSize().width - PaintableRouter.DIAM/2 - 5)
		        x = screen.getSize().width - PaintableRouter.DIAM/2 - 5;
		      if(y > screen.getSize().height - PaintableRouter.DIAM/2 - 5)
		        y = screen.getSize().height - PaintableRouter.DIAM/2 - 5;

		      node.setPosition(new Point(x, y));

		      node.shape(screen.getGraphics(), false);
		      node.mark(true);
		      //screen.repaint();
		      screen.parent.repaint();
		      this.setChanged(true);
		    }
		    /*
		    if(this.remove && currButt == BUT1 && this.stop){
		      if(screen.start == null)
		        return;
		      screen.unmark();
		      screen.paintRect(screen.getBackground());
		      screen.end = me.getPoint();
		      screen.paintRect(Color.GRAY);
		    }*/
	  }

	  public void mouseMoved(MouseEvent me){}

	  public void keyPressed(KeyEvent ke){
	    //System.out.println("Key pressed ...");
	    int code = ke.getKeyCode();
	    if(code == KeyEvent.VK_DELETE && screen.marked != null){
//	      screen.marked.delete();
	      this.screen.marked = null;
	      this.setChanged(true);
	    }
	  }

	  public void keyReleased(KeyEvent ke){}

	  public void keyTyped(KeyEvent ke){}

	  public void actionPerformed(ActionEvent ae){
	    //System.out.println("AP");
	    String cmd = ae.getActionCommand();
	    Cursor cu;
	    if(cmd.equals("Mark")){
	      //System.out.println("Mark");
	      cu = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(this.getClass().getResource("/icons/MarkerToolCursor.png")).getImage(), new Point(0,0), "myCursor");
	      this.screen.setCursor(cu);
	      mark = true;
	      remove = router = connection = false;
	    }else if(cmd.equals("Remove")){
	      //System.out.println("Remove");
	      cu = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(this.getClass().getResource("/icons/RemoveToolCursor.png")).getImage(), new Point(0,0), "myCursor");
	      this.screen.setCursor(cu);
	      this.remove = true;
	      router = connection = mark = false;
	    } else if(cmd.equals("Router")){
	      //System.out.println("Router");
	      cu = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(this.getClass().getResource("/icons/RouterToolCursor.png")).getImage(), new Point(0,0), "myCursor");
	      this.screen.setCursor(cu);
	      this.router = true;
	      remove = connection  = mark = false;
	    }else if(cmd.equals("Connection")){
	      //System.out.println("Connection");
	      cu = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(this.getClass().getResource("/icons/ConnectionToolCursor.png")).getImage(), new Point(0,0), "myCursor");
	      this.screen.setCursor(cu);
	      this.connection = true;
	      remove = router  = mark = false;
	    }
	}
}
