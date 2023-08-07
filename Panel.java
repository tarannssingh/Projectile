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
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList; 
import java.lang.Math;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.swing.event.ChangeEvent;
import java.lang.Math;

//*makes sure to add Action Listener to eventually allow us to compute user action on the screen
public class Panel extends JPanel implements MouseListener, ActionListener, ChangeListener{

        //*************************************************************************************************************************************VARIABLES************************************************************************************************//
    //*Initiallizes the size of a larger pixel that is easier to work with
    static int PIXEL_SIZE = 20;
    static int ScreenWidth; 
    static int ScreenHeight;

    //*Basic variables that are use to determine if the projectile is shoot and if cannon is moved 
    static boolean shoot= false;
    static boolean moveRight= false;
    static boolean moveLeft= false;

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
    static double airResistance = 0;
    static int direction;
    static double fraction;
    static double gravity;
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

    //*Used during planet randomization
    static int world;
    static boolean earth = false;
    static boolean moon = false;
    static boolean mars = false;
    static boolean jupiter = false;

    static boolean transformed = false;

    //* Global decleration of the trail check and its other components
    static JCheckBox trailCheck = new JCheckBox("Static Trail");
    static int trailType = 0;

    //* Angle used for the animation of the launcher
    static int pangle = -35;
    //*Width and Height used for the graphics positions to enable them to be changed
    static double counterwidth;
    static double counterheight;

    static double launcherwidth;
    static double launcherheight;

    static int aniwidth;
    static int aniheight;

    //* For moving launcher left and right
    static Action leftAction;
    static Action rightAction;

        //*************************************************************************************************************************************MAIN METHOD************************************************************************************************//
    //*Main method that is used to run the program
    public static void main(String[] args) throws IOException, LineUnavailableException{
        //*creates a Frame object using Frame.java
        Frame frame = new Frame();
        //*all getting the size of the screen of a specific user/ device
        Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
        //*setting the variable ScreenWidth and ScreenHeight to the width and height of the users device
        ScreenWidth = (int) screenSize.getWidth();
        ScreenHeight = (int) screenSize.getHeight();

        //*setting the variables for graphics positioning
        counterwidth = launcherwidth = screenSize.getWidth();
        counterheight = launcherheight = screenSize.getHeight();
        aniwidth = (int) screenSize.getWidth();
        aniheight = (int) screenSize.getHeight();

        //*Adjusts initial positioning of launcher
        aniheight += 70;
        counterheight = launcherheight += 70;

        //*setting our frame to the width and height of the users device 
        frame.setSize((int) screenSize.getWidth(),(int) screenSize.getHeight());
        //*This sets the initial position of the projectile to be at the bottom of the screen
        projx = aniwidth/7+55;
        projy = aniheight - 400;
        //*This implements the mouse listener to the panel allowing for the @Overide methods to be used (bottom)
        frame.addMouseListener(new Panel());
        
        //*Adding label and slider at the top of the screen to allow user to change wight of projectile
        JLabel label = new JLabel("Weight of Projectile: ");
        label.setForeground(new Color(255,255,255));
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

        //*Setting up key bindings to react to panel actions - uses a & d keys to listen for actions

        leftAction  = p.new LeftAction();
        rightAction = p.new RightAction();
        
        p.getInputMap().put(KeyStroke.getKeyStroke('a'), "leftAction");
        p.getActionMap().put("leftAction", leftAction);

        p.getInputMap().put(KeyStroke.getKeyStroke('d'), "rightAction");
        p.getActionMap().put("rightAction", rightAction);

        //*Prevents slider bug with moving launcher
        slider.getInputMap().put(KeyStroke.getKeyStroke('a'), "leftAction");
        slider.getActionMap().put("leftAction", leftAction);
      
        slider.getInputMap().put(KeyStroke.getKeyStroke('d'), "rightAction");
        slider.getActionMap().put("rightAction", rightAction);
        
        //* Adding wind to north of screeen
        p.add(wind, BorderLayout.NORTH);
        wind();

        planet();
        if(earth == true){
            JLabel planet = new JLabel("|    Planet: Earth    |");
            planet.setForeground(new Color(255,255,255));
            p.add(planet, BorderLayout.NORTH);
        }
        else if(moon == true){
            JLabel planet = new JLabel("|    Planet: Moon    |");
            planet.setForeground(new Color(255,255,255));
            p.add(planet, BorderLayout.NORTH);
        }
        else if(mars == true){
            JLabel planet = new JLabel("|    Planet: Mars    |");
            planet.setForeground(new Color(255,255,255));
            p.add(planet, BorderLayout.NORTH);
        }
        else{
            JLabel planet = new JLabel("|    Planet: Jupiter    |");
            planet.setForeground(new Color(255,255,255));
            p.add(planet, BorderLayout.NORTH);
        }

        //* This is for the trail on/off check box
        trailCheck.setFocusable(false);
        trailCheck.setOpaque(false);
        trailCheck.setForeground(Color.white);
        p.add(trailCheck);
        // checkBox.setFont(new Font("Consolas", Font.PLAIN, 35));

    }

        //*************************************************************************************************************************************RECALLED METHODS************************************************************************************************//
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
        wind.setForeground(new Color(255,255,255));

        //*Used to move graphics if moved left or right
        if(moveLeft == true && shoot == false){
            aniwidth -= 20;
            counterwidth -= 20;
            launcherwidth -= 20;
            projx = aniwidth/7+55;
            moveLeft = false;
        }
        else{
            projx = aniwidth/7+55;
        }
        if(moveRight == true && shoot == false){
            aniwidth += 20;
            counterwidth += 20;
            launcherwidth += 20;
            projx = aniwidth/7+55;
            moveRight = false;
        }
        else{
            projx = aniwidth/7+55;
        }
        
        //*Makes sure to check that launcher is at end of its animation
        if(shoot == true && pangle == 61){
            //*This draws multiple (in this case 3) iterations of the arraylists for everytime that action performed is called - this speeds up the shot 
            for(int y =0; y<4; y++){
                //*This if statement is required to bypass the initial value of "j" which resulted in an index out of bounds error
                if(xcoords.size()>0){
                    //*This if statement waits every 1 times the actionPerformed method is called to increment j, this slows down the projectiles animation if requred (not doing anthing rn)
                    if(w%1==0){
                        j+=1;
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
                        j=0;
                        projx = aniwidth/8+100;
                        projy = aniheight - 400;
                        w=0;
                        transformed = false;
                        Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
                        ScreenWidth = aniwidth = (int) screenSize.getWidth();
                        ScreenHeight = aniheight = (int) screenSize.getHeight();
                        aniheight += 70;
                        pangle = -35;
                        counterwidth = launcherwidth = ScreenWidth;
                        counterheight = launcherheight = ScreenHeight+70;
                        //*Called to randomize air resistance if required
                        wind();
                    }
                }
            }    
        }

        //*Animation of launcher shooting by changing positions of the launcher and counterweight
        if(shoot == true && pangle<60){
            //changing angle and adjusting counterweight/launcher
            pangle+=2;
            launcherwidth-=2;
            counterwidth -= 0.66667*2;
            launcherheight -= 0.3333*2;
            counterheight  += 0.4334*2;
        }

        //* This is for the trail changing feature
        int trailNumber = 0;
            // This is a way to check if the checkbox is selected
            if (trailCheck.isSelected()){
                trailNumber = 0;
            }
            else{
                trailNumber = 1;
            }
            if (trailNumber == 1){ 
                if (shoot == false){
                    trailType = 1;
                }
            }
            if (trailNumber == 0){
                if (shoot == false){
                    trailType = 0;
                }
            }

        //* Called every 5 milliseconds to repaint graphics method
        repaint();
    }

        //*************************************************************************************************************************************GRAPHICS METHODS************************************************************************************************//
    //*Graphics initillizer that is used to call methods that draw the graphics
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    //*The method that draws the actual graphics
    public void draw(Graphics g){
        
        //*Changes size of screen if projectile it goes out of frame - Makes sure to check that launcher is at end of its animation
        if(shoot == true && pangle==61){
            if(xcoords.get(xcoords.size()-1)>ScreenWidth){
                Graphics2D g2 = (Graphics2D) g;
                AffineTransform at = new AffineTransform();
                at.scale(0.5, 0.5);
                g2.setTransform(at);
                g2.translate(0,ScreenHeight*2-110);
                transformed = true;
            }
        }

        //* Drawing background according to planet and adjusting for transformation (if ball goes off screen)
        if(moon == true){
            if(transformed == true){
                g.setColor(new Color(0,0,0));
                g.fillRect(0,-(ScreenHeight*2),ScreenWidth*3, (ScreenHeight - PIXEL_SIZE)*3);
                g.setColor(new Color(218,217,215));
                g.fillRect(0,ScreenHeight-90,ScreenWidth*3,90);
            }
            else{
                //*bg
                g.setColor(new Color(0,0,0));
                g.fillRect(0,0,ScreenWidth, ScreenHeight - PIXEL_SIZE);
                //*ground
                g.setColor(new Color(218,217,215));
                g.fillRect(0,ScreenHeight-90,ScreenWidth,90);
            }
        }
        else if(earth == true){
             if(transformed == true){
                g.setColor(new Color(173,216,230));
                g.fillRect(0,-(ScreenHeight*2),ScreenWidth*3, (ScreenHeight - PIXEL_SIZE)*3);
                g.setColor(new Color(0,100,0));
                g.fillRect(0,ScreenHeight-90,ScreenWidth*3,90);
            }
            else{
                //*bg
                g.setColor(new Color(173,216,230));
                g.fillRect(0,0,ScreenWidth, ScreenHeight - PIXEL_SIZE);
                //*ground
                g.setColor(new Color(0,100,0));
                g.fillRect(0,ScreenHeight-90,ScreenWidth,90);
            }
        }
        else if(mars == true){
            if(transformed == true){
                //*bg
                g.setColor(new Color(0,0,0));
                g.fillRect(0,-(ScreenHeight*2),ScreenWidth*3, (ScreenHeight - PIXEL_SIZE)*3);
                //*ground
                g.setColor(new Color(193,68,14));
                g.fillRect(0,ScreenHeight-90,ScreenWidth*3,90);
            }
            else{
                //*bg
                g.setColor(new Color(0,0,0));
                g.fillRect(0,0,ScreenWidth, ScreenHeight - PIXEL_SIZE);
                //*ground
                g.setColor(new Color(193,68,14));
                g.fillRect(0,ScreenHeight-90,ScreenWidth,90);
            }
        }
        else{
            if(transformed==true){
                //*bg
                g.setColor(new Color(201,144,57));
                g.fillRect(0,-(ScreenHeight*2),ScreenWidth*3, (ScreenHeight - PIXEL_SIZE)*3);
                //*ground
                g.setColor(new Color(165,145,134));
                g.fillRect(0,ScreenHeight-90,ScreenWidth*3,90);
            }
            else{
                //*bg
                g.setColor(new Color(201,144,57));
                g.fillRect(0,0,ScreenWidth, ScreenHeight - PIXEL_SIZE);
                //*ground
                g.setColor(new Color(165,145,134));
                g.fillRect(0,ScreenHeight-90,ScreenWidth,90);
            }
        }

        //*Body
        g.setColor(new Color(68,48,34));
        g.fillRect(aniwidth/7+50,aniheight-190,100,10);
        g.fillRect(aniwidth/7+68,aniheight-195,65,3);

        //*Wheel 1
        //Outer Brown Circle
        g.setColor(new Color(133,94,66));
        g.fillOval(aniwidth/7+30,aniheight-200,40,40);
        g.setColor(new Color(0,0,0));
        //Inner Black Circle
        g.fillOval(aniwidth/7+35,aniheight-195,30,30);
        g.setColor(new Color(133,94,66));
        //Inner Brown Circle
        g.fillOval(aniwidth/7+45,aniheight-185,10,10);
        //Spokes
        //Verticle
        g.fillRect(aniwidth/7+49,aniheight-200,2,35);
        //Horizontal
        g.fillRect(aniwidth/7+33,aniheight-182,35,2);
        
        //*Wheel 2
        //Outer Brown Circle
        g.fillOval(aniwidth/7+130,aniheight-200,40,40);
        g.setColor(new Color(0,0,0));
        //Inner Black Circle
        g.fillOval(aniwidth/7+135,aniheight-195,30,30);
        g.setColor(new Color(133,94,66));
        //Inner Brown Circle
        g.fillOval(aniwidth/7+145,aniheight-185,10,10);
        //Spokes
        //Verticle
        g.fillRect(aniwidth/7+149,aniheight-200,2,35);
        //Horizontal
        g.fillRect(aniwidth/7+133,aniheight-182,35,2);

        //*Counter Weight
        g.setColor(new Color(85,60,42));
        //Left wing
        Rectangle2D leftweig = new Rectangle2D.Double(counterwidth/7+100,counterheight-290,35,5);
        AffineTransform lweig = new AffineTransform();
        lweig.rotate(Math.toRadians(-50), leftweig.getX() + leftweig.getWidth()/2, leftweig.getY() + leftweig.getHeight()/2);
        lweig.translate(0,0);
        Shape leftweight = lweig.createTransformedShape(leftweig);
        ((Graphics2D) g).fill(leftweight);

        //Right wing
        Rectangle2D rigweig = new Rectangle2D.Double(counterwidth/7+125,counterheight-290,35,5);
        AffineTransform rweig = new AffineTransform();
        rweig.rotate(Math.toRadians(50), rigweig.getX() + rigweig.getWidth()/2, rigweig.getY() + rigweig.getHeight()/2);
        rweig.translate(0,0);
        Shape rightweight = rweig.createTransformedShape(rigweig);
        ((Graphics2D) g).fill(rightweight);

        //Semicircle
        ((Graphics2D) g).setStroke(new BasicStroke(5));
        ((Graphics2D) g).draw(new Arc2D.Double( counterwidth/7+105, counterheight-295, 50, 40, 180, 180, Arc2D.OPEN));

        //Supports
        g.setColor(new Color(43,30,22));
        Rectangle2D leftsup = new Rectangle2D.Double(counterwidth/7+127,counterheight-300,5,45);
        Rectangle2D rightsup = new Rectangle2D.Double(counterwidth/7+105,counterheight-275,50,5);
        ((Graphics2D) g).fill(leftsup);
        ((Graphics2D) g).fill(rightsup);

        //Bolt
        g.setColor(new Color(76,76,76));
        Ellipse2D bolt = new Ellipse2D.Double(counterwidth/7+125,counterheight-305,10,10);
        ((Graphics2D) g).fill(bolt);

        //*Left strut
        g.setColor(new Color(133,94,66));
        Rectangle2D leftstrut = new Rectangle2D.Double(aniwidth/7+60,aniheight-240,110,5);
        AffineTransform lef = new AffineTransform();
        lef.rotate(Math.toRadians(70), leftstrut.getX() + leftstrut.getWidth()/2, leftstrut.getY() + leftstrut.getHeight()/2);
        lef.translate(0,0);
        Shape left = lef.createTransformedShape(leftstrut);
        ((Graphics2D) g).fill(left);

        //*Right strut
        Rectangle2D rightstrut = new Rectangle2D.Double(aniwidth/7+40,aniheight-215,125,5);
        AffineTransform rig = new AffineTransform();
        rig.rotate(Math.toRadians(-70), rightstrut.getX() + rightstrut.getWidth()/2, rightstrut.getY() + rightstrut.getHeight()/2);
        rig.translate(0,0);
        Shape right = rig.createTransformedShape(leftstrut);
        ((Graphics2D) g).fill(right);

        //*Center strut 
        g.fillRect(aniwidth/7+97,aniheight-280,5,100);
        
        //*Center bolt
        g.setColor(new Color(118,92,72));
        g.fillOval(aniwidth/7+93,aniheight-290,15,15);
        g.setColor(new Color(76,76,76));
        g.fillOval(aniwidth/7+95,aniheight-287,10,10);


        //*Launcher
        g.setColor(new Color(68,48,34));
        Rectangle2D launweig = new Rectangle2D.Double(launcherwidth/7-30,launcherheight-275,175,5);
        AffineTransform laweig = new AffineTransform();
        laweig.rotate(Math.toRadians(pangle), launcherwidth / 7 - 30 + 125, launcherheight - 255+ 5/2);
        laweig.translate(0,0);
        Shape launcherweight = laweig.createTransformedShape(launweig);
        ((Graphics2D) g).fill(launcherweight);


        g.fillOval(aniwidth/7+55,aniheight-400,PIXEL_SIZE,PIXEL_SIZE);


        if(shoot == true && pangle==61){

            //*projectile (if shot)
            g.setColor(Color.red);
            g.fillOval(projx,projy,PIXEL_SIZE,PIXEL_SIZE);
            //*seeing the intersection triangle
            g.fillOval(mousexpos,mouseypos,10,10);

            //*drawing the pathing of the projectile using the arraylists
            if (trailType == 0)
            {
                for(int i = 0; i < xcoords.size(); i += 20)
                {
                g.setColor(Color.white);
                g.fillOval(xcoords.get(i),ycoords.get(i),10,10);
                }
            }
            if (trailType == 1)
            {
                for(int i = 0; i < j ; i += 20)
                {
                g.setColor(Color.white);
                g.fillOval(xcoords.get(i),ycoords.get(i),10,10);
                } 
            }
        }
    }

        //*************************************************************************************************************************************RANDOMIZATION METHODS************************************************************************************************//
    public static void wind(){
        //* Used to randomize air resistance in either direction every 2 shots (variable direction determines sign of resistance)
        if(numshots%2==0){
            direction = (int) (Math.random()*2);
            if(direction%2==0){
                 airResistance = -1*(Math.random()*0.1);
                 if(airResistance>-0.01){
                     airResistance = -0.01;
                 }
            }
            else{
                airResistance = (Math.random()*0.1);
                if(airResistance<0.01){
                    airResistance = 0.01;
                }
            }
        }
    }

    //* Randomizing planet selected
    public static void planet(){
         world = (int) (Math.random()*4);
        if(world==0){
            //* Earth gravity
            gravity = -32;
            earth = true;
        }
        else if(world==1){
            //* Moon gravity
            gravity = -5.3;
            moon = true;
        }
        else if(world==2){
            //* Mars gravity
            gravity = -12.2;
            mars = true;
        }
        else{
            //* Jupiter gravity
            gravity = -50;
            jupiter = true;
        }
    }

        //*************************************************************************************************************************************CALCULATION METHODS************************************************************************************************//
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
        ivelocity = hypotenuse/2;
        //System.out.println("Velocity: " + ivelocity);
        
        mass = weight*30 / Math.abs(gravity);
        fraction = airResistance*50 / mass;
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

        //*************************************************************************************************************************************INITIALIZATION METHODS************************************************************************************************//
    //* Called at the begining of every new shot to fill the array list with projetile locations
    public void alist(){
        if(projy == aniheight -400){
            //*find the initial pathing of the projectile as time increases and storing it in the arraylist
            while(hy>0){
                //*Increments t differently to add more or less points to the arraylist based on the velocity of the shot
                if(ivelocity<200){
                    t+=0.01;  
                }
                else if(ivelocity<300){
                    t+=0.0075;
                }
                else{
                    t+=0.005;
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
        //System.out.println("Size: " + xcoords.size());
    }

    public void getmouseinfo(){
       //*Getting the location of the mouse when it is released
        PointerInfo info = MouseInfo.getPointerInfo();
        mousexpos = (int) info.getLocation().getX()-12;
        mouseypos = (int) info.getLocation().getY()-35; 
    }

        //*************************************************************************************************************************************LISTENER METHODS************************************************************************************************//
    //*The following methods are used to determine if a mouse action has been performed
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        if(shoot == false){
            getmouseinfo();
        }
        //*Checking to see if the shot is behind and lower than the ball and that ball is not already in the air
        if(mousexpos<aniwidth/7+55 && mouseypos>aniheight-400 && projx==aniwidth/7+55 && projy == aniheight-400 && shoot==false){
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

    //*Classes used to listen for action performed and change the specific variables required
    public class LeftAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            moveLeft = true;
        }
        
    }

        public class RightAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            moveRight = true;
        }
        
    }

}
