/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weaponfoundation;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.BlindPart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Kaan
 */
public class FoundationPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Entity foundation = createFoundation(gameData);
        world.addEntity(foundation);
    }

    public Entity createFoundation(GameData gameData) {
        Entity foundation = new Foundation();
        Random random = new Random();

        float x = (float) random.nextInt(gameData.getDisplayWidth());
        float y = (float) random.nextInt(gameData.getDisplayHeight());
        float duration = 10;
        boolean interactable = true;
        foundation.setRadius(8);

        //foundation.add(new InformationPart("testWeapon.png", "description.txt", foundation));
        foundation.add(new PositionPart(x, y));
        foundation.add(new BlindPart(duration));
        foundation.add(new InteractPart(interactable));

        return foundation;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity foundation : world.getEntities(Foundation.class)) {
            world.removeEntity(foundation);
        }
    }

    @Override
    public String[] getSpritePaths() {
        return new String[]{};
    }
}
