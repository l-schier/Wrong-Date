package dk.sdu.mmmi.weaponheels;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IInteractService;

/**
 *
 * @author Kaan
 */
public class HeelsProcessor implements IEntityProcessingService, IInteractService {

    @Override
    public void process(GameData gameData, World world) {

    }

    @Override
    public void interact(Entity user, World world) {
        InventoryPart inventory = user.getPart(InventoryPart.class);
        Entity currentWeapon = inventory.getCurrentWeapon();
        for (Entity heels : world.getEntities(Heels.class)) {
            InteractPart interact = heels.getPart(InteractPart.class);
            if (user.circleCollision(heels) && interact.isInteractable() && heels != currentWeapon) {
                inventory.addItem(heels);
            }
        }
    }
}
