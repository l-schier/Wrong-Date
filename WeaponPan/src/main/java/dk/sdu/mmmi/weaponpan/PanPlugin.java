/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weaponpan;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.InformationPart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.StunPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Kaan
 */
public class PanPlugin implements IGamePluginService {
     @Override
    public void start(GameData gameData, World world) {
        Entity pan = createPan(gameData);
        world.addEntity(pan);
    }

    public Entity createPan(GameData gameData) {
        Entity pan = new Pan();
        Random random = new Random();

        float x = (float)random.nextInt(gameData.getDisplayWidth());
        float y = (float)random.nextInt(gameData.getDisplayHeight());
        float duration = 10;
        boolean interactable = true;
        pan.setRadius(8);

        pan.add(new InformationPart("testWeapon.png", "description.txt", pan));
        pan.add(new PositionPart(x, y));
        pan.add(new StunPart(duration));
        pan.add(new InteractPart(interactable));
        
        return pan;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity pan : world.getEntities(Pan.class)) {
            world.removeEntity(pan);
        }
    }
}
