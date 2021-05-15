package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IPathfinder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Anton
 */
public class EnemySystemTest {
    
    public EnemySystemTest() {
    }
    
    @Test
    public void testProcess() {
        System.out.println("process");
        
        GameData gameData = new GameData();        
        World world = new World();
        
        EnemyPlugin enemyPlugin = new EnemyPlugin();
        enemyPlugin.start(gameData, world);
        
        Entity enemy = world.getEntities(Enemy.class).get(0);
        PositionPart pos = enemy.getPart(PositionPart.class);

        float startX = pos.getX();
        float startY = pos.getY();
        
        PositionPart nextPos = new PositionPart(startX+1, startY+1);
        
        IPathfinder aStarPathfinder = mock(IPathfinder.class);
        when(aStarPathfinder.findNextPosition(enemy, gameData, world)).thenReturn(nextPos);
        
        EnemySystem instance = new EnemySystem();
        instance.addPathfinder(aStarPathfinder);
        instance.process(gameData, world);

        float endX = pos.getX();
        float endY = pos.getY();

        assertNotEquals((int) startX, (int) endX);
        assertNotEquals((int) startY, (int) endY);
    }
}
