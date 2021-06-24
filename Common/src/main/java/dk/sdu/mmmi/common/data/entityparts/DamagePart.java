package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Jacob
 */
public class DamagePart implements EntityPart {
    
    private int damage;
    private boolean weaponUsed;
    
    public DamagePart (int damage) {
        this.damage = damage;
        weaponUsed = false;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @param damage the damage to set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * @return the weaponUsed
     */
    public boolean isWeaponUsed() {
        return weaponUsed;
    }

    /**
     * @param weaponUsed the weaponUsed to set
     */
    public void setWeaponUsed(boolean weaponUsed) {
        this.weaponUsed = weaponUsed;
    }
}
