package dk.sdu.mmmi.common.services;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import java.awt.Image;

/**
 *
 * @author Jacob
 */
public interface IItemService {
    void useItem(Entity me, GameData gameData);
    String getDescription();
    Image getSprite();
}
