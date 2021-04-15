package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.services.ICollisionChecker;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import java.util.ArrayList;
import java.util.Collections;

public class EnemySystem implements IEntityProcessingService {

    private ICollisionChecker collisionChecker;
    private AStar aStar;

    private int randomcount = 0;
    private float randomPathX;
    private float randomPathY;

    public void addCollisionChecker(ICollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
        this.aStar = new AStar(5, collisionChecker);
    }

    public void removeCollisionChecker(ICollisionChecker collisionChecker) {
        this.collisionChecker = null;
        this.aStar = null;
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity enemy : world.getEntities(Enemy.class)) {

            PositionPart positionPart = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);
            LifePart lifePart = enemy.getPart(LifePart.class);

            Entity target = getTarget(enemy, world);
            PositionPart nextPos = null;
            if (target != null) {
                PositionPart targetPos = target.getPart(PositionPart.class);
                if (this.aStar != null) {
                    nextPos = this.aStar.search(200, enemy, targetPos);
                }
            }

            if (nextPos == null) {
                nextPos = randomMove(positionPart);
            }

            movingPart.setRight(positionPart.getX() < nextPos.getX());
            movingPart.setLeft(positionPart.getX() > nextPos.getX());
            movingPart.setUp(positionPart.getY() < nextPos.getY());
            movingPart.setDown(positionPart.getY() > nextPos.getY());

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);
            lifePart.process(gameData, enemy);

            if (lifePart.getLife() <= 0) {
                world.removeEntity(enemy);
            }

            updateShape(enemy);
        }
    }

    private PositionPart randomMove(PositionPart positionPart) {
        ArrayList<PositionPart> directions = new ArrayList<PositionPart>();
        // go north
        directions.add(new PositionPart(0, 0 + 1));
        // go south
        directions.add(new PositionPart(0, 0 - 1));
        // go east
        directions.add(new PositionPart(0 + 1, 0));
        // go west
        directions.add(new PositionPart(0 - 1, 0));
        // go north east
        directions.add(new PositionPart(0 + 1, 0 + 1));
        // go north west
        directions.add(new PositionPart(0 - 1, 0 + 1));
        // go south east
        directions.add(new PositionPart(0 + 1, 0 - 1));
        // go south west
        directions.add(new PositionPart(0 - 1, 0 - 1));

        if (randomcount == 0) {
            setNewcourse(directions);
        }
        
        float x = positionPart.getX() + randomPathX;
        float y = positionPart.getY() + randomPathY;

        while (!this.collisionChecker.isPositionFree(x, y)) {
            setNewcourse(directions);
            x = positionPart.getX() + randomPathX;
            y = positionPart.getY() + randomPathY;
        }

        positionPart.setX(x);
        positionPart.setY(y);
        randomcount -= 1;

        return positionPart;
    }

    private void setNewcourse(ArrayList<PositionPart> directions) {
        Collections.shuffle(directions);
        randomPathX = directions.get(0).getX();
        randomPathY = directions.get(0).getY();
        randomcount = 20;
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
