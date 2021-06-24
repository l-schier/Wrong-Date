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
import dk.sdu.mmmi.core.managers.GameInputProcessor;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dk.sdu.mmmi.common.data.entityparts.InventoryPart;
import dk.sdu.mmmi.common.data.entityparts.KeyPart;
import dk.sdu.mmmi.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.common.data.entityparts.PlayerPart;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.data.entityparts.RenderPart;
import dk.sdu.mmmi.common.data.entityparts.WallPart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import dk.sdu.mmmi.common.services.IEntityPostProcessingService;
import java.util.ArrayList;
import java.util.HashMap;

public class Game implements ApplicationListener {

    private final int gameWidth = 800;
    private final int menuWidth = 300;
    private final int Width = gameWidth + menuWidth;
    private final int Height = 600;

    private ShapeRenderer sr;
    private OrthographicCamera cam;
    private Viewport vp;
    private Stage stage;
    private Skin skin;
    private Menu menu;

    private boolean isPaused;

    private final GameData gameData = new GameData();
    private final World world = new World();

    private final List<IGamePluginService> gamePluginList = new CopyOnWriteArrayList<>();
    private final List<IEntityProcessingService> entityProcessorList = new CopyOnWriteArrayList<>();
    private final List<IEntityPostProcessingService> postEntityProcessorList = new CopyOnWriteArrayList<>();

    private final HashMap<String, Texture> entityTextures = new HashMap<String, Texture>();
    private final HashMap<String, Animation> enityAnimation = new HashMap<>();
    private final HashMap<String, TextureRegion> entityTextureRegion = new HashMap<>();

    private SpriteBatch batch;
    private final int humanTextureHeight = 64;
    private final int humanTextureWidth = 64;
    private float elapsedTime;
    private Texture textureSheet;

    public Game() {
        init();
        gameData.setDisplayWidth(gameWidth);
        gameData.setDisplayHeight(Height);
        gameData.setMenuWidth(menuWidth);
    }

    private void init() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Wrong-Date";
        cfg.width = Width;
        cfg.height = Height;
        cfg.resizable = false;

        new LwjglApplication(this, cfg);

        //Use "dir /b > filenames.txt" to crearte file and remove redundant files within
        copyFile("filenames.txt");

        try {
            File fileNames = new File(Gdx.files.getLocalStoragePath() + "filenames.txt");
            Scanner sc = new Scanner(fileNames);

            while (sc.hasNextLine()) {
                copyFile(sc.nextLine());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void copyFile(String fileName) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        File temp = new File(fileName);

        // copy module sprites to runner folder
        try {
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
        }
    }

    @Override
    public void create() {
        gameData.setCamX(gameData.getDisplayWidth() / 2 + menuWidth);
        gameData.setCamY(gameData.getDisplayHeight() / 2);
        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        vp = new ScreenViewport(cam);
        vp.apply();
        cam.translate(gameData.getCamX(), gameData.getCamY(), 0);

        sr = new ShapeRenderer();
        stage = new Stage(new ScreenViewport());
        stage.getViewport().apply();
        skin = new Skin(Gdx.files.internal(Gdx.files.getLocalStoragePath() + "comic-ui.json"));
        batch = new SpriteBatch();

        menu = new Menu(Width, gameWidth, Height, skin, stage, world, this);

        //Allows multiple inputprocessor
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(new GameInputProcessor(gameData));
        multiplexer.addProcessor(stage);
    }

    @Override
    public void render() {
        if (!isPaused) {
            vp.getCamera().position.set((float) gameData.getCamX(), (float) gameData.getCamY(), 0);
            vp.getCamera().update();
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            gameData.setDelta(Gdx.graphics.getDeltaTime());
            update();
            draw();
            gameData.getKeys().update();
            menu.update();
        }
        stage.draw();
        stage.act();
        //cheking if pause
        if (menu.getPause()) {
            pause();
            menu.resetPause();
        }

        //Cheking if resume
        if (menu.getResume()) {
            resume();
            menu.resetResume();
        }
    }

    private void update() {
        // Update
        for (IEntityProcessingService entityProcessorService : entityProcessorList) {
            entityProcessorService.process(gameData, world);
        }

        // Post Update
        for (IEntityPostProcessingService postEntityProcessorService : postEntityProcessorList) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private Texture getTexture(String spritePath) {
        // reuse texture
        if (this.entityTextures.containsKey(spritePath)) {
            return this.entityTextures.get(spritePath);
        }

        // create new texture
        Texture img = new Texture(Gdx.files.getLocalStoragePath() + spritePath);
        this.entityTextures.put(spritePath, img);
        return img;
    }

    private void drawBackground() {
        float fromX = gameData.getCamX() - (gameData.getDisplayWidth() / 2 + menuWidth);
        float toX = gameData.getDisplayWidth();
        float fromY = gameData.getCamY() - (gameData.getDisplayHeight() / 2);
        float toY = gameData.getDisplayHeight();

        Texture img = getTexture("floor.png");
        img.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        batch.setProjectionMatrix(vp.getCamera().combined);
        batch.begin();
        batch.draw(img, fromX, fromY, 0, 0, (int) toX, (int) toY);
        batch.end();
    }

    private void drawWallsAndDoors(Entity entity) {
        WallPart wall = entity.getPart(WallPart.class);
        DoorPart doors = entity.getPart(DoorPart.class);
        RenderPart render = entity.getPart(RenderPart.class);

        try {
            batch.setProjectionMatrix(vp.getCamera().combined);
            batch.begin();

            int fromX, fromY, toX, toY;
            int wHeight = 32;
            int wWidth = 32;

            // left wall
            TextureRegion leftWall = getTextureRegion(render.getSpritePath(), 1, 3, wWidth, wHeight);
            fromX = (int) wall.getStartX() - wWidth;
            fromY = (int) wall.getStartY();
            toY = (int) wall.getEndY();
            for (int y = fromY; y < toY; y += wHeight) {
                batch.draw(leftWall, fromX, y);
            }

            // right wall
            TextureRegion rightWall = getTextureRegion(render.getSpritePath(), 4, 2, wWidth, wHeight);
            fromX = (int) wall.getEndX();
            fromY = (int) wall.getStartY();
            toY = (int) wall.getEndY();
            for (int y = fromY; y < toY; y += wHeight) {
                batch.draw(rightWall, fromX, y);
            }

            // top wall
            TextureRegion topWall = getTextureRegion(render.getSpritePath(), 2, 1, wWidth, wHeight);
            fromX = (int) wall.getStartX();
            fromY = (int) wall.getEndY();
            toX = (int) wall.getEndX();
            for (int x = fromX; x < toX; x += wWidth) {
                batch.draw(topWall, x, fromY);
            }

            //top corners
            TextureRegion topRightWall = getTextureRegion(render.getSpritePath(), 4, 1, wWidth, wHeight);
            batch.draw(topRightWall, toX, fromY);
            TextureRegion topLeftWall = getTextureRegion(render.getSpritePath(), 1, 1, wWidth, wHeight);
            batch.draw(topLeftWall, fromX - wWidth, fromY);

            // bottom wall
            TextureRegion bottomWall = getTextureRegion(render.getSpritePath(), 2, 1, wWidth, wHeight);
            fromX = (int) wall.getStartX();
            fromY = (int) wall.getStartY() - wHeight;
            toX = (int) wall.getEndX();
            for (int x = fromX; x < toX; x += wWidth) {
                batch.draw(bottomWall, x, fromY);
            }

            //bottom cornerns
            TextureRegion bottomRightWall = getTextureRegion(render.getSpritePath(), 4, 4, wWidth, wHeight);
            batch.draw(bottomRightWall, toX, fromY);
            TextureRegion bottomLeftWall = getTextureRegion(render.getSpritePath(), 1, 4, wWidth, wHeight);
            batch.draw(bottomLeftWall, fromX - wWidth, fromY);

            Texture imgD = getTexture(doors.getSpritePath());
            int dHeight = 32;
            int dWidth = 32;

            Entity player = null;
            for (Entity e : world.getEntities()) {
                if (e.getPart(PlayerPart.class) != null) {
                    player = e;
                    break;
                }
            }
            ArrayList<KeyPart> keys = null;
            if (player != null) {
                InventoryPart inv = player.getPart(InventoryPart.class);
                if (inv != null) {
                    keys = inv.getKeyParts();
                }
            }
            KeyPart.KeyColor doorColor = doors.getLockColor();
            int leftX = 0; 
            int rightX = 2; 
            int topBottomX = 1;
            int colorY = 0; 
            for (float[] door : doors.getDoors()) {
                float x = door[0];
                float y = door[1];

                if (keys != null) {
                    boolean hasKey = false;
                    for (KeyPart k : keys) {
                        if (k.getColor() == doorColor) {
                            hasKey = true;
                        }
                    }
                    if (hasKey) {
                        colorY = 0; 
                    } else {
                        if(null == doorColor){
                            
                        }else switch (doorColor) {
                            case Gold:
                                colorY = 1;
                                break;
                            case Silver:
                                colorY = 2; 
                                break;
                            case Bronze:
                                colorY = 3; 
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    if(null == doorColor){
                            
                        }else switch (doorColor) {
                            case Gold:
                                colorY = 1;
                                break;
                            case Silver:
                                colorY = 2; 
                                break;
                            case Bronze:
                                colorY = 3; 
                                break;
                            default:
                                break;
                        }
                }

                if (door[0] == wall.getStartX()) { // left door
                    batch.draw(imgD, x - 32, y, leftX, colorY * dHeight, dWidth, dHeight);
                } else if (door[0] == wall.getEndX()) { // right door  
                    batch.draw(imgD, x, y, rightX * dWidth, colorY * dHeight, dWidth, dHeight);
                } else if (door[1] == wall.getStartY()) { // ottom door
                    batch.draw(imgD, x, y - 32, topBottomX * dWidth, colorY * dHeight, dWidth, dHeight);
                } else if (door[1] == wall.getEndY()) { // top door
                    batch.draw(imgD, x, y, topBottomX * dWidth, colorY * dHeight, dWidth, dHeight);
                }
            }

            batch.end();
        } catch (GdxRuntimeException e) {
            System.out.println("Image not found");
        }
    }

    private Animation getAnimationFromTextureRange(String spritePath, int col) {
        String id = "" + spritePath + col;
        if (this.enityAnimation.containsKey(id)) {
            return this.enityAnimation.get(id);
        } else {
            Array<TextureRegion> frames = new Array<>();
            for (int i = col - 1; i < col + 3; i++) {
                frames.add(new TextureRegion(getTexture(spritePath), i * humanTextureWidth, 0, humanTextureWidth, humanTextureHeight));
            }
            Animation newAnim = new Animation(1f / 4f, frames);
            enityAnimation.put(id, newAnim);
            return newAnim;
        }
    }

    public TextureRegion getTextureRegion(String spritePath, int col, int row, int width, int height) {
        String id = "" + spritePath + col + row;
        if (this.entityTextureRegion.containsKey(id)) {
            return this.entityTextureRegion.get(id);
        } else {
            TextureRegion region = new TextureRegion(getTexture(spritePath), (col * width) - width, (row * height) - height, width, height);
            entityTextureRegion.put(id, region);
            return region;
        }
    }

    private void draw() {

        // background
        drawBackground();

        // entities
        for (Entity entity : world.getEntities()) {

            // anything else
            if (entity.getPart(RenderPart.class) != null && entity.getPart(PositionPart.class) != null) {
                PositionPart pos = entity.getPart(PositionPart.class);
                RenderPart render = entity.getPart(RenderPart.class);

                if (!render.isVisible()) {
                    continue;
                }

                // for player and enemy
                if (entity.getPart(MovingPart.class) != null) {
                    try {
                        textureSheet = getTexture(render.getSpritePath());

                        Animation backWalk = getAnimationFromTextureRange(render.getSpritePath(), 1);
                        Animation frontWalk = getAnimationFromTextureRange(render.getSpritePath(), 5);
                        Animation leftWalk = getAnimationFromTextureRange(render.getSpritePath(), 9);
                        Animation rightWalk = getAnimationFromTextureRange(render.getSpritePath(), 13);

                        MovingPart entityMovingPart = entity.getPart(MovingPart.class);

                        elapsedTime += Gdx.graphics.getDeltaTime();

                        batch.setProjectionMatrix(vp.getCamera().combined);
                        batch.begin();

                        if (entityMovingPart.isRight()) {
                            batch.draw((TextureRegion) rightWalk.getKeyFrame(elapsedTime, true), pos.getX() - 32, pos.getY() - 32);
                        } else if (entityMovingPart.isLeft()) {
                            batch.draw((TextureRegion) leftWalk.getKeyFrame(elapsedTime, true), pos.getX() - 32, pos.getY() - 32);
                        } else if (entityMovingPart.isUp()) {
                            batch.draw((TextureRegion) backWalk.getKeyFrame(elapsedTime, true), pos.getX() - 32, pos.getY() - 32);
                        } else if (entityMovingPart.isDown()) {
                            batch.draw((TextureRegion) frontWalk.getKeyFrame(elapsedTime, true), pos.getX() - 32, pos.getY() - 32);
                        } else {
                            TextureRegion stand = new TextureRegion(textureSheet, 4 * humanTextureWidth, 0, humanTextureWidth, humanTextureWidth);
                            batch.draw(stand, pos.getX() - 32, pos.getY() - 32);
                        }

                        batch.end();

                    } catch (GdxRuntimeException e) {
                        System.out.println("Image not found");
                    }
                    // for everything else
                } else {
                    try {
                        if (entity.getPart(WallPart.class) == null) {
                            Texture img = getTexture(render.getSpritePath());

                            batch.setProjectionMatrix(vp.getCamera().combined);
                            batch.begin();
                            batch.draw(img, pos.getX() - 16, pos.getY() - 16);
                            batch.end();
                        }

                    } catch (GdxRuntimeException e) {
                        System.out.println("Image not found");
                    }
                }

                // all other walls and doors
                if (entity.getPart(WallPart.class) != null
                        && entity.getPart(DoorPart.class) != null
                        && entity.getPart(RenderPart.class) != null) {
                    drawWallsAndDoors(entity);
                    continue;
                }

                continue;
            }
        }

        // hardcoded obstacle
        float boxStartX = 100;
        float boxStopX = 200;
        float boxStartY = 100;
        float boxStopY = 200;

        sr.setColor(1, 0, 0, 1);
        sr.setProjectionMatrix(vp.getCamera().combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(boxStartX, boxStartY, boxStartX, boxStopY);
        sr.line(boxStartX, boxStopY, boxStopX, boxStopY);
        sr.line(boxStopX, boxStopY, boxStopX, boxStartY);
        sr.line(boxStopX, boxStartY, boxStartX, boxStartY);
        sr.end();
    }

    @Override
    public void resize(int width, int height) {
        vp.update(width + menuWidth, height);
        stage.getViewport().update(width + menuWidth, height);
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
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

    public void addPostEntityProcessingService(IEntityPostProcessingService eps) {
        this.postEntityProcessorList.add(eps);
    }

    public void removePostEntityProcessingService(IEntityPostProcessingService eps) {
        this.postEntityProcessorList.remove(eps);
    }

    public void addGamePluginService(IGamePluginService plugin) {
        this.gamePluginList.add(plugin);
        plugin.start(gameData, world);
    }

    public void removeGamePluginService(IGamePluginService plugin) {
        this.gamePluginList.remove(plugin);
        plugin.stop(gameData, world);
        String[] sprites = plugin.getSpritePaths();
        for (String sprite : sprites) {
            if (this.entityTextures.containsKey(sprite)) {
                this.entityTextures.remove(sprite);
            }
        }
    }
}
