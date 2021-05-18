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

        if (right) {
            x += speed;
        } else if (left) {
            x -= speed;
        }

        if (up) {
            y += speed;
        } else if (down) {
            y -= speed;
        }
        if (x > gameData.getCamX() + (gameData.getDisplayWidth() / 2f) - gameData.getMenuWidth() - 32f) {
            x += 64;
        } else if (x <  32f + gameData.getCamX() - (gameData.getDisplayWidth() / 2f) - gameData.getMenuWidth()) {
            x -= 64;
        }
        if (y > gameData.getCamY() + gameData.getDisplayHeight() / 2f - 32f) {
            y += 64;
        } else if (y < gameData.getCamY() - gameData.getDisplayHeight() / 2f + 32f) {
            y -= 64;
        }
        positionPart.setX(x);
        positionPart.setY(y);
    }
}
