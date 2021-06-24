package dk.sdu.mmmi.weaponheels;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.entityparts.StunPart;
import dk.sdu.mmmi.common.services.IItemService;

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
}
