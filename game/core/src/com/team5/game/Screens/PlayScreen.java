package com.team5.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.team5.game.Tools.Constants;
import com.team5.game.Tools.CustomCamera;
import com.team5.game.Environment.Walls;
import com.team5.game.MainGame;
import com.team5.game.Tools.GameController;
import com.team5.game.UI.Hud;
import com.team5.game.UI.Minimap.Minimap;
import com.team5.game.UI.PauseMenu;

public class PlayScreen implements Screen {

    /*
    PlayScreen is the class that renders the main gameplay scene
    of the game, taking all the components from other entities.
     */

    //Game Reference
    private final MainGame game;

    //Tilemaps
    private final TmxMapLoader mapLoader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    //Colliders
    private final World world;
    private final Box2DDebugRenderer b2dr;

    //Stage
    public Stage stage;

    //HUD
    private Hud hud;
    private PauseMenu pauseMenu;
    private Minimap minimap;

    public boolean paused;
    public boolean mapVisible;

    //Audio
    Music music = Gdx.audio.newMusic(Gdx.files.internal("Audio/Music/song.wav"));

    float volume = 0.01f;

    //References
    private final Walls walls;
    public CustomCamera camera;
    public GameController gameController;

    // Team 25 - Load/Save state
    public boolean loadGame;

    public PlayScreen(MainGame game, boolean loadGame){
        /* loads the game and sets up the map, collisions, and sound*/
        this.game = game;

        //Tilemap
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("TileMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //Collisions
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        //Camera setup
        camera = new CustomCamera();

        //UI setup
        stage = new Stage(camera.port);
        Gdx.input.setInputProcessor(stage);

        //Game Controller
        gameController = new GameController(game, this, loadGame);
        camera.follow(gameController.getPlayer());

        //Collisions for TileMap
        walls = new Walls(world, map);

        //HUD
        hud = new Hud(this);
        pauseMenu = new PauseMenu(game, this);
        minimap = new Minimap(this, gameController.getTeleporters());

        //Audio
        music.setLooping(true);
        music.setVolume(volume* Constants.volumeMultipler);
        music.play();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        /*draws the map and the player*/
        checkPause();

        if (!paused && !mapVisible) {
            update(Gdx.graphics.getDeltaTime());
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        //Use b2dr.render(world, new Matrix4(camera.cam.combined)); to check collision boxes

        game.batch.setProjectionMatrix(camera.cam.combined);

        game.batch.begin();
        gameController.draw(game.batch);
        stage.act(delta);
        stage.draw();
        gameController.drawPlayer(game.batch);
        game.batch.end();

        hud.draw(delta);

        if (paused) {
            pauseMenu.draw(delta);
        }

        if (mapVisible){
            minimap.draw(delta);
        }

    }

    @Override
    public void resize(int width, int height) {
        /*adapts the camera to changes of window size*/
        camera.port.update(width, height);
    }

    @Override
    public void pause() {
        /*creates the pause menu over the top of the map*/
        pauseMenu.update();
        Gdx.input.setInputProcessor(pauseMenu.stage);
        music.pause();
        paused = true;
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        /*stops the music*/
        music.stop();
    }

    @Override
    public void dispose() {
        /*removes objects used from memory*/
        this.dispose();
        stage.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        minimap.dispose();
        gameController.dispose();
        music.dispose();
    }

    //Past here is all the methods I made

    public void update(float delta){
        /*makes the camera follow the player*/
        world.step(1/60f, 6, 2);

        gameController.update(delta);

        //Moves the camera to the player
        camera.update();
        camera.follow(gameController.getPlayer());

        //HUD
        hud.update();

        renderer.setView(camera.cam);
    }

    public void minimapOn(){
        /*creates the minimap that allows the player to teleport*/
        minimap.update();
        Gdx.input.setInputProcessor(minimap.stage);
        mapVisible = true;
    }

    public void minimapOff(){
        /*removes the minimap from the screen*/
        Gdx.input.setInputProcessor(stage);
        mapVisible = false;
    }

    void checkPause(){
        /*checks if the user has pressed the escape key and if so,
        creates the pause screen
         */
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            if (mapVisible){
                minimapOff();
            } else if (paused){
                music.play();
                Gdx.input.setInputProcessor(stage);
                paused = false;
            } else {
                music.pause();
                pauseMenu.update();
                Gdx.input.setInputProcessor(pauseMenu.stage);
                paused = true;
            }
        }
    }

    public World getWorld(){
        /*returns the map
        returns:
            World object, that hold the map.
         */
        return world;}
}
