package dk.sdu.mmmi;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
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
    private Stage stage;
    private Skin skin;
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
        stage = new Stage();
        //TODO få næste linje til at fungere
//        skin = new Skin(Gdx.files.external("/uiskin.json"));
        //Allows multiple inputprocessor
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(new GameInputProcessor(gameData));
        multiplexer.addProcessor(stage);


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
    
    /**
     * Draws menu
     */
    private void drawMenu(){
        //Menu field
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(255, 0, 0, 1);
        sr.rect(gameWidth, 0, Width, Height);
        sr.end();
        
        
        //Profile Picture field
        int spacing = 20;
        int proPicX = (100 - spacing*2)/2 + gameWidth + spacing;
        int proPicR = (100 - spacing*2)/2;
        int proPicY = Height - (spacing + proPicR);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0, 255, 0, 1);
        sr.circle(proPicX, proPicY, proPicR);
        sr.end();
        
     
        //Profile Name TextField   
        //TODO fix next line 
        skin = new Skin(Gdx.files.internal("C:\\Users\\tes_7\\OneDrive\\Skrivebord\\Wrong-Date\\Core\\src\\main\\java\\dk\\sdu\\mmmi\\uiskin.json"));
        String proNameStr = "Hello";
        TextField proNameTextField = new TextField(proNameStr, skin);
        int proNameX = proPicX + proPicR + spacing;
        int proNameLength = Width - proNameX - spacing;
        proNameTextField.setPosition(proNameX, proPicY);
        proNameTextField.setScale(.25f);
        proNameTextField.setSize(proNameLength , proPicR);
        stage.getActors().add(proNameTextField);
        
        //Weapon TextField
        String weapStr = "WEAPON";
        TextField weapTextField = new TextField(weapStr, skin);
        int weapLength = menuWidth - (2*spacing);
        int weapHeight = 25;
        int weapX = gameWidth + spacing;
        int weapY = proPicY - proPicR - spacing - weapHeight;
        weapTextField.setPosition(weapX, weapY);
        weapTextField.setScale(.25f);
        weapTextField.setSize(weapLength, weapHeight);
        stage.getActors().add(weapTextField);
        
        //Weapon Image
        Image weapImage = new Image(new Texture(Gdx.files.internal("C:\\Users\\tes_7\\OneDrive\\Skrivebord\\Wrong-Date\\Core\\src\\main\\java\\dk\\sdu\\mmmi\\White-square.jpg")));
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
        int invLength = menuWidth - (2*spacing);
        int invHeight = 25;
        int invX = gameWidth + spacing;
        int invY = weapImageY - weapImageHeight + spacing;
        invTextField.setPosition(invX, invY);
        invTextField.setScale(.25f);
        invTextField.setSize(invLength, invHeight);
        stage.getActors().add(invTextField);
        


   
        
        
        
        

            
        
    }

}
