package gameClient.Graphics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LevelLoaderPanel extends JPanel implements ActionListener {
    public interface levelPickedListener{
        void setLevel(int level, int id);
    }
    levelPickedListener listener;
    JButton button;
    JTextField levelID;
    JTextField userID;

    /**
     * Get userID, and level ID, then send it back to the GAME
     * @param listener the main frame
     */
    public LevelLoaderPanel(JFrame frame , levelPickedListener listener){
        this.listener = listener;
        //this.setLayout(new FlowLayout());
        button = new JButton("Submit");
        button.addActionListener(this);

        levelID = new JTextField(10);;
        levelID.setText("");

        userID = new JTextField(15);
        userID.setText("");

        setLayout(new GridLayout(0,1));
        var layout = new GridLayout(0,2);
        JPanel top = new JPanel(layout);
        top.setBorder(new EmptyBorder(5,5,5,5));
        JPanel center = new JPanel(layout);
        center.setBorder(new EmptyBorder(5,5,5,5));
        //JPanel buttom = new JPanel(new GridLayout(0, 1));
        //top.setBorder(new EmptyBorder(5,5,5,5));

        top.add(new JLabel("Enter ID :"));
        top.add(userID);
        center.add(new JLabel("Enter level [ 0 - 23 ] :"));
        center.add(levelID);
        add(top);
        add(center);

        frame.getRootPane().setDefaultButton(button);
        //add(new JLabel(""), BorderLayout.SOUTH);
        add(button);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 150);
    }

    /**
     * when we pressed on submit we try to parse the id and level and send it to the game.
     * @param e the action made
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button) {
            sendData(userID.getText(), levelID.getText());
        }
    }

    public boolean sendData(String id, String level){
        try{
            int iid = -1;
            try{
                iid = Integer.parseInt(id);
            }catch (Exception e){ }
            listener.setLevel(Integer.parseInt(level), iid);
            return true;
        }catch(NumberFormatException exception){
            if(id.length() > 10){
                System.out.println("ERROR : id is way to long!");
                userID.setText("max 15 characters!");
                levelID.setText("");
                return false;
            }
            System.out.println("Wrong format - UserID and Level should be integers");
            levelID.setText("Should be an integer!");
            userID.setText("");
            return false;
            //exception.printStackTrace();
        }catch (Exception e){
            System.out.println("OPS something went wrong");
            System.out.println("Most likely a server error, choose another level");
            userID.setText("Server Error");
            levelID.setText("choose different level");
            return false;
        }
    }
}
