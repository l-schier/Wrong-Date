package dk.sdu.mmmi.astar;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PlayerPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.SightPart;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Anton
 */
public class AStarPathfinderTest {
    
    public AStarPathfinderTest() {
    }
    
    @Test
    public void testFindNextPosition() {
        System.out.println("findNextPosition");
        
        Entity me = new Entity();
        PositionPart startPos = new PositionPart(10, 10);
        me.add(new SightPart(200));
        me.add(startPos);
        
        GameData gameData = new GameData();
        World world = new World();
        world.addEntity(me);
        
        AStarPathfinder instance = new AStarPathfinder();
        
        // random test
        PositionPart nextPos = instance.findNextPosition(me, gameData, world);
        String from = "10:10";
        String to = nextPos.getX() + ":" + nextPos.getY();
        assertNotEquals(from, to);
        
        // reset
        startPos.setX(10);
        startPos.setY(10);
        
        Entity target = new Entity();
        target.add(new PlayerPart());
        target.add(new PositionPart(20, 20));
        world.addEntity(target);
        
        // target test
        nextPos = instance.findNextPosition(me, gameData, world);
        assertEquals(11, nextPos.getX());
        assertEquals(11, nextPos.getY());
    }    
}
