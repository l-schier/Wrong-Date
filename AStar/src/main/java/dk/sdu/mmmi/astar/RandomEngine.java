package dk.sdu.mmmi.astar;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.ICollisionChecker;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Anton
 */
public class RandomEngine {

    private ICollisionChecker collisionChecker;
    private final int randomStepsToTake = 50;
    private int randomcount = 0;
    private float randomPathX;
    private float randomPathY;

    public RandomEngine(ICollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
    }

    public void setCollisionEngine(ICollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
    }

    public PositionPart randomMove(World world, Entity me) {
        PositionPart positionPart = me.getPart(PositionPart.class);
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

        while (this.collisionChecker != null
                && !this.collisionChecker.isPositionFree(world, me, x, y)) {
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
        randomcount = randomStepsToTake;
    }
}
