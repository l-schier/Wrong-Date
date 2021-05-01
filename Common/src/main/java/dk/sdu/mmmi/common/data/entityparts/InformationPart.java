/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.common.data.entityparts;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
public class InformationPart implements EntityPart{
    

    private String image, description; 
    private final Object component; 
    
    public InformationPart(String image, String description, Object component){
        this.component = component; 
        renderFile(image);
        renderFile(description);
        this.image = image;
        this.description = description;
    }
    
    private void renderFile(String fileName) {
       InputStream inputStream = component.getClass().getClassLoader().getResourceAsStream(fileName);
        File temp = new File(fileName);

        // copy module sprites to runner folder
        try {
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
//            return (Gdx.files.getLocalStoragePath() + fileName);
        } catch (Exception e) {
        }    
        
//        return null;
    }
    
    public Image getImage(){
        return new Image(new Texture(Gdx.files.getLocalStoragePath() + image));
    }
    
    public File getDescription(){
        return new File(Gdx.files.getLocalStoragePath() + description);
    }
    


    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
