/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weaponheels;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.entityparts.StunPart;
import dk.sdu.mmmi.common.services.IItemService;
import java.awt.Image;

/**
 *
 * @author Kaan
 */
public class Heels extends Entity implements IItemService {
    
    @Override
    public void useItem(Entity me, GameData gameData) {
        StunPart stun = this.getPart(StunPart.class);
        stun.setIsStunning(true);
    }

    @Override
    public String getDescription() {
        return "Stun weapon";
    }

    @Override
    public Image getSprite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
