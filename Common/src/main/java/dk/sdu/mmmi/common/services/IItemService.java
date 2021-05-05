package dk.sdu.mmmi.common.services;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Jacob
 */
public interface IItemService {
    void useItem(Entity shooter, GameData gameData);
    String getDescription();
    Image getSprite();

}
