//basic imports that are being used or are most likly going to be used
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList; 
import java.lang.Math;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

//makes sure to add Action Listener to eventually allow us to compute user action on the screen
public class Panel extends JPanel implements ActionListener {


    static int PIXEL_SIZE = 20;
    static int ScreenWidth;
    static int ScreenHeight;

    boolean shoot= false;
    boolean moveRight= false;
    boolean moveLeft= false;

    static int projx;
    static int projy;

    static int wheel1;
    static int wheel2;
    static int body;
    static int shaft;

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
        
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //bg
        g.setColor(new Color(173,216,230));
        g.fillRect(0,0,ScreenWidth, ScreenHeight - PIXEL_SIZE);
        //ground
        g.setColor(new Color(0,100,0));
        g.fillRect(0,ScreenHeight-90,ScreenWidth,90); 
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }



    
}