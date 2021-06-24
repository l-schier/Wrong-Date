package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author lasse
 */
public class WallPart implements EntityPart{
    private float startX, startY, endX, endY;
    
    public WallPart(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    
    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public float getStartX() {
        return startX;
    }
    
    public float getStartY() {
        return startY;
    }
    
    public float getEndX() {
        return endX;
    }
    
    public float getEndY() {
        return endY;
    }    
}
