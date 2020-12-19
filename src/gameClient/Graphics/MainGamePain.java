package gameClient.Graphics;

import gameClient.GameData.PokemonGameData;

import javax.swing.*;

public class MainGamePain extends JPanel {

    private GameDrawer gameDrawer;
    private ScoreDrawer scoreDrawer;
    private PokemonGameData gameData;
    public MainGamePain(PokemonGameData gameData, int width, int height){
        gameDrawer = new GameDrawer(gameData, this, width, height);
        scoreDrawer = new ScoreDrawer(gameData);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.add(scoreDrawer);
        this.add(gameDrawer);
    }
}
