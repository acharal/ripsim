package gr.uoa.di.acharal;

import java.awt.*;

public interface Paintable extends java.io.Serializable{
  public static final Color markc = Color.BLACK;
  public void paint(Graphics g);
  public void setPosition(Point p);
  public Point getPosition();
  public boolean contains(Point p);
  public void delete();
  public String getAddress();
  public void setScreen(Screen s);
}
