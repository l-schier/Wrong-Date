package dk.sdu.mmmi.weaponnails;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.DescriptionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Jacob
 */
public class NailsPlugin implements IGamePluginService {

    private final String spriteFile = "weapon.png", descriptionFile = "WeaponsDesription.txt";

    @Override
    public void start(GameData gameData, World world) {
        Entity nails = createWeapon(gameData);
        world.addEntity(nails);
    }

    public Entity createWeapon(GameData gameData) {
        Entity nails = new Nails();
        Random random = new Random();

        float x = (float) random.nextInt(gameData.getDisplayWidth());
        float y = (float) random.nextInt(gameData.getDisplayHeight());
        int damage = 1;
        boolean interactable = true;
        nails.setRadius(8);

        nails.add(new PositionPart(x, y));
        nails.add(new DamagePart(damage));
        nails.add(new InteractPart(interactable));
        nails.add(new RenderPart(spriteFile, nails));
        nails.add(new DescriptionPart(descriptionFile, nails));

        return nails;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity nails : world.getEntities(Nails.class)) {
            world.removeEntity(nails);
        }
    }

}
