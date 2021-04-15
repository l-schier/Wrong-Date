package dk.sdu.mmmi.weapon;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DamagePart;
import dk.sdu.mmmi.common.data.entityparts.LifePart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.TimerPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonitem.ItemSPI;
import java.awt.Image;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author Jacob
 */
public class WeaponProcessor implements IEntityProcessingService, ItemSPI {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(Weapon.class)) {

            PositionPart positionPart = weapon.getPart(PositionPart.class);
            MovingPart movingPart = weapon.getPart(MovingPart.class);
            TimerPart timerPart = weapon.getPart(TimerPart.class);
            DamagePart damagePart = weapon.getPart(DamagePart.class);

            
            movingPart.setUp(true);
            if (timerPart.getExpiration() < 0) {
                world.removeEntity(weapon);
            }
            timerPart.process(gameData, weapon);
            movingPart.process(gameData, weapon);
            positionPart.process(gameData, weapon);

            //setShape(weapon);
        }
    }

    
    public void setShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        
        //float radians = positionPart.getRadians();

        shapex[0] = x;
        shapey[0] = y;
        shapex[1] = x + 1;
        shapex[1] = y + 1;

        //shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5));
        //shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5));

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }

    @Override
    public Entity useWeapon(Entity shooter, GameData gameData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Image getSprite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
