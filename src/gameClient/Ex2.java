package gameClient;

import gameClient.Graphics.PokemonFrame;

public class Ex2 {
    static PokemonFrame game;
    public static void main(String[] args) {
        game = new PokemonFrame("kek", 800, 600, args);
        game.start();
    }
}
