package dk.sdu.mmmi.player;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import static dk.sdu.mmmi.common.data.GameKeys.DOWN;
import static dk.sdu.mmmi.common.data.GameKeys.ENTER;
import static dk.sdu.mmmi.common.data.GameKeys.LEFT;
import static dk.sdu.mmmi.common.data.GameKeys.RIGHT;
import static dk.sdu.mmmi.common.data.GameKeys.SPACE;
import static dk.sdu.mmmi.common.data.GameKeys.UP;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;

public class PlayerSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {

            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);
            LifePart lifePart = player.getPart(LifePart.class);
            InteractPart interactPart = player.getPart(InteractPart.class);

            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setUp(gameData.getKeys().isDown(UP));
            movingPart.setDown(gameData.getKeys().isDown(DOWN));
            interactPart.setIsInteracting(gameData.getKeys().isDown(ENTER));
            
//            if (gameData.getKeys().isDown(SPACE)) {
//                useItem(player, gameData);
//            }

            movingPart.process(gameData, player);
            positionPart.process(gameData, player);
            lifePart.process(gameData, player);

            if (lifePart.getLife() <= 0) {
                world.removeEntity(player);
            }

            updateShape(player);
        }
    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = 3.1415f / 2;

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 5);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 5);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
        
        InventoryPart inventoryPart = entity.getPart(InventoryPart.class);
        if(inventoryPart.getInventory() != null){
            for(Entity item : inventoryPart.getInventory()){
                PositionPart pos = item.getPart(PositionPart.class);
                pos.setPosition(x, y);
            }
        }
        
    }
}
