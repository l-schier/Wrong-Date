package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Anton
 */
public class SightPart implements EntityPart {

    private float expiration;
    private float sightLimit;

    public SightPart(float sightLimit) {
        this.expiration = 0;
        this.sightLimit = sightLimit;
    }

    public float getSightLimit() {
        if (expiration > 0) {
            return 0;
        }

        return this.sightLimit;
    }

    public void blindFor(float expiration) {
        this.expiration = expiration;
    }

    private void reduceExpiration(float delta) {
        this.expiration -= delta;
        if (this.expiration < 0) {
            this.expiration = 0;
        }
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        if (expiration > 0) {
            reduceExpiration(gameData.getDelta());
        }
    }
}
