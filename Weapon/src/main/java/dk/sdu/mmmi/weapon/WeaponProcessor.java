package dk.sdu.mmmi.weapon;


import com.badlogic.gdx.scenes.scene2d.ui.Image;
import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.data.entityparts.InformationPart;
import dk.sdu.mmmi.common.data.entityparts.InteractPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IInteractService;
import dk.sdu.mmmi.common.services.IItemService;


import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author Jacob
 */
public class WeaponProcessor implements IEntityProcessingService, IItemService, IInteractService {
    

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

        shapex[1] = (float) (x);
        shapey[1] = (float) (y);

        shapex[2] = (float) (x);
        shapey[2] = (float) (y);

        shapex[3] = (float) (x);
        shapey[3] = (float) (y);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }

    public void useItem(Entity shooter, GameData gameData) {
//        InventoryPart inventory = shooter.getPart(InventoryPart.class);
//        DamagePart damage = inventory.getWeapon().getPart(DamagePart.class);
//        damage.setWeaponUsed(true);

        InventoryPart inventory = shooter.getPart(InventoryPart.class);
        Entity weapon = inventory.getWeapon();
        if (weapon != null) {
            DamagePart damage = weapon.getPart(DamagePart.class);
            damage.setWeaponUsed(true);
        }

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
        for (Entity weapon : world.getEntities(Weapon.class)) {
            InteractPart interact = weapon.getPart(InteractPart.class);
            if (user.circleCollision(weapon) && interact.isInteractable()) {
                InventoryPart inventory = user.getPart(InventoryPart.class);
                inventory.addItem(weapon);
                interact.setInteractable(false);
            }
        }
    }

    @Override
    public Image getImage(World world) {
        InformationPart imagePart;
        
        for (Entity weapon : world.getEntities(Weapon.class)) {
            
            imagePart = weapon.getPart(InformationPart.class);
            return imagePart.getImage();
       
        }
        
        return null;


    }

  

}
