import gameClient.GameData.PokemonGameData;
import gameClient.utilities.IMain;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class testsPart2 {
    /**
     * JUST RUN THE GAME, its a test a visual test!
     */

    @Test
    public void testLevel0(){
        PokemonGameData data = new PokemonGameData(0, -1, new IMain() {
            @Override
            public void hasStopped(Object target) { }
        });

        while (!data.isFinished()){
            try{
                Thread.sleep(50);
            }catch (Exception e){

            }
        }
        assertTrue(data.getGameData().moves < 200);
        assertTrue(data.getGameData().grade > 100);
    }

    @Test
    public void testLevel11(){
        PokemonGameData data = new PokemonGameData(11,-1, new IMain() {
            @Override
            public void hasStopped(Object target) { }
        });

        while (!data.isFinished()){
            try{
                Thread.sleep(50);
            }catch (Exception e){

            }
        }
        assertTrue(data.getGameData().moves < 600);
        assertTrue(data.getGameData().grade > 1800);
    }
}
