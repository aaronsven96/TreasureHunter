package com.dartmouth.treasurehunter.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dartmouth.treasurehunter.THMain;

//By Aaron
//Victory Screen

public class EndScreen implements Screen {

    private Stage stage;
    private boolean isWin;
    private THMain game;
    private ScreenViewport hubViewport;

    EndScreen(boolean isWin, THMain game){
        this.game=game;
        this.isWin=isWin;
    }

    @Override
    public void show() {
        hubViewport = new ScreenViewport(new OrthographicCamera());

        stage=new Stage(hubViewport);
        Table table= new Table();
        table.setFillParent(true);

        BitmapFont font =new BitmapFont();

        font.getData().setScale(4,4);
        Label.LabelStyle style =new Label.LabelStyle(font, Color.WHITE);
//        final TextButton leftButton = new TextButton("left",game.skin);
//        leftButton.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                game.dispose();
//            }
//        });
        Label whoWon;
        if (isWin){
            whoWon=new Label("Winner!",style);
        }
        else{
            whoWon=new Label("Loser!",style);
        }
        table.add(whoWon).expand();
        table.row();
        //table.add(leftButton).minWidth(100).prefWidth(200).minHeight(100).prefHeight(200);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        hubViewport.update(width,height);
        stage.getCamera().viewportWidth=width;
        stage.getCamera().viewportHeight=height;
        stage.getCamera().update();
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
