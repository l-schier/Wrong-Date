/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weaponheels;

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
public class HeelsProcessor implements IEntityProcessingService, IInteractService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity heels : world.getEntities(Heels.class)) {
            PositionPart positionPart = heels.getPart(PositionPart.class);
            setShape(heels);
        }
    }

    public void setShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = 3.1415f / 2;

        shapex[0] = (float) (x + Math.cos(radians) * 12);
        shapey[0] = (float) (y + Math.sin(radians) * 12);

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
        for (Entity heels : world.getEntities(Heels.class)) {
            InteractPart interact = heels.getPart(InteractPart.class);
            if (user.circleCollision(heels) && interact.isInteractable() && heels != currentWeapon) {
                inventory.addItem(heels);
            }
        }
    }
}
