package dk.sdu.mmmi.collision;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.data.entityparts.DoorPart;
import dk.sdu.mmmi.common.data.entityparts.KeyPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.WallPart;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Anton
 */
public class ColliderTest {

    public ColliderTest() {
    }

    @Test
    public void testProcess() {
        System.out.println("process");
        GameData gameData = new GameData();
        World world = new World();

        Entity player = new Entity();
        player.setRadius(10);
        player.add(new PositionPart(10, 10));
        DamagePart dp = new DamagePart(5);
        dp.setWeaponUsed(true);
        player.add(dp);
        world.addEntity(player);

        Entity enemy = new Entity();
        enemy.setRadius(10);
        enemy.add(new PositionPart(10, 10));
        enemy.add(new LifePart(10));
        world.addEntity(enemy);

        Collider instance = new Collider();
        instance.process(gameData, world);

        LifePart eLife = enemy.getPart(LifePart.class);

        assertNotEquals(10, eLife.getLife());
    }

    @Test
    public void testIsPositionFree() {
        System.out.println("isPositionFree");
        World world = new World();

        Entity room = new Entity();
        room.add(new WallPart(0, 0, 10, 10));
        room.add(new DoorPart(new float[0][0], KeyPart.KeyColor.Silver, "door.png", room));
        world.addEntity(room);

        Entity me = new Entity();
        me.add(new PositionPart(1, 1));
        world.addEntity(me);

        Collider instance = new Collider();

        float x = 0.0F;
        float y = 0.0F;
        boolean expResult = false;
        boolean result = instance.isPositionFree(world, me, x, y);
        assertEquals(expResult, result);

        x = 2.0F;
        y = 2.0F;
        expResult = true;
        result = instance.isPositionFree(world, me, x, y);
        assertEquals(expResult, result);
    }
}
