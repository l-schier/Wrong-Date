package dk.sdu.mmmi.map;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DoorPart;
import dk.sdu.mmmi.common.data.entityparts.KeyPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.data.entityparts.HelpPart;
import dk.sdu.mmmi.common.data.entityparts.WallPart;
import dk.sdu.mmmi.common.services.IGamePluginService;

/**
 *
 * @author lasse
 */
public class RoomPlugin implements IGamePluginService {

    private static final String wallSpriteFile = "wall.png";
    private static final String doorSpriteFile = "door.png";

    @Override
    public void start(GameData gd, World world) {
        Entity room = createRoom(gd);
        world.addEntity(room);
    }

    private Entity createRoom(GameData gameData) {
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        return createRoom(gameData, x, y);
    }

    protected static Entity createRoom(GameData gameData, float x, float y) {
        float wallWidth = 32f;
        float startX = x - gameData.getDisplayWidth() / 2 + wallWidth;
        float startY = y - gameData.getDisplayHeight() / 2 + wallWidth;
        float endX = x + gameData.getDisplayWidth() / 2 - wallWidth;
        float endY = y + gameData.getDisplayHeight() / 2 - wallWidth;
        float[][] doors = new float[4][4];

        doors[0][0] = (((endX) - (startX)) / 2) + startX - wallWidth / 2f;
        doors[0][1] = startY;
        doors[0][2] = doors[0][0] + wallWidth;
        doors[0][3] = startY;

        doors[1][0] = endX;
        doors[1][1] = (((endY) - (startY)) / 2) + startY - wallWidth / 2f;
        doors[1][2] = endX;
        doors[1][3] = doors[1][1] + wallWidth;

        doors[2][0] = (((endX) - (startX)) / 2) + startX - wallWidth / 2f;
        doors[2][1] = endY;
        doors[2][2] = doors[2][0] + wallWidth;
        doors[2][3] = endY;

        doors[3][0] = startX;
        doors[3][1] = (((endY) - (startY)) / 2) + startY - wallWidth / 2f;
        doors[3][2] = startX;
        doors[3][3] = doors[3][1] + wallWidth;

        Entity room = new Room();

        room.add(new PositionPart(x, y));
        room.add(new WallPart(startX, startY, endX, endY));
        room.add(new DoorPart(doors, KeyPart.KeyColor.Silver, doorSpriteFile, room));
        room.add(new RenderPart(wallSpriteFile, room));
        room.add(new HelpPart("Map.txt", room));

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
