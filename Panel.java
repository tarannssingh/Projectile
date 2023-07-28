//*basic imports that are being used or are most likly going to be used
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList; 
import java.lang.Math;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.swing.event.ChangeEvent;
import java.lang.Math;

//*makes sure to add Action Listener to eventually allow us to compute user action on the screen
public class Panel extends JPanel implements MouseListener, ActionListener, ChangeListener{

    //*Initiallizes the size of a larger pixel that is easier to work with
    static int PIXEL_SIZE = 20;
    static int ScreenWidth; 
    static int ScreenHeight;

    //*Basic variables that are use to determine if the projectile is shoot and if cannon is moved 
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

    //*Variables used to allow for delay to function (defined later)
    int DELAY = 5;
    Timer timer;
    Random random;

    //*Variables required for projectile calculations
    static double t= 0.0; 
    static double vx;
    static double vy;
    static double hx;
    static double hy;
    static double ho;

    //*Variables for air resistance
    static double weight = 30;
    static double airResistance;
    static int direction;
    static double fraction;
    static double gravity = -32;
    static double mass;
    static double coef1;
    static double coef2;
    static double fraction2;
    static double coef3;

    //*Variable used to determine the iteration of the action performed method - used to update position of projectile via arraylists xcoords and ycoords
    static int j = 0;
    //*Used to control the iteration of j and therfore slowdown the speed of the projectile by determining how many refreshes to wait */
    static int w = 0;

    //*Used to count number of shots taken
    static int numshots = 0;

    //*Used to store the location of the projectile pathing
    static ArrayList<Integer> xcoords = new ArrayList<Integer>();
    static ArrayList<Integer> ycoords = new ArrayList<Integer>();
    
    //*tdiffs is a method to store the distance between each component of the projectile - may be useful later when it comes to drawing the projectile due to time
    static ArrayList<Integer> tdiffs = new ArrayList<Integer>();
    
    //* Initializing label outside of main method to access it from ActionPerformed
    final static JLabel wind = new JLabel("Wind: " + airResistance);

    static JSlider slider  = new JSlider(JSlider.HORIZONTAL, 1, 6, 3);

    public static void main(String[] args) throws IOException, LineUnavailableException{
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
        
        //*Adding label and slider at the top of the screen to allow user to change wight of projectile
        JLabel label = new JLabel("Weight of Projectile: ");
        Panel p = new Panel();
        p.setOpaque(true);
        slider.setSize(20, 20);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(1);
        slider.addChangeListener(p);
        p.add(label, BorderLayout.NORTH);
        p.add(slider, BorderLayout.NORTH);
        //panel.setSize(1, 1);
        frame.add(p);
        
        //* Adding wind to north of screeen
        p.add(wind, BorderLayout.NORTH);
        wind();

    }

    /* 
     *The following three methods allow for performed actions that must be redrawn using graphics to be seen
     *This is accomplished using the use of a timer and delay which is a 5 millisecond time period until which the method
     *actionPerformed() and repaint() is called and the graphics are redrawn
    */
    public Panel(){
        random = new Random();
        start();
    }
    public void start(){
        timer = new Timer(DELAY,this);
        timer.start();
    }
    //* This method is called every 5 milliseconds - it is used to update the position of the projectile using the external variable "j" which points to the location in the arraylist
    public void actionPerformed(ActionEvent e){
        //*Displaying updated wind value
        wind.setText("Wind: " + airResistance);
        
        if(shoot == true){
            //*This if statement is required to bypass the initial value of "j" which resulted in an index out of bounds error
            if(xcoords.size()>0){
                //*This if statement waits every 3 times the actionPerformed method is called to increment j, this slows down the projectiles animation */
                if(w%3==0){
                    j++;
                }
                w++;
                projx = xcoords.get(j);
                projy = ycoords.get(j);
                //*This clears all required variables and things at the end of a shoot (determined when at the end of an arraylist) to prepare for the next shoot
                if(j==xcoords.size()-1){
                    shoot = false;
                    xcoords.clear();
                    ycoords.clear();
                    tdiffs.clear();
                    t=0;
                    projx = 500;
                    projy = ScreenHeight - 300;
                    j=0;
                    w=0;
                    //*Called to randomize air resistance if required
                    wind();
                }
            }    
        }
        //* Called every 5 milliseconds to repaint graphics method
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

            //*drawing the pathing of the projectile using the arraylists
            for(int i =0; i<xcoords.size(); i++){
                g.setColor(Color.white);
                g.fillOval(xcoords.get(i),ycoords.get(i),10,10);
            }
        }
    }

    //*calculation of all relevant variables without air resistance
    public void calculations(){
        //*Finding the intersection point of the two points
        intersectionx= mousexpos;
        intersectiony= projy+4;
        //*Finding the length of the two sides of the triangle formed by the intersection point and the projectile
        adjacent = projx - intersectionx;
        opposite = mouseypos - intersectiony;
        //*Using trig to get the angle of the shot
        angle = Math.toDegrees(Math.atan(opposite/adjacent));
        //System.out.println("Angle: " + angle);
        //*Using the hypotenuse to act as the initial velocity of the shot
        hypotenuse = Math.sqrt(Math.pow(adjacent,2)+Math.pow(opposite,2));
        ivelocity = hypotenuse/1.5;
        //System.out.println("Velocity: " + ivelocity);

        //*Conducts all the calculations for projectile elements
        //*Important to note that initial height is based on difference of projectile y-location and ground level
        ho = ScreenHeight-PIXEL_SIZE-projy;
        vx = ivelocity * Math.cos(Math.toRadians(angle));
        vy = (-32 * t) + ivelocity*Math.sin(Math.toRadians(angle));
        hy = (-16*Math.pow(t,2))+(ivelocity*t*Math.sin(Math.toRadians(angle)))+ho;
        hx= ivelocity* t * Math.cos(Math.toRadians(angle));
    }

    public static void wind(){
        //* Used to randomize air resistance in either direction every 2 shots (variable direction determines sign of resistance)
        if(numshots%2==0){
            direction = (int) (Math.random()*2);
            if(direction%2==0){
                 airResistance = -1*(Math.random()*0.1);
            }
            else{
                airResistance = (Math.random()*0.1);
            }
        }
    }

    
    
    //*calculation of all relevant variables with air resistance
    public void acalculations(){

        //*Finding the intersection point of the two points
        intersectionx= mousexpos;
        intersectiony= projy+4;
        //*Finding the length of the two sides of the triangle formed by the intersection point and the projectile
        adjacent = projx - intersectionx;
        opposite = mouseypos - intersectiony;
        //*Using trig to get the angle of the shot
        angle = Math.toDegrees(Math.atan(opposite/adjacent));
        //System.out.println("Angle: " + angle);
        //*Using the hypotenuse to act as the initial velocity of the shot
        hypotenuse = Math.sqrt(Math.pow(adjacent,2)+Math.pow(opposite,2));
        ivelocity = hypotenuse/1.5;
        //System.out.println("Velocity: " + ivelocity);

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

    //* Called at the begining of every new shot to fill the array list with projetile locations
    public void alist(){
        if(projy == ScreenHeight -300){
            //*find the initial pathing of the projectile as time increases and storing it in the arraylist
            while(hy>0){
                //*Increments t differently to add more or less points to the arraylist based on the velocity of the shot
                if(ivelocity<200){
                    t+=0.1;  
                }
                else if(ivelocity<300){
                    t+=0.075;
                }
                else{
                    t+=0.05;
                }

                //*Storing values into the xcoords and ycoords arraylist
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
    public void mousePressed(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        if(shoot == false){
            getmouseinfo();
        }
        //*Checking to see if the shot is behind and lower than the ball and that ball is not already in the air
        if(mousexpos<500 && mouseypos>ScreenHeight-300 && projx==500 && projy == ScreenHeight-300 && shoot==false){
            //* Called to complete all projectile calculations 
            //! Change for air resistance or not
            //calculations();
            acalculations();
            //*Called to fill arraylists with new calculations
            alist();
            shoot = true;
            //* Variable required for wind
            numshots++;
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

    //* Used with slider to change the value of the weight of the projectile
    @Override
    public void stateChanged(ChangeEvent e) {

        int a = slider.getValue();
        if(a==1){
            weight = 20;
        }
        else if(a==2){
            weight = 30;
        }
        else if(a==3){
            weight = 40;
        }
        else if(a==4){
            weight = 50;
        }
        else if(a==5){
            weight = 60;
        }
        else{
            weight = 70;
        }
    }


}