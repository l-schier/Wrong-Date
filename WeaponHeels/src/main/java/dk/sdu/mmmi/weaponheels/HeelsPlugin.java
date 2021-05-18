package dk.sdu.mmmi.weaponheels;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DescriptionPart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.data.entityparts.StunPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Kaan
 */
public class HeelsPlugin implements IGamePluginService {
    private final String spriteFile = "heels.png", descriptionFile = "heels.txt";

    @Override
    public void start(GameData gameData, World world) {
        Entity heels = createHeels(gameData);
        world.addEntity(heels);
    }

    public Entity createHeels(GameData gameData) {
        Entity heels = new Heels();
        Random random = new Random();

        float x = (float) random.nextInt(gameData.getDisplayWidth() - 64) + 32;
        float y = (float) random.nextInt(gameData.getDisplayHeight() - 64) + 32;
        float duration = 10;
        boolean interactable = true;
        heels.setRadius(8);

        heels.add(new PositionPart(x, y));
        heels.add(new StunPart(duration));
        heels.add(new InteractPart(interactable));
        heels.add(new RenderPart(this.spriteFile, heels));
        heels.add(new DescriptionPart(descriptionFile, heels));

        return heels;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity heels : world.getEntities(Heels.class)) {
            world.removeEntity(heels);
        }
    }

    @Override
    public String[] getSpritePaths() {
        return new String[]{};
    }
}
