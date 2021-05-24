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
import dk.sdu.mmmi.common.services.IEntityProcessingService;

/**
 *
 * @author lasse
 */
public class ObstacleSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity obstacle : world.getEntities(Obstacle.class)) {
            updateShape(obstacle);
        }
    }

    private void updateShape(Entity obstacle) {
        float[] shapex = obstacle.getShapeX();
        float[] shapey = obstacle.getShapeY();

        DeadZonePart deadzone = obstacle.getPart(DeadZonePart.class);
        
        shapex[0] = deadzone.getStartX();
        shapey[0] = deadzone.getStartY();

        shapex[1] = deadzone.getStopX();
        shapey[1] = deadzone.getStartY();

        shapex[2] = deadzone.getStopX();
        shapey[2] = deadzone.getStopY();

        shapex[3] = deadzone.getStartX();
        shapey[3] = deadzone.getStopY();
        
        obstacle.setShapeX(shapex);
        obstacle.setShapeY(shapey);
    }

}
