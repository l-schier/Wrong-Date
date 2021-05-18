/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weaponbubblegum;

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
public class BubblegumProcessor implements IEntityProcessingService, IInteractService {
     @Override
    public void process(GameData gameData, World world) {
        for (Entity bubblegum : world.getEntities(Bubblegum.class)) {
            PositionPart positionPart = bubblegum.getPart(PositionPart.class);
            setShape(bubblegum);
        }
    }

    public void setShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = 3.1415f / 2;

        shapex[0] = (float) (x + Math.cos(radians) * 20);
        shapey[0] = (float) (y + Math.sin(radians) * 20);

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
        for (Entity bubblegum : world.getEntities(Bubblegum.class)) {
            InteractPart interact = bubblegum.getPart(InteractPart.class);
            if (user.circleCollision(bubblegum) && interact.isInteractable() && bubblegum != currentWeapon) {
                inventory.addItem(bubblegum);
                System.out.println("Added bubblegum");
            }
        }
    }
}
