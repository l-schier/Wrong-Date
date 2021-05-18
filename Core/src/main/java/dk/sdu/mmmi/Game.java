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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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

    private SpriteBatch batch;

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
        //cfg.useGL30 = false;
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
        stage = new Stage(new ScreenViewport()); // new StretchViewport(gameData.getDisplayWidth(), gameData.getDisplayHeight())
        stage.getViewport().apply();
        skin = new Skin(Gdx.files.internal(Gdx.files.getLocalStoragePath() + "comic-ui.json"));
        batch = new SpriteBatch();
        menu = new Menu(Width, gameWidth, Height, skin, stage, world);

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
        float toX = fromX + gameData.getDisplayWidth();
        float fromY = gameData.getCamY() - (gameData.getDisplayHeight() / 2);
        float toY = fromY + gameData.getDisplayHeight();

        Texture img = new Texture(Gdx.files.getLocalStoragePath() + "floor.png");
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
            Texture d = new Texture(Gdx.files.getLocalStoragePath() + doors.getSpritePath());
            Texture w = new Texture(Gdx.files.getLocalStoragePath() + render.getSpritePath());
            w.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

            batch.setProjectionMatrix(vp.getCamera().combined);
            batch.begin();
            batch.draw(w, wall.getStartX() - 32, wall.getStartY() - 32, 0, 0, w.getWidth(), (int) wall.getEndY());
            batch.draw(w, wall.getStartX(), wall.getStartY() - 32, 0, 0, (int) wall.getEndX() + 32, w.getHeight());
            batch.draw(w, wall.getStartX() - 32, wall.getEndY(), 0, 0, (int) wall.getEndX(), w.getHeight());
            batch.draw(w, wall.getEndX(), wall.getStartY(), 0, 0, w.getWidth(), (int) wall.getEndY() + 32);

            for (float[] door : doors.getDoors()) {
                float x = door[0];
                float y = door[1];

                if (door[0] == wall.getStartX()) { // left door
                    batch.draw(d, x - 32, y, 0, 0, d.getWidth(), d.getHeight());
                } else if (door[0] == wall.getEndX()) { // right door                        
                    batch.draw(d, x, y, 0, 0, d.getWidth(), d.getHeight());
                } else if (door[1] == wall.getStartY()) { // bottom door
                    batch.draw(d, x, y - 32, 0, 0, d.getWidth(), d.getHeight());
                } else if (door[1] == wall.getEndY()) { // top door
                    batch.draw(d, x, y, 0, 0, d.getWidth(), d.getHeight());
                }
            }

            batch.end();
        } catch (GdxRuntimeException e) {
            System.out.println("Image not found");
        }
    }

    private void draw() {

        // background
        drawBackground();

        // entities
        for (Entity entity : world.getEntities()) {

            // walls and doors
            if (entity.getPart(WallPart.class) != null
                    && entity.getPart(DoorPart.class) != null
                    && entity.getPart(RenderPart.class) != null) {
                drawWallsAndDoors(entity);
                continue;
            }

            // anything else
            if (entity.getPart(RenderPart.class) != null && entity.getPart(PositionPart.class) != null) {
                PositionPart pos = entity.getPart(PositionPart.class);
                RenderPart render = entity.getPart(RenderPart.class);

                if (!render.isVisible()) {
                    continue;
                }

                try {
                    Texture img = getTexture(render.getSpritePath());

                    batch.setProjectionMatrix(vp.getCamera().combined);
                    batch.begin();
                    batch.draw(img, pos.getX() - 16, pos.getY() - 16);
                    batch.end();
                } catch (GdxRuntimeException e) {
                    System.out.println("Image not found");
                }

                continue;
            }

            // shape render (vector) fallback
            sr.setColor(1, 1, 1, 1);

            sr.setProjectionMatrix(vp.getCamera().combined);
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
                sr.setColor(1, 0, 0, 1);

                DoorPart doorPart = entity.getPart(DoorPart.class);
                float[][] doors = doorPart.getDoors();
                for (int i = 0; i < 4; i++) {
                    sr.setProjectionMatrix(vp.getCamera().combined);
                    sr.begin(ShapeRenderer.ShapeType.Line);
                    sr.line(doors[i][0], doors[i][1], doors[i][2], doors[i][3]);
                    sr.end();
                }

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
