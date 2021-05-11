/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Kaan
 */
public class StunPart implements EntityPart {
    
    private boolean isStunning;
    private float stunDuration;
    
    public StunPart(float stunDuration){
        this.stunDuration = stunDuration;
        this.isStunning = false;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isIsStunning() {
        return isStunning;
    }

    public void setIsStunning(boolean isStunning) {
        this.isStunning = isStunning;
    }

    public float getStunDuration() {
        return stunDuration;
    }

    public void setStunDuration(float stunDuration) {
        this.stunDuration = stunDuration;
    }
    
}
