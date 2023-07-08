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


public class Panel extends JPanel implements ActionListener {

    


    static int PIXEL_SIZE = 20;
    static int ScreenWidth;
    static int ScreenHeight;

    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Frame a = new Frame();
        Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
        ScreenWidth = (int) screenSize.getWidth();
        ScreenHeight = (int) screenSize.getHeight();
        a.setSize((int) screenSize.getWidth(),(int) screenSize.getHeight());
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }



    
}