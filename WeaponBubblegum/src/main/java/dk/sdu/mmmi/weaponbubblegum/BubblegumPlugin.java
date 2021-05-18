/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weaponbubblegum;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.BlindPart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author Kaan
 */
public class BubblegumPlugin implements IGamePluginService{
        @Override
    public void start(GameData gameData, World world) {
        Entity bubblegum = createBubblegum(gameData);
        world.addEntity(bubblegum);
    }

    public Entity createBubblegum(GameData gameData) {
        Entity bubblegum = new Bubblegum();
        Random random = new Random();

        float x = (float)random.nextInt(gameData.getDisplayWidth());
        float y = (float)random.nextInt(gameData.getDisplayHeight());
        float duration = 10;
        boolean interactable = true;
        bubblegum.setRadius(8);

        //bubblegum.add(new InformationPart("testWeapon.png", "description.txt", bubblegum));
        bubblegum.add(new PositionPart(x, y));
        bubblegum.add(new BlindPart(duration));
        bubblegum.add(new InteractPart(interactable));
        
        return bubblegum;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity bubblegum : world.getEntities(Bubblegum.class)) {
            world.removeEntity(bubblegum);
        }
    }
}
