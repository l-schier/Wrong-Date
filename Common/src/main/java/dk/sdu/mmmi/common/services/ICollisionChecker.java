package dk.sdu.mmmi.common.services;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.World;

public interface ICollisionChecker {

    boolean isPositionFree(World world, Entity me, float x, float y);
}
