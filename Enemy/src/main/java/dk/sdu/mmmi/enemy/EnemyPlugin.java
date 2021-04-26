package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.SightPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
        String sprite = getSprite();

        Entity enemy = createEnemy(gameData, sprite);
        world.addEntity(enemy);
    }

    private String getSprite() {
        // Get module sprites
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(spriteFile);
        File temp = new File(spriteFile);

        // Copy module sprites to runner folder
        try {
            // Reconsider replacing existing
            // Or giving unique name to file during copy
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return spriteFile;
        } catch (Exception e) {
        }

        return null;
    }

    private Entity createEnemy(GameData gameData, String sprite) {
        float x = 50;
        float y = 50;

        Entity enemy = new Enemy();

        enemy.add(new LifePart(10));
        enemy.add(new MovingPart(1));
        enemy.add(new SightPart(200));
        enemy.add(new PositionPart(x, y));

        enemy.setRadius(8);
        enemy.setSpriteFile(sprite);

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
