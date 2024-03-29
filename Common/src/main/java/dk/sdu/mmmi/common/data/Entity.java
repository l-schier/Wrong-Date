package dk.sdu.mmmi.common.data;

import dk.sdu.mmmi.common.data.entityparts.EntityPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Entity implements Serializable {

    private final UUID ID = UUID.randomUUID();
    
    private float[] color = new float[3];
    private float[] shapeX = new float[4];
    private float[] shapeY = new float[4];
    
    private String spriteFile;
    
    private float radius;
    private float boundingCircleX;
    private float boundingCircleY;
    
    private Map<Class, EntityPart> parts;

    public Entity() {
        parts = new ConcurrentHashMap<>();
    }

    public void add(EntityPart part) {
        parts.put(part.getClass(), part);
    }

    public void remove(Class partClass) {
        parts.remove(partClass);
    }

    public <E extends EntityPart> E getPart(Class partClass) {
        return (E) parts.get(partClass);
    }

    public void setRadius(float r) {
        this.radius = r;
    }

    public float getRadius() {
        return radius;
    }

    public String getID() {
        return ID.toString();
    }

    public String getSpriteFile() {
        return spriteFile;
    }

    public void setSpriteFile(String spriteFile) {
        this.spriteFile = spriteFile;
    }

    public float[] getShapeX() {
        return shapeX;
    }

    public void setShapeX(float[] shapeX) {
        this.shapeX = shapeX;
    }

    public float[] getShapeY() {
        return shapeY;
    }

    public void setShapeY(float[] shapeY) {
        this.shapeY = shapeY;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public float getBoundingCircleX() {
        return boundingCircleX;
    }

    public void setBoundingCircleX(float boundingCircleX) {
        this.boundingCircleX = boundingCircleX;
    }

    public float getBoundingCircleY() {
        return boundingCircleY;
    }

    public void setBoundingCircleY(float boundingCircleY) {
        this.boundingCircleY = boundingCircleY;
    }
    
    public boolean checkCollision(Entity entity) {
        float dx = this.boundingCircleX - entity.boundingCircleX;
        float dy = this.boundingCircleY - entity.boundingCircleY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance < this.radius + entity.radius) {
            return true;
        }
        return false;
    }
    
    public boolean circleCollision(Entity entity) {
        PositionPart e1p = this.getPart(PositionPart.class);
        PositionPart e2p = entity.getPart(PositionPart.class);

        float dx = e1p.getX() - e2p.getX();
        float dy = e1p.getY() - e2p.getY();

        // a^2 + b^2 = c^2
        // c = sqrt(a^2 + b^2)
        double distance = Math.sqrt(dx * dx + dy * dy);

        // if radius overlap
        if (distance < this.getRadius() + entity.getRadius()) {
            // Collision!
            return true;
        }

        return false;
    }
}
