package dk.sdu.mmmi;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.DoorPart;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IGamePluginService;
import dk.sdu.mmmi.common.services.IHelp;
import dk.sdu.mmmi.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.core.managers.GameInputProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game implements ApplicationListener {
    
    private static int gameWidth = 800;
    private static int menuWidth = 300;
    private static int Width = gameWidth + menuWidth;
    private static int Height = 600;
    
    private static boolean isPaused;

    private static OrthographicCamera cam;
    private ShapeRenderer sr;
    private Stage stage;
    private Skin skin;
    private final GameData gameData = new GameData();
    private Menu menu;
    private static World world = new World();
    private static final List<IEntityProcessingService> entityProcessorList = new CopyOnWriteArrayList<>();
    private static final List<IGamePluginService> gamePluginList = new CopyOnWriteArrayList<>();
    private static List<IPostEntityProcessingService> postEntityProcessorList = new CopyOnWriteArrayList<>();
    private static final List<IHelp> helpList = new CopyOnWriteArrayList<>();
  
    private SpriteBatch batch;

    public Game() {
        init();
        gameData.setDisplayWidth(gameWidth);
        gameData.setDisplayHeight(Height);
    }
    
    public void init() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Wrong-Date";
        cfg.width = Width;
        cfg.height = Height;
        cfg.useGL30 = false;
        cfg.resizable = false;

        new LwjglApplication(this, cfg);
        
        //Use "dir /b > filenames.txt" to crearte file and remove redundant files within
        renderFile("filenames.txt");
        
        try{
            File fileNames = new File(Gdx.files.getLocalStoragePath() + "filenames.txt");
            Scanner sc = new Scanner(fileNames);
            
            while (sc.hasNextLine()){
                renderFile(sc.nextLine());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private void renderFile(String fileName){   
        
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        File temp = new File(fileName);

        // copy module sprites to runner folder
        try {
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
        }    
        
    }

    @Override
    public void create() {
        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();        
        stage = new Stage();
        skin = new Skin(Gdx.files.internal(Gdx.files.getLocalStoragePath() + "comic-ui.json"));
        //Allows multiple inputprocessor
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(new GameInputProcessor(gameData));
        multiplexer.addProcessor(stage);
       

        batch = new SpriteBatch();
        
        menu = new Menu(Width, gameWidth,Height, skin, stage);
    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameData.setDelta(Gdx.graphics.getDeltaTime());
        update();
        // https://www.codeandweb.com/texturepacker/tutorials/libgdx-physics
        // https://stackoverflow.com/questions/6474634/how-do-i-access-a-file-inside-an-osgi-bundle
        // https://stackoverflow.com/questions/6244993/no-access-to-bundle-resource-file-osgi
        // URL file = this.getClass().getClassLoader().getResource("rocket.png");
        draw();
        gameData.getKeys().update();
        stage.draw();
        stage.act();
    }

    private void update() {
        if(!isPaused){
            // Update
            for (IEntityProcessingService entityProcessorService : entityProcessorList) {
                entityProcessorService.process(gameData, world);
            }

            // Post Update
            for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessorList) {
                postEntityProcessorService.process(gameData, world);
            }
        }
        
        
        //Help files
        //I want to move this to Menu class outside of constant update
        ArrayList helpFiles = new ArrayList();
        for (IHelp help : helpList) {
            helpFiles.add(help.getFile());
        }
        menu.setHelpFiles(helpFiles);
        
        //cheking if pause
        if (menu.getPause()){
            pause();
            menu.resetPause();
        }
        
        //Cheking if resume
        if (menu.getResume()){
            resume();
            menu.resetResume();
        }
       
    }

    private void draw() {
        
        for (Entity entity : world.getEntities()) {
            if (entity.getPart(RenderPart.class) != null && entity.getPart(PositionPart.class) != null) {
                PositionPart pos = entity.getPart(PositionPart.class);
                RenderPart render = entity.getPart(RenderPart.class);
                try {
                    Texture img = new Texture(Gdx.files.getLocalStoragePath() + render.getSpritePath());

                batch.begin();
                batch.draw(img, pos.getX() - 16, pos.getY() - 16);
                batch.end();
                } catch (GdxRuntimeException e) {
                    System.out.println("Image not found");
                }

                continue;
        }

        sr.setColor(1, 1, 1, 1);

        sr.begin(ShapeRenderer.ShapeType.Line);

        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();

        for (int i = 0, j = shapex.length - 1;
                i < shapex.length;
                j = i++) {

            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }

        sr.end();
            
        if (entity.getPart(DoorPart.class) != null) {
            sr.setColor(1,0,0,1);

            DoorPart doorPart = entity.getPart(DoorPart.class);
            float[][] doors = doorPart.getDoors();
            for (int i = 0; i < 4; i++) {
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.line(doors[i][0], doors[i][1], doors[i][2], doors[i][3]);
                sr.end();
            }

        }
        
    }

        float boxStartX = 100;
        float boxStopX = 200;
        float boxStartY = 100;
        float boxStopY = 200;

        sr.setColor(1, 0, 0, 1);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(boxStartX, boxStartY, boxStartX, boxStopY);
        sr.line(boxStartX, boxStopY, boxStopX, boxStopY);
        sr.line(boxStopX, boxStopY, boxStopX, boxStartY);
        sr.line(boxStopX, boxStartY, boxStartX, boxStartY);
        sr.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        isPaused = true;
        System.out.println("Game pause");
    }

    @Override
    public void resume() {
        isPaused = false;
        System.out.println("Game Resume");
    }

    @Override
    public void dispose() {
    }

    public void addEntityProcessingService(IEntityProcessingService eps) {
        this.entityProcessorList.add(eps);
    }

    public void removeEntityProcessingService(IEntityProcessingService eps) {
        this.entityProcessorList.remove(eps);
    }

    public void addPostEntityProcessingService(IPostEntityProcessingService eps) {
        this.postEntityProcessorList.add(eps);
    }

    public void removePostEntityProcessingService(IPostEntityProcessingService eps) {
        this.postEntityProcessorList.remove(eps);
    }

    public void addGamePluginService(IGamePluginService plugin) {
        this.gamePluginList.add(plugin);
        plugin.start(gameData, world);
    }

    public void removeGamePluginService(IGamePluginService plugin) {
        this.gamePluginList.remove(plugin);
        plugin.stop(gameData, world);
    }
    
    public void addHelp(IHelp plugin) {
        this.helpList.add(plugin);
    }

    public void removeHelp(IHelp plugin) {
        this.helpList.remove(plugin);
    }
    
    




}
