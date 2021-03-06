package com.team5.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.team5.game.Screens.MainMenuScreen;
import com.team5.game.Tools.Constants;

public class MainGame extends Game {

	/*
	Main Game actually decides which screen will be shown at
	any given time.
	 */

	public SpriteBatch batch;
	public TextureAtlas atlas;
	private int level = 2;

	@Override
	public void create () {
		/*
		Creates screen allowing change to other screen through .setScreen
		 */
		batch = new SpriteBatch();
		atlas = Constants.ATLAS;
		this.level = 2;
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose(){
		batch.dispose();
		atlas.dispose();
	}
	public int getLevel(){
		/*
		getter for level (difficulty)
		returns:
			int this.level
		 */
		return this.level;
	}
	public void setLevel(int level){
		/*
		setter for level (difficulty)
		param:
			int level
		 */
		this.level = level;
	}
}
