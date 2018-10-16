package com.dartmouth.treasurehunter;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.dartmouth.treasurehunter.Bluetooth.BluetoothInterface;
import com.dartmouth.treasurehunter.Screens.ExplorerScreen;
import com.dartmouth.treasurehunter.Screens.NavigatorScreen;

//By Aaron
//Goes to this when Game is Opened

public class THMain extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Skin skin;
	public BluetoothInterface message;
	private boolean isExplorer=true;
	public int monDen=8;
	public int playerFreq=4;


	THMain(BluetoothInterface message,boolean isExplorer,int monDen,int playerFreq){
		this.message=message;
		this.isExplorer=isExplorer;
		if (monDen<64&&monDen>4) {
			this.monDen = monDen;
		}
		if (playerFreq>0) {
			this.playerFreq = playerFreq;
		}
	}
	THMain(BluetoothInterface message){
		this.message=message;
	}
	THMain(){}

	@Override
	public void create () {

		batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("level-plane-ui.json"));

        //Go to explorer
		if (isExplorer){
            this.setScreen(new ExplorerScreen(this));
        }
        //got to Navigator
        else{
		    this.setScreen(new NavigatorScreen(this));
        }
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		skin.dispose();
		batch.dispose();
	}
}
