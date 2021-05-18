package dk.sdu.mmmi.collision;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.BlindPart;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.data.entityparts.DoorPart;
import dk.sdu.mmmi.common.data.entityparts.EnemyPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.WallPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.KeyPart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.SightPart;
import dk.sdu.mmmi.common.data.entityparts.StunPart;
import dk.sdu.mmmi.common.services.ICollisionChecker;
import java.util.ArrayList;
import dk.sdu.mmmi.common.services.IEntityPostProcessingService;

/**
 *
 * @author Anton
 */
public class Collider implements IEntityPostProcessingService, ICollisionChecker {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity e1 : world.getEntities()) {
            for (Entity e2 : world.getEntities()) {
                if (e1.getID().equals(e2.getID())) {
                    continue;
                }
                if (e2.getPart(InventoryPart.class) != null) {
                    InventoryPart inventoryPart = e2.getPart(InventoryPart.class);
                    if (inventoryPart.getWeapon() != null && inventoryPart.getWeapon().equals(e1)) {
                        continue;
                    }
                }

                if (e1.getPart(DamagePart.class) != null && e2.getPart(LifePart.class) != null) {
                    DamagePart e1d = e1.getPart(DamagePart.class);
                    LifePart e2l = e2.getPart(LifePart.class);
                    if (circleCollision(e1, e2) && e1d.isWeaponUsed()) {
                        e2l.takeLife(e1d.getDamage());
                    }
                }
                
                if (e1.getPart(BlindPart.class) != null && e2.getPart(SightPart.class) != null) {
                    BlindPart e1b = e1.getPart(BlindPart.class);
                    SightPart e2s = e2.getPart(SightPart.class);
                    if (circleCollision(e1, e2) && e1b.isBlinding()) {
                        e2s.blindFor(e1b.getBlindDuration());
                    }
                }
                
                if (e1.getPart(StunPart.class) != null && e2.getPart(MovingPart.class) != null) {
                    StunPart e1s = e1.getPart(StunPart.class);
                    MovingPart e2m = e2.getPart(MovingPart.class);
                    if (circleCollision(e1, e2) && e1s.isStunning()) {
                        e2m.stunFor(e1s.getStunDuration());
                    }
                }
            }
            
            // Disable all WeaponParts
            if (e1.getPart(DamagePart.class) != null) {
                DamagePart e1d = e1.getPart(DamagePart.class);
                e1d.setWeaponUsed(false);
            }
            if (e1.getPart(BlindPart.class) != null) {
                BlindPart e1b = e1.getPart(BlindPart.class);
                e1b.setIsBlinding(false);
            }
            if (e1.getPart(StunPart.class) != null) {
                StunPart e1s = e1.getPart(StunPart.class);
                e1s.setIsStunning(false);
            }
        }
    }

    private boolean circleCollision(Entity entity1, Entity entity2) {
        PositionPart e1p = entity1.getPart(PositionPart.class);
        PositionPart e2p = entity2.getPart(PositionPart.class);

        float dx = e1p.getX() - e2p.getX();
        float dy = e1p.getY() - e2p.getY();

        // a^2 + b^2 = c^2
        // c = sqrt(a^2 + b^2)
        double distance = Math.sqrt(dx * dx + dy * dy);

        // if radius overlap
        if (distance < entity1.getRadius() + entity2.getRadius()) {
            // Collision!
            return true;
        }

        return false;
    }

    @Override
    public boolean isPositionFree(World world, Entity me, float x, float y) {
        InventoryPart inventory = me.getPart(InventoryPart.class);
        ArrayList<KeyPart> keys = new ArrayList<>();
        if (inventory != null) {
            keys = inventory.getKeyParts();
        }

        for (Entity e : world.getEntities()) {
            if (e == me) {
                continue;
            }

            DoorPart doorPart = e.getPart(DoorPart.class);
            WallPart wall = e.getPart(WallPart.class);

            if (wall != null && doorPart != null) {

                float[][] doors = doorPart.getDoors();
                PositionPart pos = me.getPart(PositionPart.class);

                // entity is inside the box of walls!
                if (wall.getStartX() <= pos.getX() && pos.getX() <= wall.getEndX()
                        && wall.getStartY() <= pos.getY() && pos.getY() <= wall.getEndY()) {

                    // is x:y on the outside of the wall?
                    boolean wallBool = x <= wall.getStartX() || wall.getEndX() <= x
                            || y <= wall.getStartY() || wall.getEndY() <= y;
                    boolean doorBool = false;

                    // if so, did x:y get outside through a door?
                    for (float[] door : doors) {
                        if (door[0] == door[2]) { // x == x, so it is a left or right door
                            doorBool = doorBool || door[1] <= y && y <= door[3];
                        } else if (door[1] == door[3]) { // y == y, so it is a top or bottom door
                            doorBool = doorBool || door[0] <= x && x <= door[2];
                        }
                    }

                    boolean hasKey = false;
                    EnemyPart enemy = me.getPart(EnemyPart.class);
                    // enemy has a master key
                    if (enemy != null) {
                        hasKey = true;
                    } else {
                        // do we have a key matching the lock color?
                        for (KeyPart key : keys) {
                            if (key.getColor() == doorPart.getLockColor()) {
                                hasKey = true;
                            }
                        }
                    }

                    if (wallBool) {
                        return doorBool && hasKey;
                    }
                }
            }
            // Rest of collison
        }

        float deadZoneStartX = 100;
        float deadZoneStopX = 200;
        float deadZoneStartY = 100;
        float deadZoneStopY = 200;

        if (deadZoneStartX <= x && x <= deadZoneStopX && deadZoneStartY <= y && y <= deadZoneStopY) {
            return false;
        }

        return true;
    }
}
