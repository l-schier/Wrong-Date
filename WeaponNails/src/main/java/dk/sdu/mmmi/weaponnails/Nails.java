package dk.sdu.mmmi.weaponnails;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.services.IItemService;
import java.awt.Image;

/**
 *
 * @author Jacob
 */
public class Nails extends Entity implements IItemService {

    public void useItem(Entity shooter, GameData gameData) {
        DamagePart damage = this.getPart(DamagePart.class);
        damage.setWeaponUsed(true);
    }
}
