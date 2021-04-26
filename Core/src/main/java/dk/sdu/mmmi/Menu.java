/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private static int spacing = 10;
    private boolean pause, resume;
    private boolean pauseClicked, helpClicked,settingsClicked;
    Skin skin;
    
    
    //Menu
    String backgorundImageStr;
    Image backgroundImage;
    String proPicURL;
    Image proPicImage;
    String proNameStr = "";
    TextField proNameTextField;
    TextField weapTextField;
    String whiteSquare;
    Image weapImage;
    String weapDescString;
    TextArea weapDescArea;
    TextField invTextField;
    Image iteminfoImage;
    String itemInfoStr = "";
    TextArea itemInfoArea;
    Button helpButton, settingsButton, pauseButton;
    
    

    
    public void setMenuData(int WidthWindow, int Width0, int Height){
        this.WidthWindow = WidthWindow;
        this.Width0 = Width0;
        this.Height = Height;
        Width = WidthWindow - Width0;
    }
    
    public void draw(Skin skin, Stage stage){
        int x1 = Width0 + spacing;
        int width1 = 90;
        int x2 = x1 + width1 + spacing;
        int width2 = WidthWindow - x2 - spacing;
        int height2 = 40;
        int width3 = Width - (2*spacing);
        
        backgorundImageStr = Gdx.files.getLocalStoragePath() + "PinkSquare.jpg";
        this.skin = skin;
        //Menu Field
        backgroundImage = new Image(new Texture(backgorundImageStr));
        backgroundImage.setPosition(Width0, 0);
        backgroundImage.setSize(Width, Height);
        stage.getActors().add(backgroundImage);
        
        //Profile Picture
        proPicURL = Gdx.files.getLocalStoragePath() + "ProfilePicture.png";
        proPicImage = new Image(new Texture(Gdx.files.internal(proPicURL)));
        proPicImage.setPosition(x1, Height - spacing - width1);
        proPicImage.setSize(width1, width1);
        stage.getActors().add(proPicImage);
        
        //Profile Name TextField 
        proNameStr = "Test Name";
        proNameTextField = new TextField(proNameStr, skin);
        proNameTextField.setPosition(x2, proPicImage.getY() + width1 - height2);
        proNameTextField.setSize(width2 , height2);
        stage.getActors().add(proNameTextField);
        
        //Weapon TextField
        weapTextField = new TextField("WEAPON", skin);
        weapTextField.setPosition(x1, proPicImage.getY() - spacing - height2);
        weapTextField.setSize(width3, height2);
        stage.getActors().add(weapTextField);
        
        //Weapon Image
        whiteSquare = Gdx.files.getLocalStoragePath() + "White-square.jpg";
        weapImage = new Image(new Texture(Gdx.files.internal(whiteSquare)));
        weapImage.setPosition(x1, weapTextField.getY() - spacing - width1);
        weapImage.setSize(width1, width1);
        stage.getActors().add(weapImage);
        
        //Weapon description TextArea
        weapDescString = "Weapon";
        weapDescArea = new TextArea(weapDescString, skin);
        weapDescArea.setPosition(x2, weapImage.getY());
        weapDescArea.setSize(width2, width1);
        stage.getActors().add(weapDescArea);
        
        //Inventory TextField
        invTextField = new TextField( "INVENTORY", skin);
        invTextField.setPosition(x1, weapImage.getY() - spacing - height2);
        invTextField.setSize(width3, height2);
        stage.getActors().add(invTextField);
        
        //inventory pictures 
        Image tempImage = new Image(new Texture(Gdx.files.internal(whiteSquare)));
        tempImage.setPosition(x1, invTextField.getY() - spacing - 100);
        tempImage.setSize(width3, 100);
        stage.getActors().add(tempImage);
  
        //Item Information Image
        iteminfoImage = new Image(new Texture(Gdx.files.internal(whiteSquare)));
        iteminfoImage.setPosition(x1, tempImage.getY() - spacing - width1);
        iteminfoImage.setSize(width1, width1);
        stage.getActors().add(iteminfoImage);
        
        //Item Information TextArea
        itemInfoStr = "Item information";
        itemInfoArea = new TextArea(itemInfoStr, skin);
        itemInfoArea.setPosition(x2, iteminfoImage.getY());
        itemInfoArea.setSize(width2, width1);
        stage.getActors().add(itemInfoArea);
        
        
        //Help button�
        helpButton = new TextButton("HELP", skin);
        helpButton.setPosition(Width0, 0);  
        helpButton.setSize(Width/3, (itemInfoArea.getY() - spacing)/2);
       
        stage.getActors().add(helpButton);
        
        //Settings button
        settingsButton = new TextButton("SETTINGS", skin);
        settingsButton.setPosition(Width0 + helpButton.getWidth(), helpButton.getY());  
        settingsButton.setSize(helpButton.getWidth()*2, helpButton.getHeight());
        stage.getActors().add(settingsButton);
        
        //Pause button
        pauseButton = new TextButton("PAUSE", skin);
        pauseButton.setPosition(x1, helpButton.getHeight());
        pauseButton.setSize(width3, helpButton.getHeight());
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
        return !(helpClicked || pauseClicked || settingsClicked);
    }
    
    private boolean canResume(){
        return (helpClicked ^ pauseClicked ^ settingsClicked) || 
                (!helpClicked & !pauseClicked & !settingsClicked);
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