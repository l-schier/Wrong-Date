package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author lasse
 */
public class DoorPart extends RenderPart {

    private float[][] doors;
    private KeyPart.KeyColor lockColor;

    public DoorPart(float[][] doors, KeyPart.KeyColor lockColor, String spriteFile, Object component) {
        super(spriteFile, component);
        this.doors = doors;
        this.lockColor = lockColor;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public float[][] getDoors() {
        return doors;
    }

    public KeyPart.KeyColor getLockColor() {
        return lockColor;
    }
}
