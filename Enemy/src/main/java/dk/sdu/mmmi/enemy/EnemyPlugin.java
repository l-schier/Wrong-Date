package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.SightPart;
import dk.sdu.mmmi.common.services.IGamePluginService;

/**
 *
 * @author Anton
 */
public class EnemyPlugin implements IGamePluginService {

    public EnemyPlugin() {
        
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity enemy = createEnemy(gameData);
        world.addEntity(enemy);
    }

    private Entity createEnemy(GameData gameData) {
        float x = 50;
        float y = 50;
        
        Entity enemy = new Enemy();
        
        enemy.add(new LifePart(100));        
        enemy.add(new MovingPart(1));
        enemy.add(new SightPart(200));
        enemy.add(new PositionPart(x, y));
        
        enemy.setRadius(8);
        
        return enemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity enemy : world.getEntities(Enemy.class)) {
            world.removeEntity(enemy);
        }
    }
}
