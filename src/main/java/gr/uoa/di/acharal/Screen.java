package gr.uoa.di.acharal;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.event.*;

public class Screen extends JPanel{
  protected ArrayList objects = new ArrayList();
  public Point start = null;
  public Point end = null;
  public Point old = null;
  public Markable marked = null;
  protected JFrame parent;

  public Screen(JFrame parent){
    super(true); // double buffering
    this.parent = parent;
    Dimension size = this.getToolkit().getScreenSize();
    this.setBackground(Color.WHITE);
    this.setSize(size);
    //this.setMinimumSize(size);
    this.setPreferredSize(size);
    this.setMaximumSize(size);
  }

  public void reset(){
    ArrayList help = (ArrayList)this.objects.clone();
    for(int i = 0; i < help.size(); ++i){
      ((Paintable)help.get(i)).delete();
    }
    objects = new ArrayList();
    start = null;
    end = null;
    old = null;
    marked = null;
  }


  public void setObjects(){
    for(int i = 0; i < this.objects.size(); ++i){
      ((Paintable)this.objects.get(i)).setScreen(this);
    }
  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);
  }

  public ArrayList getObjects(){ return this.objects;}

  public void paintChildren(Graphics g){
    super.paintChildren(g);
    this.paintObjects(g);
  }

  public Paintable getPaintable(String a){
    for(int i = 0; i < this.objects.size(); ++i){
      Paintable p = (Paintable)this.objects.get(i);
      if(p.getAddress() == a){
        return p;
      }
    }
    return null;
  }


  protected void paintObjects(Graphics g){
    Object[] olist = objects.toArray();
    for(int i = 0; i < olist.length; ++i)
      ((Paintable)olist[i]).paint(g);
  }

  public void unmark(){
    if(marked != null) marked.mark(false);
  }

  public void unmarkAll(){
    for(int i = 0; i < this.objects.size(); ++i){
      ((Markable)this.objects.get(i)).mark(false);
    }
  }

  public void insert(Paintable p){
    objects.add(p);
    this.unmark();
    marked = (Markable)p;
    marked.mark(true);
    this.update(this.getGraphics());
  }

  public void remove(Paintable p){
    int i = objects.indexOf(p);
    if(i != -1)
      this.objects.remove(i);
  }

  protected Paintable getObjectAt(Point p){
    Object[] arr = objects.toArray();
    Paintable ob;
    for(int i = arr.length-1; i >= 0; --i){
      ob = (Paintable)arr[i];
      if(ob.contains(p)) return ob;
    }
    return null;
  }

  public boolean getAddressExists(String addr){
    for(int i = 0; i < this.objects.size(); ++i)
      if(((Paintable)this.objects.get(i)).getAddress() == addr) return true;
    return false;
  }

  public final void paintLine(Color c){
    if(start == null || old == null)
      return;
    Graphics g = this.getGraphics();
    g.setColor(c);
    g.drawLine(start.x, start.y, old.x, old.y);
    this.paintObjects(g);
  }

  public final void paintRect(Color c){
    if(start == null || end == null)
      return;
    Graphics g = this.getGraphics();
    g.setColor(c);

    int x = (start.x < end.x) ? start.x : end.x;
    int y = (start.y < end.y) ? start.y : end.y;
    int height = Math.abs(start.y - end.y);
    int width = Math.abs(start.x - end.x);
    g.drawRect(x, y, width, height);

    this.paintObjects(g);
  }

  public void update(Graphics g){
    //super.update(g);
    this.paintObjects(g);
  }
}


