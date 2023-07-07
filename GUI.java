import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
;


public class GUI implements ActionListener {

    int counts = 0;
    JLabel label;

    public GUI(){
        JFrame frame = new JFrame("My First GUI");

        JButton button = new JButton("Shoot");
        button.addActionListener(this);
        label = new JLabel("Number of Shots: 0");

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(button);
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
       new GUI();
       System.out.println("Properly Running");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        counts++;
        label.setText("Number of Shots: " + counts);
    }
    
}