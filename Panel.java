//basic imports that are being used or are most likly going to be used
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList; 
import java.lang.Math;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

//makes sure to add Action Listener to eventually allow us to compute user action on the screen
public class Panel extends JPanel implements MouseListener, ActionListener{

    //Initiallizes the size of a larger pixel that is easier to work with
    static int PIXEL_SIZE = 20;
    static int ScreenWidth;
    static int ScreenHeight;

    //Basic variables that are use dto determine if projectile is shoot and if cannon is moved
    static boolean shoot= false;
    boolean moveRight= false;
    boolean moveLeft= false;

    //Variables that are used to determine the position of the projectile
    static int projx;
    static int projy;

    //Variables that are used to determine the position of the cannon
    static int wheel1;
    static int wheel2;
    static int body;
    static int shaft;

    //Variables required to calculate the trajectory of the projectile
    static double ivelocity;
    static double angle;

    //Captured variables that are use to determine angle and velocity
    static int mousexpos;
    static int mouseypos;

    //The intersection point of the mouse release and projectile position
    static int intersectionx;
    static int intersectiony;
    //The length of the opposite and adjacent sides of the triangle formed by the intersection point and the projectile
    static double opposite;
    static double adjacent;
    static double hypotenuse;

    //Variables used to allow for ticks to function (defined later)
    int DELAY = 5;
    Timer timer;
    int ticks=0;


    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        //creates a Frame object using Frame.java
        Frame frame = new Frame();
        //all getting the size of the screen of a specific user/ device
        Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
        //setting the variable ScreenWidth and ScreenHeight to the width and height of the users device
        ScreenWidth = (int) screenSize.getWidth();
        ScreenHeight = (int) screenSize.getHeight();
        //setting our frame to the width and height of the users device 
        frame.setSize((int) screenSize.getWidth(),(int) screenSize.getHeight());
        //This sets the initila position of the projectile to be at the bottom of the screen
        projx = 500;
        projy = ScreenHeight - 300;
        //This implements the mouse listener to the panel allowing for the @Overide methods to be used (bottom)
        frame.addMouseListener(new Panel());
        
        
    }

    /*The following three methods allow for performed actions that must be redrawn using graphics to be seen
     *This is accomplished using the use of ticks which is a 5 millisecond time period until which the method
     *repaint() is called and the graphics are redrawn
    */
    public Panel(){
        start();
    }
    public void start(){
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void actionPerformed(ActionEvent e){
        repaint();
    }


    
    //Graphics initillizer that is used to call methods that draw the graphics
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    //The method that draws the actual graphics
    public void draw(Graphics g){
        //bg
        g.setColor(new Color(173,216,230));
        g.fillRect(0,0,ScreenWidth, ScreenHeight - PIXEL_SIZE);
        //ground
        g.setColor(new Color(0,100,0));
        g.fillRect(0,ScreenHeight-90,ScreenWidth,90); 
        
        if(shoot == true){
            //projectile (if shot)
            g.setColor(Color.red);
            g.fillOval(projx,projy,PIXEL_SIZE,PIXEL_SIZE);
            //seeing the intersection triangle
            g.fillOval(mousexpos,mouseypos,10,10);
            ///g.drawLine(mousexpos+5,mouseypos,mousexpos+5,0);
            ///g.drawLine(projx+5,projy+9,0,projy+9);
            g.fillOval(mousexpos,projy+4,10,10);
        }
    }

    //The following methods are used to determine if a mouse action has been performed
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        System.out.println("Mouse Clicked");  
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        //Getting the location of the mouse when it is released
        PointerInfo info = MouseInfo.getPointerInfo();
        mousexpos = (int) info.getLocation().getX()-12;
        mouseypos = (int) info.getLocation().getY()-35;
        //Finding the intersection point of the two points
        intersectionx= mousexpos;
        intersectiony= projy+4;
        //Finding the length of the two sides of the triangle formed by the intersection point and the projectile
        adjacent = projx - intersectionx;
        opposite = mouseypos - intersectiony;
        //Using trig to get the angle of the shot
        angle = Math.toDegrees(Math.atan(opposite/adjacent));
        System.out.println("Angle: " + angle);
        //Using the hypotenuse to act as the initial velocity of the shot
        hypotenuse = Math.sqrt(Math.pow(adjacent,2)+Math.pow(opposite,2));
        ivelocity = hypotenuse/1.5;
        //checking to see if the shot is behind and lower than the ball
        if(mousexpos<500 && mouseypos>ScreenHeight-300){
            shoot = true;
        }
        else{
            System.out.println("Invalid Shot");
        } 
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}





    
}