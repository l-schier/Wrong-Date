package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.SightPart;
import dk.sdu.mmmi.common.services.ICollisionChecker;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IPathfinder;

/**
 *
 * @author Anton
 */
public class EnemySystem implements IEntityProcessingService {

    private IPathfinder aStarPathfinder;
    private ICollisionChecker collisionChecker;

    public void addPathfinder(IPathfinder aStarPathfinder) {
        this.aStarPathfinder = aStarPathfinder;
    }

    public void removePathfinder(IPathfinder aStarPathfinder) {
        this.aStarPathfinder = null;
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity enemy : world.getEntities(Enemy.class)) {

            PositionPart positionPart = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);
            SightPart sightPart = enemy.getPart(SightPart.class);
            LifePart lifePart = enemy.getPart(LifePart.class);

            if (aStarPathfinder != null) {
                // find next position
                PositionPart nextPos = aStarPathfinder.findNextPosition(enemy, gameData, world);
                // move to next position
                movingPart.setRight(positionPart.getX() < nextPos.getX());
                movingPart.setLeft(positionPart.getX() > nextPos.getX());
                movingPart.setUp(positionPart.getY() < nextPos.getY());
                movingPart.setDown(positionPart.getY() > nextPos.getY());
                if (collisionChecker != null) {
                    collisionChecker.leavingRoom(gameData, world, enemy, nextPos.getX(), nextPos.getY());
                }
            }

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);
            sightPart.process(gameData, enemy);
            lifePart.process(gameData, enemy);

            if (lifePart.getLife() <= 0) {
                world.removeEntity(enemy);
            }
        }
    }

    public void addCollisionChecker(ICollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
    }

    public void removeCollisionChecker(ICollisionChecker collisionChecker) {
        this.collisionChecker = null;
    }
}
