package dk.sdu.mmmi.cbse.osgicollision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

public class Collider implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity e1 : world.getEntities()) {
            for (Entity e2 : world.getEntities()) {
                if (e1.getID().equals(e2.getID())) {
                    continue;
                }

                LifePart e1l = e1.getPart(LifePart.class);
                LifePart e2l = e2.getPart(LifePart.class);

                if (circleCollision(e1, e2)) {
                    e1l.setIsHit(true);
                    e2l.setIsHit(true);
                }
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
}
