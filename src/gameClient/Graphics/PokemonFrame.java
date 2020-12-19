package gameClient.Graphics;

import gameClient.GameData.PokemonGameData;
import gameClient.utilities.IMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * The main frame, with is the actual window, with the ui etc...
 */
public class PokemonFrame extends JFrame implements LevelLoaderPanel.levelPickedListener, Runnable, IMain {
    private boolean running = true;
    private int frames = 60;

    CardLayout cLayout;
    JPanel mainPane;
    GameDrawer gamePanel;
    LevelLoaderPanel loader;
    PokemonGameData game;

    ArrayList<Integer> levels = new ArrayList<>();

    /**
     * This class is responsible for everything, from logging in to creating the window, and getting the right level
     * @param name
     * @param width
     * @param height
     */
    public PokemonFrame(String name, int width, int height, String[] args){
        super(name);
        this.setResizable(true);
        this.setSize(width, height);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
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

        createLevelLoader();
        if(args.length > 0){
            if(loader.sendData(args[0], args[1]) == false){
                System.out.println("WRONG INPUT");
                running = false;
                return;
            }
        }else{
            createLevelLoader();
            showLoader();
        }

        //createGame(23, width,height);

        //createGame(0,width, height); // you have [0,23] games


        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * create the game and load it.
     * @param level
     * @param width
     * @param height
     */
    public void createGame(int level, int id, int width, int height){
        if(gamePanel != null) {
            mainPane.remove(gamePanel);
        }
        game = new PokemonGameData(level, id, this);
        gamePanel = new GameDrawer(game, width, height);
        mainPane.add("Game", gamePanel);
    }

    /**
     * Create a login screen
     */
    public void createLevelLoader(){
        loader = new LevelLoaderPanel(this,this);
        mainPane.add("Loader", loader);
    }

    /**
     * after login choose the right level
     */
    public void showGame(){
        cLayout.show(mainPane, "Game");
        setSize(gamePanel.getPreferredSize());
        setResizable(true);
    }

    /**
     * show the loading screen
     */
    public void showLoader(){
        cLayout.show(mainPane, "Loader");
        setSize(loader.getPreferredSize());
        setResizable(false);
    }

    /**
     * Create the main loop for this windows
     */
    public void start(){
        new Thread(this).start();
    }

    /**
     * the main loop
     */
    @Override
    public void run() {
        long time = System.currentTimeMillis();
        int sleep = 1000 / frames;
        try {
            while (running) {
                repaint();

                // Try to always sleep exactly the time needed.
                time -= System.currentTimeMillis();
                Thread.sleep(sleep + time);
                time = System.currentTimeMillis();
            }
            System.out.println("stopped");
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }catch (Exception e){

        }
    }

    /**
     * repaint everything
     */
    @Override
    public void repaint() {
        mainPane.repaint();
    }

    /**
     * check if the window still running
     * @return
     */
    public boolean running(){
        return running;
    }

    /**
     * stop everything
     */
    public void stop(){
        running = false;
        if(game != null) {
            game.stop();
        }
    }

    /**
     * from the Loading screen set the right level and open the game.
     * @param level
     */
    @Override
    public void setLevel(int level, int id) {
        System.out.println("picked level " + level);
        createGame(level, id, mainPane.getPreferredSize().width, mainPane.getPreferredSize().height);
        showGame();
    }

    /**
     * From any thread stop the game, this except to get the game object
     * @param target
     */
    @Override
    public void hasStopped(Object target) {
        if(target == game) {
            running = false;
            System.out.println("game has stopped closing everything");
        }
    }
}
