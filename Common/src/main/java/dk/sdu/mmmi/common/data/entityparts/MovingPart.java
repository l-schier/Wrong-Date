package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Anton
 */
public class MovingPart implements EntityPart {

    private boolean left, right, up, down;
    private final int speed;
    private float expiration;

    public MovingPart(int speed) {
        this.speed = speed;
        this.expiration = 0;
    }

    public int getSpeed() {
        return speed;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void stunFor(float expiration) {
        this.expiration = expiration;
    }

    private void reduceExpiration(float delta) {
        this.expiration -= delta;
        if (this.expiration < 0) {
            this.expiration = 0;
        }
    }

    @Override
    public void process(GameData gameData, Entity entity) {

        if (expiration > 0) {
            reduceExpiration(gameData.getDelta());
            return;
        }

        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();

        if (isRight()) {
            x += speed;
        } else if (isLeft()) {
            x -= speed;
        }

        if (isUp()) {
            y += speed;
        } else if (isDown()) {
            y -= speed;
        }

        positionPart.setX(x);
        positionPart.setY(y);
    }
    
        /**
     * @return the left
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * @return the right
     */
    public boolean isRight() {
        return right;
    }

    /**
     * @return the up
     */
    public boolean isUp() {
        return up;
    }

    /**
     * @return the down
     */
    public boolean isDown() {
        return down;
    }
}
