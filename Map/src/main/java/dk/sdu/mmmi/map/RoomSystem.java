/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.map;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PlayerPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.WallPart;
import dk.sdu.mmmi.common.services.ICollisionChecker;
import dk.sdu.mmmi.common.services.IEntityProcessingService;

/**
 *
 * @author lasse
 */
public class RoomSystem implements IEntityProcessingService {

    private ICollisionChecker collisionChecker;

    @Override
    public void process(GameData gd, World world) {
        Entity currentRoom = findCurrentRoom(gd, world);
        if(currentRoom == null) {
            Entity newRoom = RoomPlugin.createRoom(gd, gd.getCamX() - gd.getMenuWidth(), gd.getCamY());
            world.addEntity(newRoom);
            System.out.println("New room at: " + (gd.getCamX() - gd.getMenuWidth()) + " : " + gd.getCamY());
        }
        for (Entity room : world.getEntities(Room.class)) {
            updateShape(room);
        }

    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        WallPart wallpart = entity.getPart(WallPart.class);
        float x0 = wallpart.getStartX();
        float y0 = wallpart.getStartY();
        float x1 = wallpart.getEndX();
        float y1 = wallpart.getEndY();

        shapex[0] = (float) (x0);
        shapey[0] = (float) (y0);

        shapex[1] = (float) (x0);
        shapey[1] = (float) (y1);

        shapex[2] = (float) (x1);
        shapey[2] = (float) (y1);

        shapex[3] = (float) (x1);
        shapey[3] = (float) (y0);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }

    public void addCollisionChecker(ICollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
    }

    public void removeCollisionChecker(ICollisionChecker collisionChecker) {
        this.collisionChecker = null;
    }

    private Entity findCurrentRoom(GameData gd, World world) {
        for (Entity room : world.getEntities(Room.class)) {
            PositionPart pos = room.getPart(PositionPart.class);
            if (gd.getCamX() == pos.getX() + gd.getMenuWidth() && gd.getCamY() == pos.getY()) {
                return room;
            }
        }
        return null;
    }
}
