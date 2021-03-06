package com.team5.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.team5.game.MainGame;
import com.team5.game.Tools.Constants;
import com.team5.game.Tools.CustomCamera;

public class LoseScreen implements Screen {

    /*
    LoseScreen is the class that renders the mission failed screen
    and either transfers over to the PlayScreen or the MainMenuScreen
     */

    //Main Game Reference
    MainGame game;

    //Menu buttons
    ImageButton playButton;
    ImageButton quitButton;

    Stage stage;

    Texture title;

    //Audio
    Sound click = Gdx.audio.newSound(Gdx.files.internal("Audio/Sound Effects/click.wav"));

    //Menu positions
    Vector2 playPos = new Vector2(Constants.CAMERA_WIDTH/2-48, 60);
    Vector2 quitPos = new Vector2(Constants.CAMERA_WIDTH/2-48, 20);
    Vector2 titlePos = new Vector2(Constants.CAMERA_WIDTH/2-96, 100);

    //Reference
    private final CustomCamera camera;

    public LoseScreen (final MainGame game){
        /*creates the screen that tells the player they've lost and
        provides buttons to let the user go back to main menu or set up a new game
         */

        this.game = game;
        title = new Texture("Sprites/Menu/MissionFailed.png");

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
        /* draws all the objects that are part of the interface into the window*/
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.cam.combined);

        game.batch.begin();
        game.batch.draw(title, titlePos.x, titlePos.y);
        game.batch.end();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        /*allows the interface to adapt to a change in window size.*/
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
        /*removes the objects used from memory*/
        this.dispose();
        stage.dispose();
        click.dispose();
    }

    //Custom functions from here

    public void update(float delta){
        /*updates the interface*/
        //Updates Camera
        camera.update();
    }

    void setupButtons(){
        /*prepares textures and settings for the buttons that let the user play again
         or exit the game
         */
        stage = new Stage(camera.port);
        Gdx.input.setInputProcessor(stage);

        playButton = new ImageButton(new Image(new Texture("Sprites/Menu/PlayOff.png")).getDrawable());
        quitButton = new ImageButton(new Image(new Texture("Sprites/Menu/MenuOff.png")).getDrawable());

        playButton.setPosition(playPos.x, playPos.y);
        quitButton.setPosition(quitPos.x, quitPos.y);

        playButton.setSize(96, 32);
        quitButton.setSize(96, 32);

        playButton.getStyle().imageOver = new Image(new Texture("Sprites/Menu/PlayOn.png")).getDrawable();
        quitButton.getStyle().imageOver = new Image(new Texture("Sprites/Menu/MenuOn.png")).getDrawable();

        stage.addActor(playButton);
        stage.addActor(quitButton);

        playButton.addListener(new ClickListener(){
            /*creates a new game*/
            public void clicked(InputEvent event, float x, float y){
                /*creates a new game*/
                click.play(0.5f*Constants.volumeMultipler, 1.5f, 0);
                game.setScreen(new PlayScreen(game, false)); //change later
            }
        });

        quitButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                /*sends the user back to the main menu*/
                click.play(0.5f*Constants.volumeMultipler, 1.5f, 0);
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

}
