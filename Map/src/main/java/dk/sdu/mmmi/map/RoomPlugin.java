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
import dk.sdu.mmmi.common.data.entityparts.WallPart;
import dk.sdu.mmmi.common.services.IGamePluginService;

/**
 *
 * @author lasse
 */
public class RoomPlugin implements IGamePluginService {

    @Override
    public void start(GameData gd, World world) {
        Entity room = createRoom(gd);
        world.addEntity(room);
    }

    private Entity createRoom(GameData gameData) {
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float startX = 2;
        float startY = 2;
        float endX = gameData.getDisplayWidth();
        float endY = gameData.getDisplayHeight();
        float[][] doors = new float[4][4];

        doors[0][0] = (endX - startX)/2 - 15;
        doors[0][1] = startY;
        doors[0][2] = (endX - startX)/2 + 15;
        doors[0][3] = startY;
        
        doors[1][0] = endX;
        doors[1][1] = (endY - startY)/2 - 15;
        doors[1][2] = endX;
        doors[1][3] = (endY - startY)/2 + 15;
        
        doors[2][0] = (endX - startX)/2 - 15;
        doors[2][1] = endY;
        doors[2][2] = (endX - startX)/2 + 15;
        doors[2][3] = endY;
        
        // For the love of god this doesnt work and i dont know why
        doors[3][0] = startX;
        doors[3][1] = (endY - startY)/2 - 15;
        doors[3][2] = startX;
        doors[3][3] = (endY - startY)/2 + 15;

        Entity room = new Room();

        room.add(new PositionPart(x, y));
        room.add(new WallPart(startX, startY, endX, endY));
        room.add(new DoorPart(doors, KeyPart.KeyColor.Silver));

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
        return new String[]{};
    }
}
