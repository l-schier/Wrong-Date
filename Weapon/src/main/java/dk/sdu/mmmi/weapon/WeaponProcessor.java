package dk.sdu.mmmi.weapon;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commoninteract.InteractSPI;
import dk.sdu.mmmi.commonitem.ItemSPI;
import java.awt.Image;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author Jacob
 */
public class WeaponProcessor implements IEntityProcessingService, ItemSPI, InteractSPI {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(Weapon.class)) {

            PositionPart positionPart = weapon.getPart(PositionPart.class);
            DamagePart damagePart = weapon.getPart(DamagePart.class);

            setShape(weapon);
        }
    }

    
    public void setShape(Entity entity) {
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

    public void useItem(Entity shooter, GameData gameData) {
        InventoryPart inventory = shooter.getPart(InventoryPart.class);
        DamagePart damage = inventory.getWeapon().getPart(DamagePart.class);
        damage.setWeaponUsed(true);
        
    }

    @Override
    public String getDescription() {
        return "Melee weapon";
    }

    @Override
    public Image getSprite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void interact(Entity user, World world) {
        for(Entity weapon : world.getEntities(Weapon.class)){
            if(user.checkCollision(weapon)){
                InventoryPart inventory = user.getPart(InventoryPart.class);
                inventory.addItem(weapon);
            }
        }
    }

}
