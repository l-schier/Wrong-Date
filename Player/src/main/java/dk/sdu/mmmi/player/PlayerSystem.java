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
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IInteractService;
import dk.sdu.mmmi.common.services.IItemService;
import dk.sdu.mmmi.common.services.ICollisionChecker;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerSystem implements IEntityProcessingService {

    private final List<IItemService> itemServices = new CopyOnWriteArrayList<>();
    private final List<IInteractService> interactServices = new CopyOnWriteArrayList<>();
    private ICollisionChecker collisionChecker;

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {

            MovingPart movingPart = player.getPart(MovingPart.class);
            PositionPart positionPart = player.getPart(PositionPart.class);
            InventoryPart inventoryPart = player.getPart(InventoryPart.class);
            LifePart lifePart = player.getPart(LifePart.class);

            float newX = positionPart.getX();
            float newY = positionPart.getY();
            float speed = movingPart.getSpeed();

            if (gameData.getKeys().isDown(RIGHT)) {
                newX += speed;
            } else if (gameData.getKeys().isDown(LEFT)) {
                newX -= speed;
            }
            if (gameData.getKeys().isDown(UP)) {
                newY += speed;
            } else if (gameData.getKeys().isDown(DOWN)) {
                newY -= speed;
            }

            if (this.collisionChecker == null) {
                movingPart.setRight(gameData.getKeys().isDown(RIGHT));
                movingPart.setLeft(gameData.getKeys().isDown(LEFT));
                movingPart.setUp(gameData.getKeys().isDown(UP));
                movingPart.setDown(gameData.getKeys().isDown(DOWN));
            } else if (collisionChecker.isPositionFree(world, player, newX, newY)) {
                collisionChecker.leavingRoom(gameData, world, player, newX, newY);
                movingPart.setRight(gameData.getKeys().isDown(RIGHT));
                movingPart.setLeft(gameData.getKeys().isDown(LEFT));
                movingPart.setUp(gameData.getKeys().isDown(UP));
                movingPart.setDown(gameData.getKeys().isDown(DOWN));
            } else {
                movingPart.setRight(false);
                movingPart.setLeft(false);
                movingPart.setUp(false);
                movingPart.setDown(false);
            }

            if (gameData.getKeys().isPressed(ENTER)) {
                for (IInteractService interactService : interactServices) {
                    interactService.interact(player, world);
                }
            }

            if (gameData.getKeys().isPressed(SPACE)) {
                if (inventoryPart.getWeapon() != null) {
                    IItemService weapon = (IItemService) inventoryPart.getWeapon();
                    weapon.useItem(player, gameData);
                }
            }

            movingPart.process(gameData, player);
            positionPart.process(gameData, player);
            inventoryPart.process(gameData, player);
            lifePart.process(gameData, player);

            int camX = gameData.getCamX();
            int camY = gameData.getCamY();
            float plaX = positionPart.getX();
            float plaY = positionPart.getY();
            int w = gameData.getDisplayWidth();
            int h = gameData.getDisplayHeight();
            int mw = gameData.getMenuWidth();

            if (plaX > camX - mw + (w / 2)) {
                camX += w;
            }
            if (plaX < camX - mw - (w / 2)) {
                camX -= w;
            }
            if (plaY > camY + (h / 2)) {
                camY += h;
            }
            if (plaY < camY - (h / 2)) {
                camY -= h;
            }

            gameData.setCamX(camX);
            gameData.setCamY(camY);

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
    }

    public void setItemService(IItemService itemService) {
        this.itemServices.add(itemService);
    }

    public void removeItemService(IItemService itemService) {
        this.itemServices.remove(itemService);
    }

    public void setInteractService(IInteractService interactService) {
        this.interactServices.add(interactService);
    }

    public void removeInteractService(IInteractService interactService) {
        this.interactServices.remove(interactService);
    }

    public void addCollisionChecker(ICollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
    }

    public void removeCollisionChecker(ICollisionChecker collisionChecker) {
        this.collisionChecker = null;
    }
}
