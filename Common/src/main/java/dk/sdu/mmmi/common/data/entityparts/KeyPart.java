package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Anton
 */
public class KeyPart implements EntityPart {

    private KeyColor color;

    public enum KeyColor {
        Bronze,
        Silver,
        Gold
    }

    public KeyPart(KeyColor color) {
        this.color = color;
    }

    public KeyColor getColor() {
        return this.color;
    }

    public void use() {

    }

    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
