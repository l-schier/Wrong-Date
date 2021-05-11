package dk.sdu.mmmi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.HelpPart;
import dk.sdu.mmmi.common.data.entityparts.InformationPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.PlayerPart;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tes_7
 */
public class Menu {

    private ArrayList<File> helpFiles;
    private Array<Actor> actors;

    private static int WidthWindow;
    private static int Width0;
    private static int Width;
    private static int Height;
    private static int spacing = 10;
    private boolean pause, resume;
    private boolean pauseClicked, helpClicked, settingsClicked;
    Skin skin;
    Stage stage;
    World world;

    Entity player, weapon;

    //Menu
    String backgorundImageStr;
    Image backgroundImage;
    String proPicURL;
    Image proPicImage;
    TextField proNameTextField;
    TextField weapTextField;
    String whiteSquare;
    Image weapImage;
    TextArea weapDescArea;
    TextField invTextField;
    Image iteminfoImage;
    TextArea itemInfoArea;
    TextButton helpButton, settingsButton, pauseButton;

    File weaponDescFile;
    Image weaponImage = null;

    public Menu(int WidthWindow, int Width0, int Height, Skin skin, Stage stage, World world) {
        this.WidthWindow = WidthWindow;
        this.Width0 = Width0;
        this.Height = Height;
        Width = WidthWindow - Width0;
        this.skin = skin;
        this.stage = stage;
        this.world = world;

        draw();
        helpButtonfunctionality();
        pauseButtonFunctionality();

        for (Entity e : world.getEntities()) {
            if (e.getPart(PlayerPart.class) != null) {
                player = e;
            }
        }
    }

    public void setMenuData(int WidthWindow, int Width0, int Height) {
        this.WidthWindow = WidthWindow;
        this.Width0 = Width0;
        this.Height = Height;
        Width = WidthWindow - Width0;
    }

    public void draw() {
        int x1 = Width0 + spacing;
        int width1 = 90;
        int x2 = x1 + width1 + spacing;
        int width2 = WidthWindow - x2 - spacing;
        int height2 = 40;
        int width3 = Width - (2 * spacing);

        backgorundImageStr = Gdx.files.getLocalStoragePath() + "PinkSquare.jpg";
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
        proNameTextField = new TextField("Test Name", skin);
        proNameTextField.setPosition(x2, proPicImage.getY() + width1 - height2);
        proNameTextField.setSize(width2, height2);
        stage.getActors().add(proNameTextField);

        //Weapon TextField
        weapTextField = new TextField("WEAPON", skin);
        weapTextField.setPosition(x1, proPicImage.getY() - spacing - height2);
        weapTextField.setSize(width3, height2);
        stage.getActors().add(weapTextField);

        //Weapon Image
        whiteSquare = Gdx.files.getLocalStoragePath() + "squareFrame.png";
        weapImage = new Image(new Texture(Gdx.files.internal(whiteSquare)));
        weapImage.setPosition(x1, weapTextField.getY() - spacing - width1);
        weapImage.setSize(width1, width1);
        stage.getActors().add(weapImage);

        //Weapon description TextArea
        weapDescArea = new TextArea("", skin);
        weapDescArea.setPosition(x2, weapImage.getY());
        weapDescArea.setSize(width2, width1);
        stage.getActors().add(weapDescArea);

        //Inventory TextField
        invTextField = new TextField("INVENTORY", skin);
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
        itemInfoArea = new TextArea("", skin);
        itemInfoArea.setPosition(x2, iteminfoImage.getY());
        itemInfoArea.setSize(width2, width1);
        stage.getActors().add(itemInfoArea);

        //Help button?
        helpButton = new TextButton("HELP", skin);
        helpButton.setPosition(Width0, 0);
        helpButton.setSize(Width / 3, (itemInfoArea.getY() - spacing) / 2);

        stage.getActors().add(helpButton);

        //Settings button
        settingsButton = new TextButton("SETTINGS", skin);
        settingsButton.setPosition(Width0 + helpButton.getWidth(), helpButton.getY());
        settingsButton.setSize(helpButton.getWidth() * 2, helpButton.getHeight());
        stage.getActors().add(settingsButton);

        //Pause button
        pauseButton = new TextButton("PAUSE", skin);
        pauseButton.setPosition(x1, helpButton.getHeight());
        pauseButton.setSize(width3, helpButton.getHeight());
        stage.getActors().add(pauseButton);
    }

    private void helpButtonfunctionality() {
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!helpClicked) {
                    help(stage);
                    helpClicked = true;
                } else if (helpClicked) {

                    if (pauseClicked) {
                        pauseButton.setText("PAUSE");
                        pauseClicked = false;
                    }
                    stage.getActors().removeAll(actors, false);

                    resume();
                    helpClicked = false;
                }
            }
        });
    }

    private void pauseButtonFunctionality() {
        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!pauseClicked) {
                    pause();
                    pauseButton.setText("RESUME");
                    pauseClicked = true;
                } else {
                    resume();
                    pauseButton.setText("PAUSE");
                    pauseClicked = false;
                }
            }
        });
    }

    public void help(Stage stage) {
        pause();
        actors = new Array<>();
        helpFiles = new ArrayList();

        for (Entity e : world.getEntities()) {
            if (e.getPart(HelpPart.class) != null) {
                HelpPart e1 = e.getPart(HelpPart.class);
                helpFiles.add(e1.getFile());
            }
        }

        //Background
        Image helpBImage = new Image(new Texture(Gdx.files.internal(backgorundImageStr)));
        helpBImage.setPosition(0, 0);
        helpBImage.setWidth(Width0);
        helpBImage.setHeight(Height);
        actors.add(helpBImage);
        //Hashmap with all help files
        HashMap<String, File> files = new HashMap<>();

        for (File f : helpFiles) {
            files.put(f.getName().replace(".txt", ""), f);
        }

        //Array with all buttons
        HashMap<String, TextButton> buttons = new HashMap<>();

        for (Map.Entry<String, File> e : files.entrySet()) {
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
        actors.add(textArea);

        if (buttons.size() < 1) {
            String str = "Sorry, no help to get.\nYou are on your own";
            textArea.setText(str);
        } else {
            buttonWidth = Width0 / buttons.size();

            for (Map.Entry<String, TextButton> e : buttons.entrySet()) {
                TextButton b = e.getValue();
                b.setWidth(buttonWidth);
                b.setHeight(buttonHeight);
                b.setPosition(buttonX, buttonY);
                actors.add(b);
                buttonX += buttonWidth;
                b.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println(fileToText(files.get((e.getKey()))));
                        textArea.setText(fileToText(files.get((e.getKey()))));
                    }
                });
            }
        }

        for (Actor a : actors) {
            stage.getActors().add(a);
        }
    }

    public String fileToText(File f) {

        Scanner scanner;
        ArrayList<String> a = new ArrayList<String>();
        String text = "";

        try {
            scanner = new Scanner(f);
            while (scanner.hasNext()) {
                String str = scanner.nextLine();
                String[] ar = str.split("\n");
                for (String s : ar) {
                    a.add(s);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (String s : a) {
            text += s + "\n";
        }

        return text;
    }

    public void setHelpFiles(ArrayList<File> helpFiles) {
        this.helpFiles = helpFiles;
    }

    public void update() {
        InventoryPart inventoryPart = player.getPart(InventoryPart.class);
        if (inventoryPart.getWeapon() != null) {

            if (weapon == null || !weapon.equals(inventoryPart.getWeapon())) {
                weapon = inventoryPart.getWeapon();
                InformationPart informationPart = weapon.getPart(InformationPart.class);

                boolean first = false;
                if (weaponImage == null) {
                    first = true;
                }
                weaponImage = informationPart.getImage();
                weaponImage.setSize(weapImage.getImageWidth(), weapImage.getImageHeight());
                weaponImage.setPosition(weapImage.getX(), weapImage.getY());
                weapDescArea.setText(fileToText(informationPart.getDescription()));
                if (first) {
                    stage.getActors().add(weaponImage);
                    first = false;
                }
            }
        }
    }

    public boolean getPause() {
        return pause;
    }

    public void resetPause() {
        pause = false;
    }

    public boolean getResume() {
        return resume;
    }

    public void resetResume() {
        resume = false;
    }

    private boolean canPause() {
        return !(helpClicked || pauseClicked || settingsClicked);
    }

    private boolean canResume() {
        return (helpClicked ^ pauseClicked ^ settingsClicked)
                || (!helpClicked & !pauseClicked & !settingsClicked);
    }

    private void pause() {
        if (canPause()) {
            pause = true;
        }
    }

    private void resume() {
        if (canResume()) {
            resume = true;
        }
    }
}
