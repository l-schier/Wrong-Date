package dk.sdu.mmmi.astar;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PlayerPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.ICollisionChecker;
import dk.sdu.mmmi.common.services.IPathfinder;

/**
 *
 * @author Anton
 */
public class AStarPathfinder implements IPathfinder {

    private AStarEngine aStar = new AStarEngine(null);
    private RandomEngine random = new RandomEngine(null);

    public void addCollisionChecker(ICollisionChecker collisionChecker) {
        this.aStar.setCollisionEngine(collisionChecker);
        this.random.setCollisionEngine(collisionChecker);
    }

    public void removeCollisionChecker(ICollisionChecker collisionChecker) {
        this.aStar.setCollisionEngine(null);
        this.random.setCollisionEngine(null);
    }

    @Override
    public PositionPart findNextPosition(Entity me, GameData gameData, World world) {
        PositionPart nextPos = null;
        
        Entity target = getTarget(me, world);
        if (target != null) {
            PositionPart targetPos = target.getPart(PositionPart.class);
            if (this.aStar != null) {
                nextPos = this.aStar.search(world, me, targetPos);
            }
        }

        if (random != null && nextPos == null) {
            nextPos = random.randomMove(world, me);
        }

        return nextPos;
    }

    private Entity getTarget(Entity me, World world) {
        // find all entities
        for (Entity entity : world.getEntities()) {
            // player entities have playerParts
            PlayerPart playerPart = entity.getPart(PlayerPart.class);
            if (entity != me && playerPart != null) {
                return entity;
            }
        }

        return null;
    }
}
