package com.dartmouth.treasurehunter.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dartmouth.treasurehunter.GameModels.Position;
import com.dartmouth.treasurehunter.THMain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

// By Aaron
// Runs Explorer Game

public class ExplorerScreen implements Screen {

    //stage for ui actors
    private Stage stage;

    //Camera and renderer
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;

    //View for Game
    private FitViewport viewport;

    //View for Ui
    private ScreenViewport hubViewport;
    private final THMain game;

    //position of explorer
    private Position posExplorer;

    //keeps track of hurdle
    private boolean isHurdle=false;
    private boolean canHurdle=true;

    private float time=0;//time

    private TiledMap map;//map to render

    //keeps track of frames to render for hero
    private int nextFrame=0;
    private int curFrame=0;

    //Textures
    private Texture[] heroTexts;
    private Texture[]monTexts;

    //change in movement
    private int curX;
    private int curY;

    //Labels that are drawn in ui
    private Label currentMove;
    private Label timer;
    private Label hurdle;

    //Time to next hurdle
    private float hurdleTime;
    private TextButton hurdleButton;

    //positions of monsters
    private ArrayList<Position> monPositions;
    final static private int turnTime=2;

    final static private int hurdleCooldown=4;

    public ExplorerScreen(THMain game){
        this.game=game;
    }

    @Override
    public void show() {

        loadTextures();//loads texutres

        //sets renderer and camera
        float unitScale = 1 / 16f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        camera = new OrthographicCamera();
        renderer.setView(camera);
        camera.setToOrtho(false, 64, 64);
        //camera.zoom =camera.zoom/21.33333f;
        camera.zoom =camera.zoom/16f;


        //sets viewports
        hubViewport = new ScreenViewport(new OrthographicCamera());
        viewport =new FitViewport(64,64,camera);


        //initial position for explorer
        posExplorer=new Position(0,0);

        //make stage
        stage = new Stage(hubViewport);
        //input taken from stage
        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table rootTable = new Table();

        rootTable.setFillParent(true);

        Table conTable = new Table();
        BitmapFont font =new BitmapFont();

        font.getData().setScale(4,4);
        Label.LabelStyle style =new Label.LabelStyle(font, Color.WHITE);

        currentMove= new Label("Hello",style);
        timer =new Label("Time",style);
        hurdle=new Label("Time",style);

        rootTable.add(timer).expandX().center();
        rootTable.row();
        rootTable.add(currentMove).expandX().center();
        rootTable.row();
        rootTable.add(hurdle).expandX().center();
        rootTable.row();
        rootTable.add(conTable).expand().bottom();
        stage.addActor(rootTable);


        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".

        final TextButton upButton = new TextButton("Up", game.skin);
        final TextButton downButton = new TextButton("down", game.skin);
        final TextButton rightButton = new TextButton("right", game.skin);
        final TextButton leftButton = new TextButton("left",game.skin);
        hurdleButton = new TextButton("Hurdle",game.skin);

        conTable.add();
        conTable.add(upButton).minWidth(100).prefWidth(200).minHeight(100).prefHeight(200);
        conTable.row();
        conTable.add(leftButton).minWidth(100).prefWidth(200).minHeight(100).prefHeight(200);
        conTable.add(hurdleButton).minWidth(100).prefWidth(200).minHeight(100).prefHeight(200);
        conTable.add(rightButton).minWidth(100).prefWidth(200).minHeight(100).prefHeight(200);
        conTable.row();
        conTable.add();
        conTable.add(downButton).minWidth(100).prefWidth(200).minHeight(100).prefHeight(200);
        conTable.add();

        //camera.zoom =camera.zoom/100;
        upButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                curY=1;
                curX=0;
                nextFrame=1;
                currentMove.setText("Up");
            }
        });
        downButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                curY=-1;
                curX=0;
                nextFrame=0;
                currentMove.setText("Down");
            }
        });
        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                curY=0;
                curX=1;
                nextFrame=2;
                currentMove.setText("right");
            }
        });

        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                curY=0;
                curX=-1;
                nextFrame=3;
                currentMove.setText("left");
            }
        });

        hurdleButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (canHurdle) {
                    isHurdle = !isHurdle;
                }
                if(isHurdle){
                    hurdle.setText("Hurdling");
                }
                else{
                    hurdle.setText("Not Hurdling");
                }
            }
        });
    }

    //loads textures
    private void loadTextures(){
        monPositions=new ArrayList<Position>();

        map = new TmxMapLoader().load("FirstMap.tmx");

        monTexts=new Texture[4];
        monTexts[0]=new Texture("zombie/zforward.png");
        monTexts[1]=new Texture("zombie/zbackward.png");
        monTexts[3]=new Texture("zombie/zright.png");
        monTexts[2]=new Texture("zombie/zleft.png");


        heroTexts=new Texture[4];
        heroTexts[0]=new Texture("player/pforward.png");
        heroTexts[1]=new Texture("player/pbackwards.png");
        heroTexts[2]=new Texture("player/pright.png");
        heroTexts[3]=new Texture("player/pleft.png");

    }
    @Override
    public void render(float delta) {

        //get Bluetooth Data
        String nextMove=game.message.getDataGame();

        //Parse the String
        if (nextMove!=null){
            parseString(nextMove);
        }

        //Timer for hurdle Cooldown
        if(!canHurdle){
            hurdleTime+=Gdx.graphics.getDeltaTime();
            hurdleButton.setText(String.format(Locale.US,"Time: %.0f",hurdleTime));
        }
        //cooldown done
        if (hurdleTime>hurdleCooldown*turnTime){
            canHurdle=true;
            hurdleButton.setText("Hurdle");
        }

        //increment time
        time+=Gdx.graphics.getDeltaTime();
        timer.setText(String.format(Locale.US,"Time: %.1f",time));

        //send moves every three seconds
        if (time>turnTime){
            try {
                curFrame=nextFrame;
                if (isHurdle) {
                    game.message.setDataGame(sendMove(3*curX, 3*curY));
                    hurdleTime=0;
                    canHurdle=false;
                    isHurdle=false;
                    hurdle.setText("Not Hurdling");
                }
                else{
                    game.message.setDataGame(sendMove(curX, curY));
                }
                time=0;
            } catch (IOException e) {
                e.printStackTrace();
            }
            curX=0;
            curY=0;
            currentMove.setText("nothing");
        }

        //Background
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw the map and NPC textures
        Batch mapBatch=renderer.getBatch();
        viewport.apply();
        camera.position.set(posExplorer.x+.5f,posExplorer.y+.5f,0);
        camera.update();
        mapBatch.setProjectionMatrix(camera.combined);
        renderer.setView(camera);
        renderer.render();
        mapBatch.begin();
        for (Position monPos:monPositions){
            renderer.getBatch().draw(monTexts[monPos.frame],monPos.x,monPos.y,1,1);
        }

        mapBatch.draw(heroTexts[curFrame],posExplorer.x,posExplorer.y,1,1);
        mapBatch.end();

        //draw the ui
        hubViewport.apply();
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        hubViewport.update(width,height);
        camera.viewportHeight=height;
        camera.viewportWidth=width;
        camera.update();
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
        map.dispose();
        stage.dispose();
        renderer.dispose();
        for (Texture text:monTexts){
            text.dispose();
        }
        for (Texture text:heroTexts){
            text.dispose();
        }
    }

    //String for sending via Bluetooth
    private String sendMove(int dx,int dy){
        return "Movel"+dx+","+dy;
    }

    //Parse a String from Bluetooth
    private void parseString(String string){
        String[] tokens = string.split("l");
        String action=tokens[0];
        //System.out.println(string);
        if (action.equals("Update")){//update stuff
            parseUpdate(tokens);
        }
        else if(action.equals("Start")){//starts the app
        }
        else  if(action.equals("Win")){
            game.setScreen(new EndScreen(true,game));
            this.dispose();
        }
        else if (action.equals("connectionLost")){
            Gdx.app.exit();
            //Dialog dialog = new Dialog("Error", new Window.WindowStyle());
            //dialog.getContentTable().add(resetButton);
        }
    }

    //parse the update
    private void parseUpdate(String[] tokens) {
        String[] update =tokens[1].split(",");
        //System.out.println("parsing");
        posExplorer=new Position(Integer.parseInt(update[0]),Integer.parseInt(update[1]));//explorer positon
        camera.position.set(posExplorer.x,posExplorer.y,0);//set camera to position
        monPositions.clear();//clear monsters
        for (int i=2;i<tokens.length;i++){//get all the monsters
            String[] monsterPos=tokens[i].split(",");
            monPositions.add(new Position(Integer.parseInt(monsterPos[0]),Integer.parseInt(monsterPos[1]),Integer.parseInt(monsterPos[2])));
        }
    }
}
