package dk.sdu.mmmi.commonweapon;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Jacob
 */
public interface WeaponSPI {
    Entity useWeapon(Entity e, GameData gameData);
}
