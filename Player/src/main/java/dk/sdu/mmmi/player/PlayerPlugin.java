package dk.sdu.mmmi.player;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {

    public PlayerPlugin() {

    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity player = createPlayer(gameData);
        world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData) {
        float x = gameData.getDisplayWidth() - 50;
        float y = gameData.getDisplayHeight() - 50;

        Entity player = new Player();

        player.add(new LifePart(1));
        player.add(new MovingPart(2));
        player.add(new PositionPart(x, y));

        player.setRadius(8);

        return player;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity enemy : world.getEntities(Player.class)) {
            world.removeEntity(enemy);
        }
    }
}
