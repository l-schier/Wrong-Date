package dk.sdu.mmmi.commoninteract;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Jacob
 */
public interface InteractSPI {
    void interact(Entity user, GameData gameData);
}
