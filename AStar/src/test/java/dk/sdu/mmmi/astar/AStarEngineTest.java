package dk.sdu.mmmi.astar;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Anton
 */
public class AStarEngineTest {
    
    public AStarEngineTest() {
    }

    @Test
    public void testSearch() {
        System.out.println("search");
        World world = null;
        Entity me = null;
        PositionPart goalState = null;
        AStarEngine instance = null;
        PositionPart expResult = null;
        PositionPart result = instance.search(world, me, goalState);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
