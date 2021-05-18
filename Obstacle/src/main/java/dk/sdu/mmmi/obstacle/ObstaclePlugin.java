/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.obstacle;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DeadZonePart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author lasse
 */
public class ObstaclePlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Entity obstacle = createObstacle(gameData, 10f, 10f);
        world.addEntity(obstacle);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity obstacle : world.getEntities(Obstacle.class)) {
            world.removeEntity(obstacle);
        }
    }

    private Entity createObstacle(GameData gameData, float xSize, float ySize) {
        Random r = new Random();
        float x = gameData.getDisplayWidth() * r.nextFloat();
        float y = gameData.getDisplayHeight() * r.nextFloat();
        float startX = x - xSize;
        float startY = y - ySize;
        float stopX = x + xSize;
        float stopY = y + ySize;
        
        Entity obstacle = new Entity();

        obstacle.add(new PositionPart(x, y));
        obstacle.add(new DeadZonePart(startX, startY, stopX, stopY));
        System.out.println(x);
        obstacle.setRadius(0);
        
        return obstacle;
    }

}
