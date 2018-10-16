package com.dartmouth.treasurehunter.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dartmouth.treasurehunter.GameModels.Move;
import com.dartmouth.treasurehunter.GameModels.THGame;
import com.dartmouth.treasurehunter.THMain;

import java.io.IOException;
import java.util.Locale;

//Screen for testing purpose
//By Aaron

public class MainScreen implements Screen {

    private OrthographicCamera camera;
    private Stage stage;
    private THGame thGame;


    private final THMain game;
    public MainScreen(final THMain game){
        this.game=game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);



        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton hostButton = new TextButton("Host", game.skin);
        final TextButton joinButton = new TextButton("Join", game.skin);
        final TextButton optionsButton = new TextButton("Options", game.skin);
        final Label message = new Label("Message",game.skin);
        table.add(message);
        table.row();
        addButton(hostButton,table);
        addButton(joinButton,table);
        addButton(optionsButton,table);

        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    game.message.setDataGame("HelloWorld");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            });
        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String code =game.message.getDataGame();
                if (code!=null){
                    message.setText(code);
                }
            }
        });

        // Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }
    private void addButton(Button button, Table table){

        table.add(button).minHeight(100).minWidth(200).prefHeight(200).prefWidth(1000).padBottom(50);
        table.row();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
//        game.batch.setProjectionMatrix(camera.combined);
//
//        game.batch.begin();
//        game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
//        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
//        game.batch.end();
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    }
}
