/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.common.data.entityparts;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author sofie
 */
public class RenderPart implements EntityPart {
    private String spriteFile; 
    private Object component; 
    
    public RenderPart(String spriteFile, Object component){
        this.component = component; 
        this.spriteFile = getSprite(spriteFile);
    }
    
    private String getSprite(String spriteFile) {
        // Get module sprites
        InputStream inputStream = this.component.getClass().getClassLoader().getResourceAsStream(spriteFile);
        File temp = new File(spriteFile);

        // Copy module sprites to runner folder
        try {
            // Reconsider replacing existing
            // Or giving unique name to file during copy
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return spriteFile;
        } catch (IOException e) {
        }

        return null;
    }
    
    public void setSprite(String newfile){
        this.spriteFile = getSprite(newfile);
    }
    
    public String getSpritePath(){
        return this.spriteFile; 
    }
    
    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
