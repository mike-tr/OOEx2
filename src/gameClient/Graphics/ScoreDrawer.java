package gameClient.Graphics;

import gameClient.GameData.PokemonGameData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ScoreDrawer extends JPanel {
    private JLabel level;
    private JLabel grade;
    private JLabel time;
    private JLabel moves;
    private JLabel[] agents;

    public ScoreDrawer(PokemonGameData gameData){
        //this.setLayout(new GridLayout(0,3));
        this.setLayout(new GridLayout());
        this.setBorder(new EmptyBorder(5,5,5,5));
        JPanel inner = new JPanel(new GridLayout());
        inner.setBorder(new LineBorder(Color.black, 1, true));
        this.add(inner);

        JPanel innerGrid = new JPanel(new GridLayout(0, 3));
        innerGrid.setBorder(new EmptyBorder(2,2,2,2));
        inner.add(innerGrid);

        JPanel gridLeft = new JPanel(new GridLayout(2, 0));
        JPanel gridCenter = new JPanel(new GridLayout(2, 0));
        JPanel gridRight = new JPanel();
        var layout = new BoxLayout(gridRight, BoxLayout.Y_AXIS);
        gridRight.setLayout(layout);

        innerGrid.add(gridLeft);
        innerGrid.add(gridCenter);
        innerGrid.add(gridRight);

        level = new JLabel();
        time = new JLabel();
        grade = new JLabel();
        moves = new JLabel();
        gridLeft.add(level);
        gridLeft.add(time);
        gridCenter.add(grade);
        gridCenter.add(moves);
        gridRight.add(new JLabel("- Scores - "));

        new Thread(() -> {
            try{
                while (!gameData.isInitialized()){
                    Thread.sleep(10);
                }

                level.setText("Level :" + gameData.getGameData().level);
                agents = new JLabel[gameData.getAgents().size()];
                for(int i = 0; i<agents.length; i++){
                    agents[i] = new JLabel();
                    gridRight.add(agents[i]);
                }
                while (!gameData.isFinished()){
                    var data = gameData.getAndUpdateGameData();
                    grade.setText("grade : " + data.grade);
                    moves.setText("moves : " + data.moves);
                    var t = (gameData.getGame().timeToEnd() / 100)/10.0;
                    time.setText("time left : " + t);

                    int i = 0;
                    for (var agent: gameData.getAgents()) {
                        agents[i].setText("Agent (" + agent.getId() + "): " + agent.getValue());
                        i++;
                    }
                    Thread.sleep(100);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }


}
