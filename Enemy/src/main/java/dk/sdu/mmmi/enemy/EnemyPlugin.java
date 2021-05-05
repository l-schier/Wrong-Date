package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.EnemyPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.data.entityparts.SightPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author Anton
 */
public class EnemyPlugin implements IGamePluginService {

    private final String spriteFile = "enemy.png";

    public EnemyPlugin() {

    }

    @Override
    public void start(GameData gameData, World world) {
        Entity enemy = createEnemy(gameData);
        world.addEntity(enemy);
    }

    private Entity createEnemy(GameData gameData) {
        float x = 50;
        float y = 50;

        Entity enemy = new Enemy();
        enemy.add(new EnemyPart());
        enemy.add(new LifePart(10));
        enemy.add(new MovingPart(1));
        enemy.add(new SightPart(200));
        enemy.add(new PositionPart(x, y));
        enemy.add(new RenderPart(this.spriteFile, enemy));

        enemy.setRadius(8);

        return enemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity enemy : world.getEntities(Enemy.class)) {
            world.removeEntity(enemy);
        }

        // Delete images from runner storage
        File temp = new File(spriteFile);
        try {
            Files.delete(temp.toPath());
        } catch (Exception e) {
        }
    }
}
