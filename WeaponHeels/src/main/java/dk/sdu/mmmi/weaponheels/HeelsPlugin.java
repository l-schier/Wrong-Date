/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weaponheels;

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
public class HeelsPlugin implements IGamePluginService {
 @Override
    public void start(GameData gameData, World world) {
        Entity heels = createHeels(gameData);
        world.addEntity(heels);
    }

    public Entity createHeels(GameData gameData) {
        Entity heels = new Heels();
        Random random = new Random();

        float x = (float)random.nextInt(gameData.getDisplayWidth());
        float y = (float)random.nextInt(gameData.getDisplayHeight());
        float duration = 10;
        boolean interactable = true;
        heels.setRadius(8);

        heels.add(new InformationPart("testWeapon.png", "description.txt", heels));
        heels.add(new PositionPart(x, y));
        heels.add(new StunPart(duration));
        heels.add(new InteractPart(interactable));
        
        return heels;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity heels : world.getEntities(Heels.class)) {
            world.removeEntity(heels);
        }
    }
}
