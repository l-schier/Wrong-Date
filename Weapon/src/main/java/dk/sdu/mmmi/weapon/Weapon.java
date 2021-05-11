package dk.sdu.mmmi.weapon;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.services.IItemService;
import java.awt.Image;

/**
 *
 * @author Jacob
 */
public class Weapon extends Entity implements IItemService{
    
    public void useItem(Entity shooter, GameData gameData) {
        DamagePart damage = this.getPart(DamagePart.class);
        damage.setWeaponUsed(true);
    }

    @Override
    public String getDescription() {
        return "Melee weapon";
    }

    @Override
    public Image getSprite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
