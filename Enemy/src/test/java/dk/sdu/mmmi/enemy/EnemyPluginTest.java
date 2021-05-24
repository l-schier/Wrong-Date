package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Anton
 */
public class EnemyPluginTest {

    public EnemyPluginTest() {
    }

    @Test
    public void testStart() {
        System.out.println("start");
        
        GameData gameData = mock(GameData.class);
        World world = mock(World.class);
        when(world.addEntity(any(Entity.class))).thenReturn("1");

        EnemyPlugin instance = new EnemyPlugin();
        instance.start(gameData, world);

        verify(world).addEntity(any(Entity.class));
    }

    @Test
    public void testStop() {
        System.out.println("stop");
        
        GameData gameData = mock(GameData.class);
        World world = mock(World.class);

        List<Entity> testEnemies = new ArrayList<Entity>();
        testEnemies.add(new Enemy());

        when(world.getEntities(Enemy.class)).thenReturn(testEnemies);

        EnemyPlugin instance = new EnemyPlugin();
        instance.stop(gameData, world);

        verify(world).getEntities(Enemy.class);
        verify(world).removeEntity(any(Entity.class));
    }
}
