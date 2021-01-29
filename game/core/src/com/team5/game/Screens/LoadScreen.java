package com.team5.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.team5.game.MainGame;
import com.team5.game.Tools.Constants;
import com.team5.game.Tools.CustomCamera;

public class LoadScreen implements Screen {

    //Game Reference
    public MainGame game;

    //Menu buttons for loading new or old game
    ImageButton newButton, loadButton, backButton;

    Stage stage;

    Texture title;

    //Audio
    Sound click = Gdx.audio.newSound(Gdx.files.internal("Audio/Sound Effects/click.wav"));

    //Menu positions
    Vector2 newPos = new Vector2(Constants.CAMERA_WIDTH/2-48, 65);
    Vector2 loadPos = new Vector2(Constants.CAMERA_WIDTH/2-48, 5);
    Vector2 backPos = new Vector2(Constants.CAMERA_WIDTH/2-48, 35);
    Vector2 titlePos = new Vector2(Constants.CAMERA_WIDTH/2-120, 105);

    //NPC number
    private int noNPCs;

    //Colliders
    private final World world;
    private final Box2DDebugRenderer b2dr;


    //Reference
    private final CustomCamera camera;



    public LoadScreen(final MainGame game, int noNPCs){

        this.game = game;
        title = new Texture("Sprites/Menu/Title.png");

        //Collisions
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        //Camera
        camera = new CustomCamera();

        //Buttons
        setupButtons();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, new Matrix4(camera.cam.combined));

        game.batch.setProjectionMatrix(camera.cam.combined);

        game.batch.begin();
        game.batch.draw(title, titlePos.x, titlePos.y);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.port.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        b2dr.dispose();
        click.dispose();
        this.dispose();
    }

    public void update(float delta){
        world.step(1/60f, 6, 2);

        //Updates Camera
        camera.update();
    }


    void setupButtons(){
        stage = new Stage(camera.port);
        Gdx.input.setInputProcessor(stage);

        newButton= new ImageButton(new Image(new Texture("Sprites/Menu/NewOff.png")).getDrawable());
        loadButton= new ImageButton(new Image(new Texture("Sprites/Menu/LoadOff.png")).getDrawable());
        backButton= new ImageButton(new Image(new Texture("Sprites/Menu/BackOff.png")).getDrawable());

        newButton.setPosition(newPos.x, newPos.y);
        loadButton.setPosition(backPos.x, backPos.y);
        backButton.setPosition(loadPos.x, loadPos.y);

        newButton.setSize(90, 30);
        loadButton.setSize(90, 30);
        backButton.setSize(90, 30);

        newButton.getStyle().imageOver = new Image(new Texture("Sprites/Menu/NewOn.png")).getDrawable();
        loadButton.getStyle().imageOver = new Image(new Texture("Sprites/Menu/LoadOn.png")).getDrawable();
        backButton.getStyle().imageOver = new Image(new Texture("Sprites/Menu/BackOn.png")).getDrawable();

        stage.addActor(newButton);
        stage.addActor(loadButton);
        stage.addActor(backButton);

        newButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                click.play(0.5f, 1.5f, 0);
                game.setScreen(new PlayScreen(game,noNPCs));
            }
        });
        loadButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                click.play(0.5f, 1.5f, 0);
                game.setScreen(new PlayScreen(game,noNPCs));
            }
        });
        backButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                click.play(0.5f, 1.5f, 0);
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }
}