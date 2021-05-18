/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.common.data.entityparts;
import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author tes_7
 */
public class DescriptionPart implements EntityPart{
    

    private File description; 
    private final Object component; 
    
    public DescriptionPart(String description, Object component){
        this.component = component; 
        renderFile(description);
    }
    
    private void renderFile(String fileName) {
       InputStream inputStream = component.getClass().getClassLoader().getResourceAsStream(fileName);
        description = new File(fileName);

        // copy module sprites to runner folder
        try {
            Files.copy(inputStream, description.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
        }    
    }
    
    public File getDescription(){
        return description;
    }
    
    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
