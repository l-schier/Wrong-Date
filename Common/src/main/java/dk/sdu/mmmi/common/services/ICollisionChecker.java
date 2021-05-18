package dk.sdu.mmmi.common.services;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;

/**
 *
 * @author Anton
 */
public interface ICollisionChecker {

    boolean isPositionFree(World world, Entity me, float x, float y);
    boolean isInRoom(World world, Entity me, Entity room);
    void leavingRoom(GameData gameData, World world, Entity me, float newX, float newY);
    
}
