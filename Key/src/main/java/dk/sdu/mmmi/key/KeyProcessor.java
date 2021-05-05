package dk.sdu.mmmi.key;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IInteractService;
import dk.sdu.mmmi.common.services.IItemService;

import java.awt.Image;

/**
 *
 * @author Anton
 */
public class KeyProcessor implements IEntityProcessingService, IItemService, IInteractService {

    @Override
    public void process(GameData gameData, World world) {

    }

    public void useItem(Entity player, GameData gameData) {
        
    }

    @Override
    public String getDescription() {
        return "Key";
    }

    @Override
    public Image getSprite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void interact(Entity user, World world) {
        for (Entity key : world.getEntities(Key.class)) {
            InteractPart interact = key.getPart(InteractPart.class);
            RenderPart render = key.getPart(RenderPart.class);
            if (user.circleCollision(key) && interact.isInteractable()) {
                InventoryPart inventory = user.getPart(InventoryPart.class);
                inventory.addItem(key);
                interact.setInteractable(false);
                render.setVisibility(false);
            }
        }
    }
}
