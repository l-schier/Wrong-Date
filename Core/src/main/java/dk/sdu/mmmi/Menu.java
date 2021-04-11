/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tes_7
 */
public class Menu {
    
    private static  int WidthWindow;
    private static int Width0;
    private static int Width;
    private static int Height;
    private static int spacing = 20;
    
    public void setMenuData(int WidthWindow, int Width0, int Height){
        this.WidthWindow = WidthWindow;
        this.Width0 = Width0;
        this.Height = Height;
        Width = WidthWindow - Width0;
    }
    
    public void draw(Skin skin, Stage stage){
        //Profile Picture
        String proPicURL = "C:\\Users\\tes_7\\OneDrive\\Skrivebord\\Wrong-Date\\Core\\src\\main\\java\\dk\\sdu\\mmmi\\ProfilePicture.png";
        Image proPicImage = new Image(new Texture(Gdx.files.internal(proPicURL)));
        int proPicX = Width0 + spacing;
        int proPicR = (100 - spacing*2)/2;
        int proPicWidth = proPicR*2;
        int proPicHeight = proPicWidth;
        int proPicY = Height - spacing - proPicHeight;
        proPicImage.setPosition(proPicX, proPicY);
        proPicImage.setWidth(proPicWidth);
        proPicImage.setHeight(proPicHeight);
        stage.getActors().add(proPicImage);
        
        //Profile Name TextField   
        //TODO fix next line 
        skin = new Skin(Gdx.files.internal("C:\\Users\\tes_7\\OneDrive\\Skrivebord\\Wrong-Date\\Core\\src\\main\\java\\dk\\sdu\\mmmi\\uiskin.json"));
        String proNameStr = "Hello";
        TextField proNameTextField = new TextField(proNameStr, skin);
        int proNameX = proPicX + proPicWidth + spacing;
        int proNameY = proPicY + proPicHeight/2;
        int proNameLength = WidthWindow - proNameX - spacing;
        proNameTextField.setPosition(proNameX, proNameY);
        proNameTextField.setSize(proNameLength , proPicR);
        stage.getActors().add(proNameTextField);
        
        //Weapon TextField
        String weapStr = "WEAPON";
        TextField weapTextField = new TextField(weapStr, skin);
        int weapLength = Width - (2*spacing);
        int weapHeight = 25;
        int weapX = Width0 + spacing;
        int weapY = proPicY - spacing - weapHeight;
        weapTextField.setPosition(weapX, weapY);
        weapTextField.setScale(.25f);
        weapTextField.setSize(weapLength, weapHeight);
        stage.getActors().add(weapTextField);
        
        //Weapon Image
        String whiteSquare = "C:\\Users\\tes_7\\OneDrive\\Skrivebord\\Wrong-Date\\Core\\src\\main\\java\\dk\\sdu\\mmmi\\White-square.jpg";
        Image weapImage = new Image(new Texture(Gdx.files.internal(whiteSquare)));
        int weapImageHeight = proPicR * 2;
        int weapImageWidth = proPicR * 2;
        int weapImageX = weapX;
        int weapImageY = weapY - spacing - weapImageHeight;
        weapImage.setSize(weapImageWidth, weapImageHeight);
        weapImage.setPosition(weapImageX, weapImageY);
        stage.getActors().add(weapImage);
        
        //Weapon description TextArea
        String weapDescString = "Weapon";
        TextArea weapDescArea = new TextArea(weapDescString, skin);
        int weapDescHeight = weapImageHeight;
        int weapDescX = proNameX;
        int weapDescLength = proNameLength;
        int weapDescY = weapImageY;
        weapDescArea.setWidth(weapDescLength);
        weapDescArea.setHeight(weapDescHeight);
        weapDescArea.setScale(.10f);
        weapDescArea.setPosition(weapDescX, weapDescY);
        stage.getActors().add(weapDescArea);
        
        //Inventory TextField
        String invString = "INVENTORY";
        TextField invTextField = new TextField(invString, skin);
        int invLength = Width - (2*spacing);
        int invHeight = 25;
        int invX = Width0 + spacing;
        int invY = weapImageY - weapImageHeight + spacing;
        invTextField.setPosition(invX, invY);
        invTextField.setScale(.25f);
        invTextField.setSize(invLength, invHeight);
        stage.getActors().add(invTextField);
        
        //inventory pictures 
        int columns = 4, rows = 2;
        int invWidth = Width - 2*spacing;
        int invPicHeight = invWidth/columns;
        int invPicWidth = invWidth/columns;
        int invPicX0 = Width0 + spacing;
        int invPicX = invPicX0;
        int invPicY = invY - invHeight - spacing - invPicHeight/2;      

        int count = 1;
        List items = new ArrayList();
        while(count < columns * rows + 1 ){

            if(count%(columns+1) == 0){
                invPicX = invPicX0;
                invPicY -= invHeight;
            }
            Image img = new Image(new Texture(Gdx.files.internal(whiteSquare)));
            img.setSize(invPicWidth, invPicHeight);
            img.setPosition(invPicX, invPicY);
            items.add(img);
            count ++;
            invPicX += invPicWidth;
        }
        
        for(int i = 0; i < items.size(); i++){
            stage.getActors().add((Actor) items.get(i));
        }
        
        //Item Information Image
        Image iteminfoImage = new Image(new Texture(Gdx.files.internal(whiteSquare)));
        int itemInfoImageHeight = weapImageHeight;
        int itemInfoImageWidth = weapImageWidth;
        int itemInfoImageX = weapImageX;
        int itemInfoImageY = invPicY - invPicHeight/2 - spacing*2;
        iteminfoImage.setSize(itemInfoImageWidth, itemInfoImageHeight);
        iteminfoImage.setPosition(itemInfoImageX, itemInfoImageY);
        stage.getActors().add(iteminfoImage);
        
        //Item Information TextArea
        String itemInfoStr = "Item information";
        TextArea itemInfoArea = new TextArea(itemInfoStr, skin);
        int itemInfoAreaHeight = weapDescHeight;
        int itemInfoAreaX = weapDescX;
        int itemInfoAreaWidth = weapDescLength;
        int itemInfoAreaY = itemInfoImageY;
        itemInfoArea.setWidth(itemInfoAreaWidth);
        itemInfoArea.setHeight(itemInfoAreaHeight);
        itemInfoArea.setPosition(itemInfoAreaX, itemInfoAreaY);
        stage.getActors().add(itemInfoArea);
        
        //Help button
        String helpButtonStr = "HELP";
        Button helpButton = new TextButton(helpButtonStr, skin);
        int helpButtonWidth = Width /2;
        int helpButtonHeight = 50;
        int helpButtonX = Width0;
        int helpButtonY = 0;
        helpButton.setPosition(helpButtonX, helpButtonY);  
        helpButton.setWidth(helpButtonWidth);
        helpButton.setHeight(helpButtonHeight);
        stage.getActors().add(helpButton);
        
        //Settings button
        String settingsStr = "SETTINGS";
        Button settingsButton = new TextButton(settingsStr, skin);
        int settingsButtonWidth = helpButtonWidth;
        int settingsButtonHeight = helpButtonHeight;
        int settingsButtonX = helpButtonX + helpButtonWidth;
        int settingsButtonY = helpButtonY;
        settingsButton.setPosition(settingsButtonX, settingsButtonY);  
        settingsButton.setWidth(settingsButtonWidth);
        settingsButton.setHeight(settingsButtonHeight);
        stage.getActors().add(settingsButton);
        
        //Pause button
        String pauseStr = "PAUSE";
        Button pauseButton = new TextButton(pauseStr, skin);
        int pauseButtonWidth = Width;
        int pauseButtonHeight = helpButtonHeight;
        int pauseButtonX = Width0;
        int pauseButtonY = helpButtonY + helpButtonHeight;
        pauseButton.setWidth(pauseButtonWidth);
        pauseButton.setHeight(pauseButtonHeight);
        pauseButton.setPosition(pauseButtonX, pauseButtonY);
        stage.getActors().add(pauseButton);
        
//        pauseButton.addListener(new ClickListener(){
//        @Override
//        public void clicked(InputEvent event, float x, float y) {
//            System.out.println("Working");
//        }
//        });
    }
    
       

    
}
