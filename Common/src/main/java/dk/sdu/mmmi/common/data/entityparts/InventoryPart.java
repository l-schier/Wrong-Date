package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import java.util.ArrayList;

/**
 *
 * @author Jacob
 */
public class InventoryPart implements EntityPart {
    
    private ArrayList<Entity> inventory;
    private Entity weapon;
    
    public InventoryPart(){
        
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<Entity> getInventory() {
        return inventory;
    }

    public void addItem(Entity item) {
        if (item.getPart(DamagePart.class) != null){
            weapon = item;
        } else {
            inventory.add(item);
        }
    }
    
    public Entity getWeapon (){
        return weapon;
    }
    
}
