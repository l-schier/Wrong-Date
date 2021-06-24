package dk.sdu.mmmi.weaponfoundation;

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
public class FoundationProcessor implements IEntityProcessingService, IInteractService {
    
    @Override
    public void process(GameData gameData, World world) {

    }

    @Override
    public void interact(Entity user, World world) {
        InventoryPart inventory = user.getPart(InventoryPart.class);
        Entity currentWeapon = inventory.getCurrentWeapon();
        for (Entity foundation : world.getEntities(Foundation.class)) {
            InteractPart interact = foundation.getPart(InteractPart.class);
            if (user.circleCollision(foundation) && interact.isInteractable() && foundation != currentWeapon) {
                inventory.addItem(foundation);
            }
        }
    }
}
