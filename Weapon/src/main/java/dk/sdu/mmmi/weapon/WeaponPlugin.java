package dk.sdu.mmmi.weapon;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Jacob
 */
public class WeaponPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Entity weapon = createWeapon(gameData);
        world.addEntity(weapon);
    }
    
    public Entity createWeapon(GameData gameData) {
        Entity weapon = new Weapon();
        Random random = new Random();
        
        float x = (float)random.nextInt(gameData.getDisplayWidth());
        float y = (float)random.nextInt(gameData.getDisplayHeight());
        int damage = 1;
        boolean interactable = true;

        weapon.add(new PositionPart(x, y));
        weapon.add(new DamagePart(damage));
        weapon.add(new InteractPart(interactable));
        
        return weapon;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(Weapon.class)) {
            world.removeEntity(weapon);
        }
    }
    
}
