package dk.sdu.mmmi.key;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.KeyPart;
import dk.sdu.mmmi.common.data.entityparts.KeyPart.KeyColor;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Anton
 */
public class KeyPlugin implements IGamePluginService {
    
    private final String spriteFile = "silver.png";
    
    @Override
    public void start(GameData gameData, World world) {
        Entity key = createKey(gameData);
        world.addEntity(key);
    }

    public Entity createKey(GameData gameData) {
        Entity key = new Key();
        Random random = new Random();

        float x = (float) random.nextInt(gameData.getDisplayWidth() - 64) + 32;
        float y = (float) random.nextInt(gameData.getDisplayHeight() - 64) + 32;
        key.add(new PositionPart(x, y));
        key.setRadius(8);

        key.add(new InteractPart(true));
        key.add(new KeyPart(KeyColor.Silver));
        key.add(new RenderPart(this.spriteFile, key));

        return key;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity key : world.getEntities(Key.class)) {
            world.removeEntity(key);
        }
    }

    @Override
    public String[] getSpritePaths() {
        return new String[]{spriteFile};
    }
}
