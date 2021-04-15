package dk.sdu.mmmi.commonitem;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import java.awt.Image;


/**
 *
 * @author Jacob
 */

public interface ItemSPI {
    void useItem(Entity shooter, GameData gameData);
    String getDescription();
    Image getSprite();
}
