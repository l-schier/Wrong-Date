package dk.sdu.mmmi.map;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;

/**
 *
 * @author lasse
 */
public class RoomSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gd, World world) {
        Entity currentRoom = findCurrentRoom(gd, world);
        if (currentRoom == null) {
            Entity newRoom = RoomPlugin.createRoom(gd, gd.getCamX() - gd.getMenuWidth(), gd.getCamY());
            world.addEntity(newRoom);
        }
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
