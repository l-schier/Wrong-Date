package dk.sdu.mmmi.common.services;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;

/**
 *
 * @author Anton
 */
public interface IPathfinder {
    PositionPart findNextPosition(Entity me, GameData gameData, World world);
}
