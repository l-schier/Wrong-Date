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
 * @author lasse
 */
public class DeadZonePart implements EntityPart {
    private float startX, startY, stopX, stopY;
    
    public DeadZonePart(float startX, float startY, float stopX, float stopY) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
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
    
    public float getStopX() {
        return stopX;
    }
    
    public float getStopY() {
        return stopY;
    }

}
