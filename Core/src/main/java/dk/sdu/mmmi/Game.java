package dk.sdu.mmmi;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.services.IEntityProcessingService;
import dk.sdu.mmmi.common.services.IGamePluginService;
import dk.sdu.mmmi.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.core.managers.GameInputProcessor;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; 

public class Game implements ApplicationListener {

    private static int gameWidth = 800;
    private static int menuWidth = 300;
    private static int Width = gameWidth + menuWidth;
    private static int Height = 600;
    
    private static OrthographicCamera cam;
    private ShapeRenderer sr;
    private SpriteBatch batch; //For sprites
    private Stage stage;
    private final GameData gameData = new GameData();
    private final Menu menu = new Menu();
    private static World world = new World();
    private static final List<IEntityProcessingService> entityProcessorList = new CopyOnWriteArrayList<>();
    private static final List<IGamePluginService> gamePluginList = new CopyOnWriteArrayList<>();
    private static List<IPostEntityProcessingService> postEntityProcessorList = new CopyOnWriteArrayList<>();

    public Game(){
        init();
        gameData.setDisplayWidth(gameWidth);
        gameData.setDisplayHeight(Height);
        menu.setWidth(menuWidth);
        menu.setHeight(Height);
        menu.setWindowWidth(Width);
    }

    public void init() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Wrong-Date";
        cfg.width = Width;
        cfg.height = Height;
        cfg.useGL30 = false;
        cfg.resizable = false;

        new LwjglApplication(this, cfg);
    }

    @Override
    public void create() {
        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        
        stage = new Stage();
        
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(new GameInputProcessor(gameData));
        multiplexer.addProcessor(stage);

//        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameData.setDelta(Gdx.graphics.getDeltaTime());
        update();
        draw();
        gameData.getKeys().update();
        stage.draw();
        stage.act();
    }

    private void update() {
        // Update
        for (IEntityProcessingService entityProcessorService : entityProcessorList) {
            entityProcessorService.process(gameData, world);
        }

        // Post Update
        for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessorList) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private void draw() {
        drawMenu();
        
        for (Entity entity : world.getEntities()) {
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
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
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
    
    private void drawMenu(){
        //Menu field
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(255, 0, 0, 1);
        sr.rect(gameWidth, 0, Width, Height);
        sr.end();
        
        
        //Picture field
        int spacing = 20;
        int x = (100 - spacing*2)/2 + gameWidth + spacing;
        int r = (100 - spacing)/2;
        int y = Height - (spacing + r);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0, 255, 0, 1);
        sr.circle(x, y, r);
        sr.end();
        
        //Character name   
        Skin skin = new Skin(Gdx.files.internal("C:\\Users\\tes_7\\OneDrive\\Skrivebord\\Wrong-Date\\Core\\src\\main\\java\\dk\\sdu\\mmmi\\uiskin.json"));
        TextField name = new TextField("Hello", skin);
        int textX = x + r + spacing;
        int textLength = Width - textX - spacing;
        name.setPosition(textX, y);
        name.setScale(.25f);
        name.setSize(textLength , r);
        
        stage.getActors().add(name);
        

            
        
    }

}
