/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.map;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DoorPart;
import dk.sdu.mmmi.common.data.entityparts.KeyPart;

import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.data.entityparts.WallPart;
import dk.sdu.mmmi.common.services.IGamePluginService;

/**
 *
 * @author lasse
 */
public class RoomPlugin implements IGamePluginService {

    private final String wallSpriteFile = "wall.png";
    private final String doorSpriteFile = "door.png";
    
    @Override
    public void start(GameData gd, World world) {
        Entity room = createRoom(gd);
        world.addEntity(room);
    }

    private Entity createRoom(GameData gameData) {
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float startX = 32;
        float startY = 32;
        float endX = gameData.getDisplayWidth() - 32;
        float endY = gameData.getDisplayHeight() - 32;
        float[][] doors = new float[4][4];

        doors[0][0] = (endX - startX)/2 - 16;
        doors[0][1] = startY;
        doors[0][2] = (endX - startX)/2 + 16;
        doors[0][3] = startY;
        
        doors[1][0] = endX;
        doors[1][1] = (endY - startY)/2 - 16;
        doors[1][2] = endX;
        doors[1][3] = (endY - startY)/2 + 16;
        
        doors[2][0] = (endX - startX)/2 - 16;
        doors[2][1] = endY;
        doors[2][2] = (endX - startX)/2 + 16;
        doors[2][3] = endY;
        
        doors[3][0] = startX;
        doors[3][1] = (endY - startY)/2 - 16;
        doors[3][2] = startX;
        doors[3][3] = (endY - startY)/2 + 16;

        Entity room = new Room();

        room.add(new PositionPart(x, y));
        room.add(new WallPart(startX, startY, endX, endY));
        room.add(new DoorPart(doors, KeyPart.KeyColor.Silver, this.doorSpriteFile, room));
        room.add(new RenderPart(this.wallSpriteFile, room));

        room.setRadius(0);

        return room;
    }

    @Override
    public void stop(GameData gd, World world) {
        for (Entity room : world.getEntities(Room.class)) {
            world.removeEntity(room);
        }
    }

    @Override
    public String[] getSpritePaths() {
        return new String[]{wallSpriteFile, doorSpriteFile};
    }
}
