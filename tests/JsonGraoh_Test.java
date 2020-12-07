import implementation.DWGraph_DS;
import implementation.utilities.JsonGraph;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonGraoh_Test {
    @Test
    public void testLoadDataGraph(){
        JsonGraph graphLoaded = JsonGraph.fromFile("data/A1");
        DWGraph_DS graph = new DWGraph_DS(graphLoaded);

        String graphJsonS = JsonGraph.getReadFile("data/A1");
        assertEquals(graphJsonS, JsonGraph.toJson(graph));

        assertEquals(graphJsonS, graphLoaded.toJson());
    }
}