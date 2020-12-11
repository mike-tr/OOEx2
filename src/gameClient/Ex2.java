package gameClient;

import gameClient.Graphics.PokemonFrame;

public class Ex2 {
    static PokemonFrame game;
    static int frames = 60;

    long time;
    public static void main(String[] args) {
        game = new PokemonFrame("kek", 800, 600);
        game.start();
    }
}
