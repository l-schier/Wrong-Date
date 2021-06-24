package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Kaan
 */
public class BlindPart implements EntityPart {
    
    private boolean isBlinding;
    private float blindDuration;
    
    public BlindPart(float blindDuration){
        this.blindDuration = blindDuration;
        this.isBlinding = false;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isBlinding() {
        return isBlinding;
    }

    public void setIsBlinding(boolean isBlinding) {
        this.isBlinding = isBlinding;
    }

    public float getBlindDuration() {
        return blindDuration;
    }

    public void setBlindDuration(float blindDuration) {
        this.blindDuration = blindDuration;
    }
}
