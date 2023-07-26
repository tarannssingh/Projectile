//*basic imports that are being used or are most likly going to be used
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

//*makes sure to add Action Listener to eventually allow us to compute user action on the screen
public class Panel extends JPanel implements MouseListener, ActionListener{

    //*Initiallizes the size of a larger pixel that is easier to work with
    static int PIXEL_SIZE = 20;
    static int ScreenWidth; 
    static int ScreenHeight;

    //*Basic variables that are use dto determine if projectile is shoot and if cannon is moved 
    static boolean shoot= false;
    boolean moveRight= false;
    boolean moveLeft= false;

    //*Variables that are used to determine the position of the projectile
    static int projx;
    static int projy;

    //*Variables that are used to determine the position of the cannon
    static int wheel1;
    static int wheel2;
    static int body;
    static int shaft;

    //*Variables required to calculate the trajectory of the projectile
    static double ivelocity;
    static double angle;

    //*Captured variables that are use to determine angle and velocity
    static int mousexpos;
    static int mouseypos;

    //*The intersection point of the mouse release and projectile position
    static int intersectionx;
    static int intersectiony;
    //*The length of the opposite and adjacent sides of the triangle formed by the intersection point and the projectile
    static double opposite;
    static double adjacent;
    static double hypotenuse;

    //*Variables used to allow for ticks to function (defined later)
    int DELAY = 5;
    Timer timer;
    int ticks=0;

    //*Variables required for projectile calculations
    static double t= 0.0; 
    static double vx;
    static double vy;
    static double hx;
    static double hy;
    static double ho;

    //*Variables for air resistance
    static double weight = 64;
    static double airResistance = -0.1;
    static double fraction;
    static double gravity = -32;
    static double mass;
    static double coef1;
    static double coef2;
    static double fraction2;
    static double coef3;

    static int i = 0;



    //*Used to store the location of the projectile pathing
    ArrayList<Integer> xcoords = new ArrayList<Integer>();
    ArrayList<Integer> ycoords = new ArrayList<Integer>();
    //*tdiffs is a method to store the distance between each component of the projectile - may be useful later when it comes to drawing the projectile due to time
    ArrayList<Integer> tdiffs = new ArrayList<Integer>();
    



    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        //*creates a Frame object using Frame.java
        Frame frame = new Frame();
        //*all getting the size of the screen of a specific user/ device
        Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
        //*setting the variable ScreenWidth and ScreenHeight to the width and height of the users device
        ScreenWidth = (int) screenSize.getWidth();
        ScreenHeight = (int) screenSize.getHeight();
        //*setting our frame to the width and height of the users device 
        frame.setSize((int) screenSize.getWidth(),(int) screenSize.getHeight());
        //*This sets the initila position of the projectile to be at the bottom of the screen
        projx = 500;
        projy = ScreenHeight - 300;
        //*This implements the mouse listener to the panel allowing for the @Overide methods to be used (bottom)
        frame.addMouseListener(new Panel());
        
        
    }

    /* 
     *The following three methods allow for performed actions that must be redrawn using graphics to be seen
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
        if(shoot == true){  
            //*This part draws the animation of the ball shooting (idk any more specifics I just did some random stuff and it worked)
            ticks++;
            i++;
            //*this 2 changes the speed at which the projectile goes - eventually needs to be implemented with tdiff which is not being used as of now
            System.out.println("i: " + i);
            System.out.println("Size: "+ xcoords.size());
            if (ticks<=(i)){
                System.out.println("Getting xcoords/ycoords");
                projx = xcoords.get(i);
                projy = ycoords.get(i);
                if(i==xcoords.size()-1){
                    shoot = false;
                    xcoords.clear();
                    ycoords.clear();
                    tdiffs.clear();
                    t=0;
                    projx = 500;
                    projy = ScreenHeight - 300;
                    ticks = 0;
                }
            }  
        }
        repaint();
    }


    
    //*Graphics initillizer that is used to call methods that draw the graphics
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    //*The method that draws the actual graphics
    public void draw(Graphics g){
        //*bg
        g.setColor(new Color(173,216,230));
        g.fillRect(0,0,ScreenWidth, ScreenHeight - PIXEL_SIZE);
        //*ground
        g.setColor(new Color(0,100,0));
        g.fillRect(0,ScreenHeight-90,ScreenWidth,90); 
        
        if(shoot == true){
            //*projectile (if shot)
            g.setColor(Color.red);
            g.fillOval(projx,projy,PIXEL_SIZE,PIXEL_SIZE);
            //*seeing the intersection triangle
            g.fillOval(mousexpos,mouseypos,10,10);
            ///g.drawLine(mousexpos+5,mouseypos,mousexpos+5,0);
            ///g.drawLine(projx+5,projy+9,0,projy+9);
            //g.fillOval(mousexpos,projy+4,10,10);

          
           
            //*drawing the pathing of the projectile while it refreshes
            if(hy<0){
                //System.out.println("Size: " + xcoords.size());
                for(int i =0; i<xcoords.size(); i++){
                    g.setColor(Color.white);
                    g.fillOval(xcoords.get(i),ycoords.get(i),10,10);
                }
                
            }
        }
    }

    public void calculations(){
        //*Finding the intersection point of the two points
        intersectionx= mousexpos;
        intersectiony= projy+4;
        //*Finding the length of the two sides of the triangle formed by the intersection point and the projectile
        adjacent = projx - intersectionx;
        opposite = mouseypos - intersectiony;
        //*Using trig to get the angle of the shot
        angle = Math.toDegrees(Math.atan(opposite/adjacent));
        System.out.println("Angle: " + angle);
        //*Using the hypotenuse to act as the initial velocity of the shot
        hypotenuse = Math.sqrt(Math.pow(adjacent,2)+Math.pow(opposite,2));
        ivelocity = hypotenuse/1.5;
        System.out.println("Velocity: " + ivelocity);

        //*Conducts all the calculations for projectile elements
        //*Important to note that initial height is based on difference of projectile y-location and ground level
        ho = ScreenHeight-PIXEL_SIZE-projy;
        vx = ivelocity * Math.cos(Math.toRadians(angle));
        vy = (-32 * t) + ivelocity*Math.sin(Math.toRadians(angle));
        hy = (-16*Math.pow(t,2))+(ivelocity*t*Math.sin(Math.toRadians(angle)))+ho;
        hx= ivelocity* t * Math.cos(Math.toRadians(angle));
    }

    public void acalculations(){
        //*Finding the intersection point of the two points
        intersectionx= mousexpos;
        intersectiony= projy+4;
        //*Finding the length of the two sides of the triangle formed by the intersection point and the projectile
        adjacent = projx - intersectionx;
        opposite = mouseypos - intersectiony;
        //*Using trig to get the angle of the shot
        angle = Math.toDegrees(Math.atan(opposite/adjacent));
        System.out.println("Angle: " + angle);
        //*Using the hypotenuse to act as the initial velocity of the shot
        hypotenuse = Math.sqrt(Math.pow(adjacent,2)+Math.pow(opposite,2));
        ivelocity = hypotenuse/1.5;
        System.out.println("Velocity: " + ivelocity);

        mass = weight / Math.abs(gravity);
        fraction = airResistance / mass;
        coef1 = gravity * (1/fraction);
        coef2 = ivelocity * Math.sin(Math.toRadians(angle)) - coef1;
        vy = coef1 + coef2 * Math.pow(2.71, (t*fraction)*-1);
        ho = ScreenHeight-PIXEL_SIZE-projy;
        hy = coef1*t - (1/fraction * coef2) * Math.pow(2.71, (t*fraction)*-1) + ho + 1/fraction * coef2;

        fraction2 = (1/mass) * -(airResistance);
        coef3 = ivelocity * Math.cos(Math.toRadians(angle));
        vx = coef3 * Math.pow(2.71, (t*fraction2)*-1);
        hx = -1*(coef3 * (1/fraction2)) * Math.pow(2.71, (t*fraction2)*-1)+coef3 * (1/fraction2);
    }

    public void alist(){
        if(projy == ScreenHeight -300){
            //*find the initial pathing of the projectile as time increases and storing it in the arraylist
            while(hy>0){

                //*if initial velocity is greater than 200, more points (projectile locations) are added to the arraylist
                if(ivelocity<200){
                    t+=0.1;  
                }
                else{
                    t+=0.05;
                }
                //*storing values into the xcoords and ycoords arraylist
                //! Uncomments to work without air resistance
                //hy = (-16*Math.pow(t,2))+(ivelocity*t*Math.sin(Math.toRadians(angle)))+ho;
                //hx= ivelocity* t * Math.cos(Math.toRadians(angle));
                    
                //* With air resistance 
                //! Comment following 2 lines to work without air resistance
                hy = coef1*t - ((1/fraction * coef2) * Math.pow(2.71, (t*fraction)*-1)) + ho + 1/fraction * coef2;
                hx = -1*(coef3 * (1/fraction2)) * Math.pow(2.71, (t*fraction2)*-1)+coef3 * (1/fraction2);

                int rhy = ScreenHeight - (int) hy;
                int rhx =(int) hx;
                xcoords.add(projx+rhx);
                ycoords.add(rhy);
                    
                //* storing values in tdiff by adding x and y difference from the next point
                /* 
                for(int i =0; i<xcoords.size()-1; i++){
                    int xdiff = xcoords.get(i+1)-xcoords.get(i);
                    int ydiff = ycoords.get(i+1)-ycoords.get(i);
                    int tdiff = xdiff + ydiff;
                    tdiffs.add(tdiff);
                }
                */
            }
        }
    }

    public void getmouseinfo(){
       //*Getting the location of the mouse when it is released
        PointerInfo info = MouseInfo.getPointerInfo();
        mousexpos = (int) info.getLocation().getX()-12;
        mouseypos = (int) info.getLocation().getY()-35; 
    }

    //*The following methods are used to determine if a mouse action has been performed
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        //System.out.println("Mouse Clicked");
        //*makes shoot false to reset previous projectile
        //shoot = false;    
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        if(shoot == false){
            getmouseinfo();
        }
        //*checking to see if the shot is behind and lower than the ball and that ball is not already in the air
        if(mousexpos<500 && mouseypos>ScreenHeight-300 && projx==500 && projy == ScreenHeight-300 && shoot==false){
            //! Change for air resistance or not
            //calculations();
            acalculations();
            shoot = true;
            alist();
            ticks = 0;
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