package gameClient.Graphics;

import gameClient.utilities.PokemonGameHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PokemonFrame extends JFrame implements LevelLoaderPanel.levelPickedListener, Runnable {
    /*
    this class is responsible for choosing level, and handling panels.
    as well as repainting.
     */
    private boolean running = true;
    private int frames = 60;

    CardLayout cLayout;
    JPanel mainPane;
    GameDrawer gamePanel;
    LevelLoaderPanel loader;
    PokemonGameHandler game;
    public PokemonFrame(String name, int width, int height){
        super(name);
        this.setResizable(true);
        this.setSize(width, height);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
            }
        });

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        mainPane = new JPanel();
        mainPane.setPreferredSize(new Dimension(width,height));
        cLayout = new CardLayout();
        mainPane.setLayout(cLayout);
        this.setLayout(cLayout);
        add(mainPane);

        createGame(0,width, height);
        //createLevelLoader();
        //showLoader();

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createGame(int level, int width, int height){
        game = new PokemonGameHandler(level);
        gamePanel = new GameDrawer(game, width, height);
        mainPane.add("Game", gamePanel);
    }


    public void createLevelLoader(){
        loader = new LevelLoaderPanel(this);
        mainPane.add("Loader", loader);
    }

    public void showGame(){
        cLayout.show(mainPane, "Game");
        setSize(gamePanel.getPreferredSize());
        setResizable(true);
    }

    public void showLoader(){
        cLayout.show(mainPane, "Loader");
        setSize(loader.getPreferredSize());
        setResizable(false);
    }

    public void start(){
        new Thread(this).start();
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        int sleep = 1000 / frames;
        try {
            while (running) {
                repaint();

                time -= System.currentTimeMillis();
                Thread.sleep(sleep - time);
                time = System.currentTimeMillis();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void repaint() {
        mainPane.repaint();
    }

    public boolean running(){
        return running;
    }

    @Override
    public void setLevel(int level) {
        System.out.println("picked level " + level);
        createGame(level, mainPane.getPreferredSize().width, mainPane.getPreferredSize().height);
        showGame();
    }
}
