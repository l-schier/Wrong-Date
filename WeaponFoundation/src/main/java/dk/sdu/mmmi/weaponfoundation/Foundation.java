package dk.sdu.mmmi.weaponfoundation;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.entityparts.BlindPart;
import dk.sdu.mmmi.common.services.IItemService;

/**
 *
 * @author Kaan
 */
public class Foundation extends Entity implements IItemService {

    public void useItem(Entity shooter, GameData gameData) {
        BlindPart blind = this.getPart(BlindPart.class);
        blind.setIsBlinding(true);
    }
}
