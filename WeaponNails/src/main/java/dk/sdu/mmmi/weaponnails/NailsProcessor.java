package dk.sdu.mmmi.weaponnails;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IInteractService;

/**
 *
 * @author Jacob
 */
public class NailsProcessor implements IEntityProcessingService, IInteractService {
    
    @Override
    public void process(GameData gameData, World world) {

    }

    @Override
    public void interact(Entity user, World world) {
        InventoryPart inventory = user.getPart(InventoryPart.class);
        Entity currentWeapon = inventory.getCurrentWeapon();
        for (Entity nails : world.getEntities(Nails.class)) {
            InteractPart interact = nails.getPart(InteractPart.class);
            if (user.circleCollision(nails) && interact.isInteractable() && nails != currentWeapon) {
                inventory.addItem(nails);
            }
        }
    }
}
