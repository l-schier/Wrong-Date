package dk.sdu.mmmi.weaponbubblegum;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.BlindPart;
import dk.sdu.mmmi.common.data.entityparts.DescriptionPart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Kaan
 */
public class BubblegumPlugin implements IGamePluginService {

    private final String spriteFile = "bubblegum.png", descriptionFile = "bubblegum.txt";

    @Override
    public void start(GameData gameData, World world) {
        Entity bubblegum = createBubblegum(gameData);
        world.addEntity(bubblegum);
    }

    public Entity createBubblegum(GameData gameData) {
        Entity bubblegum = new Bubblegum();
        Random random = new Random();

        float x = (float) random.nextInt(gameData.getDisplayWidth() - 64) + 32;
        float y = (float) random.nextInt(gameData.getDisplayHeight() - 64) + 32;
        float duration = 10;
        boolean interactable = true;
        bubblegum.setRadius(8);

        bubblegum.add(new PositionPart(x, y));
        bubblegum.add(new BlindPart(duration));
        bubblegum.add(new InteractPart(interactable));
        bubblegum.add(new RenderPart(this.spriteFile, bubblegum));
        bubblegum.add(new DescriptionPart(descriptionFile, bubblegum));

        return bubblegum;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity bubblegum : world.getEntities(Bubblegum.class)) {
            world.removeEntity(bubblegum);
        }
    }

    @Override
    public String[] getSpritePaths() {
        return new String[]{};
    }
}
