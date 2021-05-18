package dk.sdu.mmmi.weaponfoundation;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.BlindPart;
import dk.sdu.mmmi.common.data.entityparts.HelpPart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Kaan
 */
public class FoundationPlugin implements IGamePluginService {
    private final String spriteFile = "foundation.png", helpFile = "foundation.txt";

    @Override
    public void start(GameData gameData, World world) {
        Entity foundation = createFoundation(gameData);
        world.addEntity(foundation);
    }

    public Entity createFoundation(GameData gameData) {
        Entity foundation = new Foundation();
        Random random = new Random();

        float x = (float) random.nextInt(gameData.getDisplayWidth() - 64) + 32;
        float y = (float) random.nextInt(gameData.getDisplayHeight() - 64) + 32;
        float duration = 10;
        boolean interactable = true;
        foundation.setRadius(8);

        foundation.add(new PositionPart(x, y));
        foundation.add(new BlindPart(duration));
        foundation.add(new InteractPart(interactable));
        foundation.add(new RenderPart(this.spriteFile, foundation));
        foundation.add(new HelpPart(helpFile, foundation));

        return foundation;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity foundation : world.getEntities(Foundation.class)) {
            world.removeEntity(foundation);
        }
    }

    @Override
    public String[] getSpritePaths() {
        return new String[]{};
    }
}
