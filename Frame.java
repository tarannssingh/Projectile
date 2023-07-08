import javax.swing.JFrame;

public class Frame extends JFrame {
    
    //Creates a basic method that allows for some basic info about the panel to be inputted
    //Allows us to save space in Panel.java by simply creating a Frame object
    public Frame(){
        //adds a panel to the frame
        this.add(new Panel());
        //sets a title to the GUI (seen in the top left corner)
        this.setTitle("Projectile Launcher");
        //allows the GUI to close when the 'x' button is clicked (in the top right corner)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //prevents the GUI from being resized by the user
        this.setResizable(false);
        //all places all of these elements into the panel
        this.pack();
        //allows the GUI to be visible
        this.setVisible(true);
    }
}
