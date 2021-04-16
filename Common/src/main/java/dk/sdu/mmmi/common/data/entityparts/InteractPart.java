package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;

/**
 *
 * @author Jacob
 */
public class InteractPart implements EntityPart{
    //Is the player or enemy interacting?
    private boolean interacting = false;
    
    //Can you interact with the item or door?
    private boolean interactable;
    
    public InteractPart(boolean interactable){
        this.interactable = interactable;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the isInteracting
     */
    public boolean isInteracting() {
        return interacting;
    }

    /**
     * @param interacting the interacting to set
     */
    public void setIsInteracting(boolean interacting) {
        this.interacting = interacting;
    }

    /**
     * @return the interactable
     */
    public boolean isInteractable() {
        return interactable;
    }

    /**
     * @param interactable the interactable to set
     */
    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }
    
}
