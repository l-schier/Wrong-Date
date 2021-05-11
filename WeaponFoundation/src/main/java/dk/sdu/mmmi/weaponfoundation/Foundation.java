/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weaponfoundation;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.entityparts.BlindPart;
import dk.sdu.mmmi.common.services.IItemService;
import java.awt.Image;

/**
 *
 * @author Kaan
 */
public class Foundation extends Entity implements IItemService {
    
    public void useItem(Entity shooter, GameData gameData) {
        BlindPart blind = this.getPart(BlindPart.class);
        blind.setIsBlinding(true);
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
