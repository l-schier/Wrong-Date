package dk.sdu.mmmi.weaponfoundation;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IInteractService;

/**
 *
 * @author Kaan
 */
public class FoundationProcessor implements IEntityProcessingService, IInteractService {
    
    @Override
    public void process(GameData gameData, World world) {
        for (Entity foundation : world.getEntities(Foundation.class)) {
            setShape(foundation);
        }
    }

    public void setShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = 3.1415f / 2;

        shapex[0] = (float) (x + Math.cos(radians) * 16);
        shapey[0] = (float) (y + Math.sin(radians) * 16);

        shapex[1] = (float) (x);
        shapey[1] = (float) (y);

        shapex[2] = (float) (x);
        shapey[2] = (float) (y);

        shapex[3] = (float) (x);
        shapey[3] = (float) (y);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
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
