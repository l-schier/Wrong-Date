package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

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
        }

        if (left) {
            x -= speed;
        }

        if (up) {
            y += speed;
        }

        if (down) {
            y -= speed;
        }

        // set position
        if (x > gameData.getDisplayWidth()) {
            x = 0;
        } else if (x < 0) {
            x = gameData.getDisplayWidth();
        }
        
        if (y > gameData.getDisplayHeight()) {
            y = 0;
        } else if (y < 0) {
            y = gameData.getDisplayHeight();
        }

        positionPart.setX(x);
        positionPart.setY(y);
    }
}
