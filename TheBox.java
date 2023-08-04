import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TheBox extends Rectangle{
    static int x = 0;
    static int y = 0;
    static double myY = 0;
    static int boxSize = 40;
    static double yVelocity = 0.0;
    static double friction = -0.5;
    static int speed = 0;

    public TheBox()
    {
        // Becuase this is a subclass of rectangle super class, we can call the super constructor here
        super (x, y, boxSize, boxSize);
    }


    public void paint(Graphics g)
    {
        // We cast grpahics g to graphics2D
        Graphics g2D = g;
        g2D.setColor(new Color (212, 201, 178));
        g2D.fillRect(x, y, width, height);
        //g2D.setStroke(new BasicStroke(3));
        g2D.setColor(new Color (256, 256, 256));
        g2D.drawRect(x, y, width, height);
        
    }

    public void move()
    {
        setYVelocity();
        myY = y + yVelocity;
        yVelocity += friction;
        y = (int) myY;
    }

    public void setYVelocity()
    {
        if (x == 0 || x == Panel.ScreenWidth - boxSize)
        {
        yVelocity *= -1;
        friction *= -1;
        }
    }


}
 

