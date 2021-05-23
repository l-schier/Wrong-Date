package dk.sdu.mmmi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
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
import dk.sdu.mmmi.common.data.entityparts.DescriptionPart;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.PlayerPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

/**
 *
 * @author tes_7
 */
public class Menu {

    private ArrayList<File> helpFiles;
    private Array<Actor> helpActors, settingsActors = null;
    private HashMap<String, CheckBox> components = null;
    private HashMap<String, Bundle> bundles = null;

    private int Width0 = -150;
    private int WidthWindow;
    private int WidthStart;
    private int WidthMenu;
    private int WidthGame;
    private int Height;
    private int spacing = 10;
    private boolean pause, resume;
    private boolean pauseClicked, helpClicked, settingsClicked;
    Skin skin;
    Stage stage;
    World world;

    Entity player, tempPlayer, weapon;

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

    //Inventory
    int invW, invH, invX, invY, inv0 = 3, invC, invR, invS;
    Array<Image> frames;
    Map<Entity, Image> items;
    Image clickedItem;

    File weaponDescFile;
    Image weaponImage = null;

    Image playerImage;
    Boolean playerAdded = false;

    int count = 0;

    public Menu(int WidthWindow, int gameWidth, int Height, Skin skin, Stage stage, World world) {
        this.WidthWindow = Width0 + WidthWindow;
        this.WidthStart = Width0 + gameWidth;
        this.WidthGame = gameWidth;
        this.Height = Height;
        WidthMenu = WidthWindow - gameWidth;
        this.skin = skin;
        this.stage = stage;
        this.world = world;

        frames = new Array<>();

        draw();
        helpButtonfunctionality();
        pauseButtonFunctionality();
        settingsButtonFunctionality();
    }

    private void draw() {
        int x1 = WidthStart + spacing;
        int width1 = 90;
        int x2 = x1 + width1 + spacing;
        int width2 = WidthWindow - x2 - spacing;
        int height2 = 40;
        int width3 = WidthMenu - (2 * spacing);

        backgorundImageStr = Gdx.files.getLocalStoragePath() + "PinkSquare.jpg";
        //Menu Field
        backgroundImage = new Image(new Texture(backgorundImageStr));
        backgroundImage.setPosition(WidthStart, 0);
        backgroundImage.setSize(WidthMenu, Height);
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
        invW = width3;
        invH = invW / inv0;
        invX = x1;
        invY = (int) (invTextField.getY() - spacing - invH);

        //Item Information Image
        iteminfoImage = new Image(new Texture(Gdx.files.internal(whiteSquare)));
        iteminfoImage.setPosition(x1, invY - spacing - width1);
        iteminfoImage.setSize(width1, width1);
        stage.getActors().add(iteminfoImage);

        //Item Information TextArea
        itemInfoArea = new TextArea("", skin);
        itemInfoArea.setPosition(x2, iteminfoImage.getY());
        itemInfoArea.setSize(width2, width1);
        stage.getActors().add(itemInfoArea);

        //Help button?
        helpButton = new TextButton("HELP", skin);
        helpButton.setPosition(WidthStart, 0);
        helpButton.setSize(WidthMenu / 3, (itemInfoArea.getY() - spacing) / 2);

        stage.getActors().add(helpButton);

        //Settings button
        settingsButton = new TextButton("SETTINGS", skin);
        settingsButton.setPosition(WidthStart + helpButton.getWidth(), helpButton.getY());
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
                    help();
                } else if (helpClicked) {
                    removeHelp();
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

    private void settingsButtonFunctionality() {
        //https://stackoverflow.com/questions/6527306/best-technique-for-getting-the-osgi-bundle-context
        settingsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!settingsClicked) {
                    Settings();
                } else {
                    removeSettings();
                }
            }
        });
    }

    private void Settings() {
        pause();

        BundleContext context = FrameworkUtil.getBundle(Entity.class).getBundleContext();

        if (helpClicked) {
            removeHelp();
        }

        if (bundles == null) {
            bundles = new HashMap<>();
            for (Bundle b : context.getBundles()) {
                String name = b.getSymbolicName();
                if (name.contains(".") || name.equals("Core")) {
                } else {
                    bundles.put(name, b);
                }
            }
        }

        if (components == null) {
            components = new HashMap<>();
            for (Map.Entry<String, Bundle> e : bundles.entrySet()) {
                components.put(e.getKey(), new CheckBox(e.getKey(), skin));
            }
        }

        if (settingsActors == null) {
            settingsActors = new Array<>();

            Image setBImage = new Image(new Texture(Gdx.files.internal(backgorundImageStr)));
            setBImage.setPosition(Width0, 0);
            setBImage.setWidth(WidthGame);
            setBImage.setHeight(Height);
            settingsActors.add(setBImage);
        }

        int x = Width0 + spacing,
                checkHeight = 60,
                y = Height - spacing - checkHeight,
                buttonHeight = 50;

        for (Map.Entry<String, CheckBox> e : components.entrySet()) {
            TextButton uninstall = new TextButton("X", skin);
            uninstall.setHeight(25);
            uninstall.setWidth(25);
            uninstall.setPosition(x, y + 25);
            settingsActors.add(uninstall);

            CheckBox check = e.getValue();
            check.setHeight(checkHeight);
            check.setPosition(x + 30, y);
            y -= (spacing + checkHeight);

            if (y <= buttonHeight) {
                y = Height - checkHeight - spacing;
                x = WidthGame / 2 + Width0 + spacing;
            }

            settingsActors.add(check);

            uninstall.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    String name = e.getKey();
                    Bundle b = bundles.get(name);
                    try {
                        b.uninstall();
                        bundles.remove(name);
                        components.remove(name);
                        stage.getActors().removeValue(uninstall, false);
                        stage.getActors().removeValue(check, false);
                    } catch (BundleException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        for (Map.Entry<String, Bundle> e : bundles.entrySet()) {
            components.get(e.getKey()).setChecked(e.getValue().getState() == 32);
        }

        TextField bundleName = new TextField("xxx_1.0.0.SNAPSHOT.jar", skin);
        bundleName.setHeight(buttonHeight);
        bundleName.setWidth((WidthGame / 4) * 3);
        bundleName.setPosition(Width0 + (WidthGame / 4), buttonHeight);
        settingsActors.add(bundleName);

        TextButton install = new TextButton("INSTALL", skin);
        install.setHeight(buttonHeight);
        install.setWidth(WidthGame / 4);
        install.setPosition(Width0, buttonHeight);
        settingsActors.add(install);

        install.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String bundle = bundleName.getText();
                String installPath = "file:/D:/Git/Wrong-Date/runner/bundles/" + bundle;
                try {
                    Bundle b = context.installBundle(installPath);
                    b.start();
                    String name = b.getSymbolicName();
                    bundles.put(name, b);
                    components.put(name, new CheckBox(name, skin));
                    removeSettings();
                } catch (BundleException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        TextButton save = new TextButton("SAVE", skin);
        save.setHeight(buttonHeight);
        save.setWidth(WidthGame);
        save.setPosition(Width0, 0);
        settingsActors.add(save);

        save.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                for (Map.Entry<String, CheckBox> e : components.entrySet()) {
                    CheckBox check = e.getValue();
                    String name = e.getKey();
                    Bundle b = bundles.get(name);
                    if (check.isChecked() && !(b.getState() == 32)) {
                        try {
                            b.start();
                        } catch (BundleException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else if (!check.isChecked() && b.getState() == 32) {
                        try {
                            b.stop();
                        } catch (BundleException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        for (Actor a : settingsActors) {
            stage.addActor(a);
        }

        settingsClicked = true;
    }

    private void removeSettings() {
        for (Actor a : settingsActors) {
            a.remove();
        }
        settingsActors.removeRange(1, settingsActors.size - 1);

        resume();
        settingsClicked = false;
    }

    private void help() {
        pause();

        if (settingsClicked) {
            removeSettings();
        }

        helpActors = new Array<>();
        helpFiles = new ArrayList();

        for (Entity e : world.getEntities()) {
            if (e.getPart(HelpPart.class) != null) {
                HelpPart e1 = e.getPart(HelpPart.class);
                helpFiles.add(e1.getFile());
            }
        }
        helpFiles.add(new File(Gdx.files.getLocalStoragePath() + "Core.txt"));

        //Background
        Image helpBImage = new Image(new Texture(Gdx.files.internal(backgorundImageStr)));
        helpBImage.setPosition(Width0, 0);
        helpBImage.setWidth(WidthGame);
        helpBImage.setHeight(Height);
        helpActors.add(helpBImage);
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
        int buttonX = Width0;
        int buttonY = Height - buttonHeight;

        //Text Area
        TextArea textArea = new TextArea("", skin);
        textArea.setWidth(WidthGame);
        textArea.setHeight(Height - buttonHeight);
        textArea.setPosition(Width0, 0);
        helpActors.add(textArea);

        if (buttons.size() < 1) {
            String str = "Sorry, no help to get.\nYou are on your own";
            textArea.setText(str);
        } else {
            buttonWidth = WidthGame / buttons.size();

            for (Map.Entry<String, TextButton> e : buttons.entrySet()) {
                TextButton b = e.getValue();
                System.out.println(b.getText());
                b.setWidth(buttonWidth);
                b.setHeight(buttonHeight);
                b.setPosition(buttonX, buttonY);
                helpActors.add(b);
                buttonX += buttonWidth;
                b.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        textArea.setText(fileToText(files.get((e.getKey()))));
                    }
                });
            }
        }

        for (Actor a : helpActors) {
            stage.getActors().add(a);
        }

        helpClicked = true;
    }

    private void removeHelp() {
        if (pauseClicked) {
            pauseButton.setText("PAUSE");
            pauseClicked = false;
        }
        stage.getActors().removeAll(helpActors, false);

        resume();
        helpClicked = false;
    }

    private String fileToText(File f) {
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

    public void update() {
        updatePlayer();
        updateWeapon();
        updateInventory();
    }

    private void updateInventory() {
        if (player == null) {
            addFrames(invX, invY, invW, invH, inv0);
        } else {
            if (player.getPart(InventoryPart.class) != null) {
                InventoryPart inventoryPart = player.getPart(InventoryPart.class);
                ArrayList<Entity> list = inventoryPart.getInventory();

                invC = inv0;
                invR = 1;
                invS = invC * invR;

                if (list.isEmpty()) {
                    addFrames(invX, invY, invW / inv0, invH, invS);
                } else {
                    if (invS < list.size()) {
                        for (int i = invS; i <= items.size(); i = invC * invR) {
                            invC += inv0;
                            invR++;
                            if (i >= items.size()) {
                                invS = i;
                            }
                        }
                        addFrames(invX, invY + invH / invR, invW / invC, invH / invR, invS);
                    }
                    if (items == null) {
                        items = new HashMap<>();
                        for (Entity e : list) {
                            if (e.getPart(RenderPart.class) != null) {
                                RenderPart renderPart = e.getPart(RenderPart.class);
                                items.put(e, new Image(new Texture(renderPart.getSpritePath())));
                                stage.getActors().add(items.get(e));
                                itemClicked(items.get(e), e);
                            }
                        }
                    }
                    int i = 0;
                    for (Map.Entry<Entity, Image> e : items.entrySet()) {
                        Image item = e.getValue();
                        Image frame = frames.get(i);
                        item.setPosition(frame.getX(), frame.getY());
                        item.setSize(frame.getWidth(), frame.getHeight());
                    }
                }
            }
        }
    }

    private void addFrames(int x, int y, int w, int h, int s) {
        boolean up = frames.size <= s || frames.size < inv0, down = frames.size > s;

        if (up) {
            for (int i = frames.size; i < s; i++) {
                Image image = new Image(new Texture(whiteSquare));
                frames.add(image);
            }
            for (Image image : frames) {
                image.setPosition(x, y);
                image.setSize(w, h);
                x += w;
                if (frames.indexOf(image, true) + 1 % invC == 0) {
                    x = invX;
                    y -= h;
                }
            }
            stage.getActors().addAll(frames);
        }
        if (down) {
            //ToDO implementation
        }
    }

    private void itemClicked(Image img, Entity e) {
        img.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (e.getPart(RenderPart.class) != null) {
                    RenderPart renderPart = e.getPart(RenderPart.class);
                    clickedItem = new Image(new Texture(renderPart.getSpritePath()));

                    if (!stage.getActors().contains(clickedItem, true)) {
                        clickedItem.setPosition(iteminfoImage.getX(), iteminfoImage.getY());
                        clickedItem.setSize(iteminfoImage.getWidth(), iteminfoImage.getHeight());
                        stage.getActors().add(clickedItem);
                    }

                }
                if (e.getPart(DescriptionPart.class) != null) {
                    DescriptionPart descriptionPart = e.getPart(DescriptionPart.class);
                    itemInfoArea.setText(fileToText(descriptionPart.getDescription()));
                } else {
                    itemInfoArea.setText("No Information");
                }
            }
        });
    }

    private void updatePlayer() {
        boolean exists = false;

        for (Entity e : world.getEntities()) {
            if (e.getPart(PlayerPart.class) != null) {
                exists = true;
                tempPlayer = e;
            }
        }
        if (!exists) {
            proNameTextField.setText("No Current Player");
            removePlayer();
        } else if (player == null || !player.equals(tempPlayer)) {
            player = tempPlayer;
            if (player.getPart(RenderPart.class) != null) {
                RenderPart renderPart = player.getPart(RenderPart.class);
                playerImage = new Image(new Texture(renderPart.getSpritePath()));
                playerImage.setSize(proPicImage.getWidth() / 4 * 3, proPicImage.getHeight() / 4 * 3);
                playerImage.setPosition(proPicImage.getX() + proPicImage.getWidth() / 8, proPicImage.getY() + proPicImage.getHeight() / 8);

                addPlayer();
            }
            if (player.getPart(PlayerPart.class) != null) {
                PlayerPart playerPart = player.getPart(PlayerPart.class);

                if (playerPart.getName() == null) {
                    proNameTextField.setText("No Name Chosen");
                } else {
                    proNameTextField.setText(playerPart.getName());
                }
            }
        }
    }

    private void addPlayer() {
        if (!playerAdded) {
            stage.getActors().add(playerImage);
            playerAdded = true;
        }
    }

    private void removePlayer() {
        if (playerAdded) {
            stage.getActors().removeValue(playerImage, true);
            playerAdded = false;
        }
    }

    private void updateWeapon() {
        InventoryPart inventoryPart = player.getPart(InventoryPart.class);

        if (inventoryPart.getWeapon() != null) {
            if (weapon == null || !weapon.equals(inventoryPart.getWeapon())) {
                weapon = inventoryPart.getWeapon();
                if (weapon.getPart(DescriptionPart.class) != null) {
                    DescriptionPart descriptionPart = weapon.getPart(DescriptionPart.class);
                    weapDescArea.setText(fileToText(descriptionPart.getDescription()));
                }
                if (weapon.getPart(RenderPart.class) != null) {
                    RenderPart renderPart = weapon.getPart(RenderPart.class);

                    if (weaponImage != null) {
                        stage.getActors().removeValue(weaponImage, true);
                    }

                    weaponImage = null;
                    weaponImage = new Image(new Texture(renderPart.getSpritePath()));

                }
                if (!stage.getActors().contains(weaponImage, true)) {
                    weaponImage.setSize(weapImage.getImageWidth(), weapImage.getImageHeight());
                    weaponImage.setPosition(weapImage.getX(), weapImage.getY());
                    stage.getActors().add(weaponImage);
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

    public void pause() {
        if (canPause()) {
            pause = true;
        }
    }

    public void resume() {
        if (canResume()) {
            resume = true;
        }
    }
}
