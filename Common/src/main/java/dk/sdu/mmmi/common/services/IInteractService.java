package dk.sdu.mmmi.common.services;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.World;

/**
 *
 * @author Jacob
 */
public interface IInteractService {
    void interact(Entity user, World world);
}
