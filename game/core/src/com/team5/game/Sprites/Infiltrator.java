package com.team5.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.team5.game.Environment.SystemChecker;
import com.team5.game.MainGame;
import com.team5.game.Screens.PlayScreen;
import com.team5.game.Screens.WinScreen;
import com.team5.game.Sprites.Animation.Animator;
import com.team5.game.Sprites.Pathfinding.*;
import com.team5.game.Tools.Constants;
import com.team5.game.Tools.GameController;

import java.util.Random;

public class Infiltrator extends NPC{

    /*
    Infiltrator contains all of the information regarding an Infiltrator
    in the game with reference to the AI used for it's pathfinding
     */

    //MainGame reference
    MainGame game;

    //GameController reference
    GameController gameController;

    //States
    public boolean caught = false;
    boolean imprisoned = false;

    //AI reference
    InfiltratorAIBehaviour ai;

    boolean breaking;

    //SystemChecker reference
    SystemChecker systemChecker;

    //Audio
    Sound pass = Gdx.audio.newSound(Gdx.files.internal("Audio/Sound Effects/pass.wav"));

    // Runtime Errors - Added caught parameter when constructed for loading already caught infiltrators
    public Infiltrator(MainGame game, PlayScreen screen, GameController gameController, World world,
                       NodeGraph graph, Node node, Vector2 position) {
        /*stores all the variables needed to create an infiltrator
        parameters:
            MainGame game contains the game setting
            PlayScreen screen passes the screen that currently occupies the window
            GameController gameController passes the class that sets up the game
            World world contains the map
            NodeGraph graph gives the graph of all the possible targets on the map
            Node node gives the node the infiltator starts in
            Vector2 position gives the coordinates where the infiltrator starts in
         */
        super(screen, world, graph, node, position);
        this.game = game;

        this.gameController = gameController;

        systemChecker = gameController.getSystemChecker();

        ai = new InfiltratorAIBehaviour(gameController, this, graph, node);
    }

    //To be called every frame to move and animate the infiltrator.
    @Override
    public void update(float delta) {
        /*sets up the animations
        parameters:
            float delta gives time elapsing
         */
        if (!caught) {
            ai.update(delta);
            if (ai.isWaiting() && ai.isBreaking()){
                breaking = true;
                b2body.setLinearVelocity(0f, 0f);
                x = b2body.getPosition().x;
                y = b2body.getPosition().y;
                outlineButton.setPosition(x-4, y-4);

                anim.play("interact");
                outlineAnim.play("interact");

                currentSprite = anim.getSprite();
                outlineImage = new Image(outlineAnim.getSprite());
                outlineButton.getStyle().imageOver = outlineImage.getDrawable();
            } else {
                handleAnimations(direction);
            }

        } else {
            if (!imprisoned && caught && anim.finished()) {
                b2body.setTransform(gameController.getBrig().imprison(), 0);
                x = b2body.getPosition().x;
                y = b2body.getPosition().y;
                if (gameController.getBrig().allCaught()) {
                    game.setScreen(new WinScreen(game));
                }

                imprisoned = true;
            }
            currentSprite = anim.getSprite();
        }
    }

    //Sets up all the base Animations
    @Override
    public void setup() {
        /*sets up the animations*/
        Random random = new Random();
        int sprite = random.nextInt(6)+1;
        anim = new Animator("idle", "NPC/" + sprite + "/Idle");
        anim.add("run", "NPC/" + sprite + "/Run");
        anim.add("interact", "NPC/" + sprite + "/Interact");
        anim.add("caught", "NPC/Infiltrator/Caught");
        facingRight = true;
        currentSprite = anim.getSprite();

        //Setting outline animations
        outlineAnim = new Animator("idle", "NPC/" + sprite + "/IdleOutline");
        outlineAnim.add("run", "NPC/" + sprite +"/RunOutline");
        outlineAnim.add("interact", "NPC/" + sprite + "/InteractOutline");

        outlineImage = new Image(outlineAnim.getSprite());
        outlineButton = new ImageButton(new Image(Constants.ATLAS.findRegion("Empty")).getDrawable());

        outlineButton.setPosition(x-4, y-4);
        outlineButton.setSize(Constants.TILE_SIZE+8, Constants.TILE_SIZE+8);

        outlineButton.getStyle().imageOver = outlineImage.getDrawable();

        outlineButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                /*catch infiltrator and play corresponding animation*/
                if (!caught) {
                    pass.play(0.3f*Constants.volumeMultipler);
                    beenCaught();
                }
            }
        });

        screen.stage.addActor(outlineButton);
    }

    //Ability that changes the appearance
    public void changeSkin(){
        /*Changes the texture of the infiltrator to a random different one*/
        Random random = new Random();
        int sprite = random.nextInt(6)+1;
        anim = new Animator("idle", "NPC/" + sprite + "/Idle");
        anim.add("run", "NPC/" + sprite + "/Run");
        anim.add("interact", "NPC/" + sprite + "/Interact");
        anim.add("caught", "NPC/Infiltrator/Caught");
        facingRight = true;
        currentSprite = anim.getSprite();

        outlineAnim = new Animator("idle", "NPC/" + sprite + "/IdleOutline");
        outlineAnim.add("run", "NPC/" + sprite +"/RunOutline");
        outlineAnim.add("interact", "NPC/" + sprite + "/InteractOutline");

        outlineImage = new Image(outlineAnim.getSprite());
        outlineButton.getStyle().imageOver = outlineImage.getDrawable();
    }

    public void beenCaught(){
        /*changes the animation if it's been caught*/
        caught = true;
        anim.play("caught");
        outlineButton.getStyle().imageOver =
                new Image(Constants.ATLAS.findRegion("Empty")).getDrawable();
    }

    public void dispose(){
        /*removes the unneeded objects from memory*/
        pass.dispose();
        ai.dispose();
    }
}
