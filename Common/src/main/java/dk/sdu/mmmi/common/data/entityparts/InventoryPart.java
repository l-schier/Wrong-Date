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
        inventory = new ArrayList<Entity>();
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        PositionPart playerPos = entity.getPart(PositionPart.class);
        if(this.getWeapon() != null){
            PositionPart pos = this.getWeapon().getPart(PositionPart.class);
            pos.setPosition(playerPos.getX() + 3f, playerPos.getY() + 2f);
        }
        for(Entity item : this.getInventory()){
            PositionPart pos = item.getPart(PositionPart.class);
            pos.setPosition(playerPos.getX(), playerPos.getY());
        }
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
