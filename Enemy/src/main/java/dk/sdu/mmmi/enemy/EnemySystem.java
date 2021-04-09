package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;

public class EnemySystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity enemy : world.getEntities(Enemy.class)) {

            PositionPart positionPart = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);
            LifePart lifePart = enemy.getPart(LifePart.class);

            Entity target = getTarget(enemy, world);
            if (target != null) {
                PositionPart targetPos = target.getPart(PositionPart.class);
                
                long startTime = System.nanoTime();
                PositionPart nextPos = AStar.search(100, positionPart, targetPos);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println("AStar: " + duration);

                movingPart.setRight(positionPart.getX() < nextPos.getX());
                movingPart.setLeft(positionPart.getX() > nextPos.getX());
                movingPart.setUp(positionPart.getY() < nextPos.getY());
                movingPart.setDown(positionPart.getY() > nextPos.getY());
            } else {
                movingPart.setRight(false);
                movingPart.setLeft(false);
                movingPart.setUp(false);
                movingPart.setDown(false);
            }

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);
            lifePart.process(gameData, enemy);

            if (lifePart.getLife() <= 0) {
                world.removeEntity(enemy);
            }

            updateShape(enemy);
        }
    }

    private Entity getTarget(Entity me, World world) {
        // find all entities
        for (Entity entity : world.getEntities()) {
            // player entities have lifeParts
            LifePart lifePart = entity.getPart(LifePart.class);
            if (entity != me && lifePart != null) {
                return entity;
            }
        }

        return null;
    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = 3.1415f / 2;

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 10);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 10);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }
}
