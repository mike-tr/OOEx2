package gameClient.Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelLoaderPanel extends JPanel implements ActionListener {
    public interface levelPickedListener{
        void setLevel(int level);
    }
    levelPickedListener listener;
    JButton button;
    JTextField textField;

    public LevelLoaderPanel(levelPickedListener listener){
        this.listener = listener;
        //this.setLayout(new FlowLayout());
        JLabel label = new JLabel("Choose Game Level : ");
        button = new JButton("Submit");
        button.addActionListener(this);

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(120,30));
        textField.setFont(new Font("Consolas",Font.PLAIN,25));
        textField.setForeground(new Color(0x00FF00));
        textField.setBackground(Color.black);
        textField.setCaretColor(Color.white);
        textField.setText("");

        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        add(button,BorderLayout.SOUTH);
        add(label, BorderLayout.WEST);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button) {
            String text = textField.getText();
            try{
                listener.setLevel(Integer.parseInt(text));
            }catch(Exception exception){
                System.out.println("Wrong format pick a number");
                //exception.printStackTrace();
            }
        }
    }
}
