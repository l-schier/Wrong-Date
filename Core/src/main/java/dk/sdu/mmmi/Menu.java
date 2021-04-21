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
import com.badlogic.gdx.utils.Array;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tes_7
 */
public class Menu{
    
    
    private ArrayList<File> helpFiles;
    
    private static  int WidthWindow;
    private static int Width0;
    private static int Width;
    private static int Height;
    private static int spacing = 20;
    private boolean pause, resume;
    private boolean pauseClicked, helpClicked,settingsClicked;
    Skin skin;
    String backgorundImageStr;

    public Menu(){
        renderFiles();

    }
    
    private void renderFiles(){
        Array<String> files = new Array();
        files.addAll("PinkSquare.jpg", "ProfilePicture.png",
                "White-square.jpg", "default.fnt", "uiskin.atlas",
                "uiskin.json", "uiskin.png");
        
        for(String s: files){
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(s);
            File temp = new File(s);

            // copy module sprites to runner folder
            try {
                Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
            }
        }
    }
    
    public void setMenuData(int WidthWindow, int Width0, int Height){
        this.WidthWindow = WidthWindow;
        this.Width0 = Width0;
        this.Height = Height;
        Width = WidthWindow - Width0;
    }
    
    public void draw(Skin skin, Stage stage){
        backgorundImageStr = Gdx.files.getLocalStoragePath() + "PinkSquare.jpg";
        this.skin = skin;
        //Menu Field
        Image backgroundImage = new Image(new Texture(backgorundImageStr));
        backgroundImage.setPosition(Width0, 0);
        backgroundImage.setWidth(Width);
        backgroundImage.setHeight(Height);
        stage.getActors().add(backgroundImage);
        
        //Profile Picture
        String proPicURL = Gdx.files.getLocalStoragePath() + "ProfilePicture.png";
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
        skin = new Skin(Gdx.files.internal(Gdx.files.getLocalStoragePath() + "uiskin.json"));
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
        String whiteSquare = Gdx.files.getLocalStoragePath() + "White-square.jpg";
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
            Image img = new Image(new Texture(whiteSquare));
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

        helpButtonfunctionality(stage, helpButton);
        pauseButtonFunctionality((TextButton) pauseButton);
    }
    
    private void helpButtonfunctionality(Stage stage, Button helpButton){
        helpButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!helpClicked){
                    help(stage);
                    helpClicked = true;
                } else{
                   Array<Actor> a = stage.getActors();
                   for (int i = 0; i < a.size; i++){
                       if( a.get(i).getX() < Width0){
                           stage.getActors().removeIndex(i);
                           i -=1;
                       }
                   }  
                    resume();
                    helpClicked = false;   
                    helpButtonfunctionality(stage, helpButton);
                }

               
            }
        } );
    }
    
    private void pauseButtonFunctionality(TextButton pauseButton){
        pauseButton.addListener( new ClickListener() {           
            public void clicked(InputEvent event, float x, float y) {

                if(!pauseClicked){
                    pause();
                    pauseButton.setText("RESUME");
                    pauseClicked = true;
                } else{
                    resume();
                    pauseButton.setText("PAUSE");       
                    pauseClicked = false;
                } 
      
                             
            }
        } );
    }
    
    
        
    public void help(Stage stage){
       
        pause(); 

        //Background
        Image helpBImage = new Image(new Texture(Gdx.files.internal(backgorundImageStr)));
        helpBImage.setPosition(0, 0);
        helpBImage.setWidth(Width0);
        helpBImage.setHeight(Height);
        stage.getActors().add(helpBImage);
        //Hashmap with all help files
        HashMap<String, File> files = new HashMap<>();
        
        for (File f : helpFiles){
            files.put(f.getName().replace(".txt", ""), f);
            System.out.println(files.containsKey("help"));
        }
        
        //Array with all buttons
        HashMap<String, TextButton> buttons = new HashMap<>();
        
        for (Map.Entry<String, File> e : files.entrySet()){
            buttons.put((String) e.getKey(), new TextButton((String) e.getKey(), skin));
        }
        
        int buttonWidth;
        
        
        
        int buttonHeight = 50;
        int buttonX = 0;
        int buttonY = Height - buttonHeight;


        //Text Area
        TextArea textArea = new TextArea("", skin);
        textArea.setWidth(Width0);
        textArea.setHeight(Height - buttonHeight);
        textArea.setPosition(0, 0);
        stage.getActors().add(textArea);
        
        if(buttons.size() < 1){
            String str = "Sorry, no help to get.\nYou are on your own";
            textArea.setText(str);
        } else {
            buttonWidth = Width0/buttons.size();
            
            for (Map.Entry<String, TextButton> e : buttons.entrySet()){
            TextButton b = e.getValue();
            b.setWidth(buttonWidth);
            b.setHeight(buttonHeight);
            b.setPosition(buttonX, buttonY);
            stage.getActors().add(b);
            buttonX += buttonWidth;
            b.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            textArea.setText(helpText(files.get((e.getKey()))));
                       }
        } );
        }
        }
        
        
                
    }
    
    public String helpText(File f){
        
        Scanner scanner;
        ArrayList<String> a = new ArrayList<String>();
        String text ="";
        
        try {
            scanner = new Scanner(f);
            while(scanner.hasNext()){
                String str = scanner.nextLine();
                String[] ar = str.split("\n");
                for(String s : ar){
                    a.add(s);
                }
   
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (String s: a){
            text += s + "\n";
        }
        
        return text;
    }
    
    public void setHelpFiles(ArrayList<File> helpFiles){
        this.helpFiles = helpFiles;
    }
    
    public boolean getPause(){
        return pause;
    }
    
    public void resetPause(){
        pause = false;
    }

    
    public boolean getResume(){
        return resume;
    }
    
    public void resetResume(){
        resume = false;
    }
    
    
    private boolean canPause(){
        if(helpClicked || pauseClicked || settingsClicked){
            return false;
        } else {
            return true;
        }
    }
    
    private boolean canResume(){
        if((helpClicked ^ pauseClicked ^ settingsClicked) || 
                (!helpClicked & !pauseClicked & !settingsClicked)){
            return true;
        } else {
            return false;
        }
    }
    
    private void pause(){
        if(canPause()){
            pause = true;
        }
    }
    
    private void resume(){
        if(canResume()){
            resume = true;
        }
    }
   




    
}