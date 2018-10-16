package com.dartmouth.treasurehunter.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dartmouth.treasurehunter.GameModels.Move;
import com.dartmouth.treasurehunter.GameModels.Position;
import com.dartmouth.treasurehunter.GameModels.THGame;
import com.dartmouth.treasurehunter.THMain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class NavigatorScreen implements Screen {

    private int totMoves=0;//Num of moves in Game
    private Stage stage;//Ui

    private OrthographicCamera camera;

    private final THMain game;//main game screen

    private OrthogonalTiledMapRenderer renderer;

    //Zoom for zoom functionality
    private float currentZoom;

    //MOnster thats frozen
    private Position freeze;

    //Ui Labels
    private Label timer;
    private Label moveCount;

    //Mon Textures
    private Texture[] monTexts;
    private Texture dummyText;//Dummy Texture
    private TiledMap map;

    private float totTime=0;//Time since last Move
    private THGame gameState;//Gmae state

    //for dummy functionality
    private boolean canDummy=true;
    private boolean isDummy=false;
    private float dummyTime=0;//Cooldown
    private Label dummyLbl;
    private Position dummyPos;


    final static private int turnTime=2;//in seconds
    final static private int dummyTurn=4;
    final static private int dummyCooldown=15;

    public NavigatorScreen(final THMain game){
        this.game=game;
    }
    @Override
    public void show() {

        //Set up HUB
        stage=new Stage();
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener()));
        BitmapFont font =new BitmapFont();

        //Set up UI
        font.getData().setScale(4,4);
        Label.LabelStyle style =new Label.LabelStyle(font, Color.WHITE);
        timer = new Label(String.format(Locale.US,"Hello"),style);
        dummyLbl = new Label(String.format(Locale.US,"Dummy to Place"),style);
        moveCount = new Label("0",style);
        Table labelTable=new Table();
        labelTable.add(timer);
        labelTable.row();
        labelTable.add(dummyLbl);
        labelTable.row();
        labelTable.add(moveCount);
        freeze=new Position(0,0);
        rootTable.add(labelTable).expand().top().right();

        //Load Textures
        monTexts=new Texture[4];
        monTexts[0]=new Texture("zombie/zforward.png");
        monTexts[1]=new Texture("zombie/zbackward.png");
        monTexts[3]=new Texture("zombie/zright.png");
        monTexts[2]=new Texture("zombie/zleft.png");
        dummyText=new Texture("player/pforward.png");

        map = new TmxMapLoader().load("FirstMap.tmx");

        //Make game
        gameState=new THGame();
        gameState.newGame(convertTileMap(map),64,game.monDen);

        //Set up Renderer and camera
        float unitScale = 1 / 16f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 16);
        renderer.setView(camera);
        camera.zoom =camera.zoom/100;
        //send initial data
        try {
            game.message.setDataGame(sendUpdate(gameState.getExplorerPosition(),gameState.getMonsterPositions()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //converts Tile map into 2d Char array
    public char [][] convertTileMap(TiledMap map){
        char[][] mapGame= new char[64][64];
        TiledMapTileLayer treasure =(TiledMapTileLayer) map.getLayers().get(4);
        TiledMapTileLayer wall= (TiledMapTileLayer) map.getLayers().get(2);
        TiledMapTileLayer floor= (TiledMapTileLayer) map.getLayers().get(1);
        for (int i=0;i<64;i++){
            for (int j=0;j<64;j++){
                if (treasure.getCell(i,j)!=null){
                    mapGame[i][j]='t';
                }
                else if (floor.getCell(i,j)!=null&&wall.getCell(i,j)==null){
                    mapGame[i][j]='f';
                }
                else{
                    mapGame[i][j]='w';
                }
            }
        }
        //prints board for debugging
        for (char [] line :mapGame){
            System.out.println();
            for (char c:line) {
                System.out.print(c+" ");
            }
        }
    return mapGame;
    }

    @Override
    public void render(float delta) {
        //get bluetooth message
        String move=game.message.getDataGame();

        //Does Dummy Cooldown timer
        if (!canDummy){
            dummyLbl.setText(String.format(Locale.US,"Cooldown:%d",(int)dummyTime));
            dummyTime+=Gdx.graphics.getDeltaTime();
        }
        if(dummyTime>turnTime*dummyTurn){
            isDummy=false;
        }
        if (dummyTime>turnTime*dummyCooldown){
            dummyLbl.setText("Dummy Ready");
            canDummy=true;
        }
        //if game is over go to end Screen
        if (gameState.isGameOver()){
            try {
                game.message.setDataGame(sendWin());
                game.setScreen(new EndScreen(true,game));
                this.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update UI
        totTime+=Gdx.graphics.getDeltaTime();
        moveCount.setText(String.format(Locale.US,"Moves: %d",totMoves));
        timer.setText(String.format(Locale.US,"Time: %.1f",totTime));

        //if Bluetooth not null
        if (move!=null){
            parseString(move);
        }
        Gdx.graphics.getDeltaTime();

        // Background
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();

        //draw monsters
        renderer.getBatch().begin();
        if(isDummy){
            System.out.println("Dummy:"+dummyPos.x+"|"+dummyPos.y);
            renderer.getBatch().draw(dummyText,dummyPos.x,dummyPos.y,1,1);
        }
        for (Position monPos:gameState.getMonsterPositions()){
            drawMon(monPos,renderer.getBatch());
        }
        if(totMoves%game.playerFreq==0) {
            renderer.getBatch().draw(dummyText, gameState.getExplorerPosition().x, gameState.getExplorerPosition().y, 1, 1);
        }
        renderer.getBatch().end();
        game.batch.setProjectionMatrix(stage.getCamera().combined);

        stage.draw();//draw ui
    }

    // Sets up Pan and Zoom
    public class MyGestureListener implements GestureDetector.GestureListener {
        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        //Tap freezes monsters
        @Override
        public boolean tap(float x, float y, int count, int button) {
            Vector3 touch = new Vector3();
            camera.unproject(touch.set(x, y, 0));
            freeze=new Position((int)touch.x,(int)touch.y);
            return false;
        }

        //Long press makes a dummy
        @Override
        public boolean longPress(float x, float y) {
            Vector3 touch = new Vector3();
            camera.unproject(touch.set(x, y, 0));
            if (canDummy){
                isDummy=true;
                canDummy=false;
                dummyTime=0;
                dummyPos=new Position((int)touch.x,(int)touch.y);
            }
            return true;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {

            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            camera.translate(-deltaX * camera.zoom,deltaY * camera.zoom);
            float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
            float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

            camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 64 - effectiveViewportWidth / 2f);
            camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 64 - effectiveViewportHeight / 2f);

            camera.update();

            System.out.println("pan:"+x+"y:"+y);
            return true;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            currentZoom = camera.zoom;

            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            camera.zoom = (initialDistance / distance) * currentZoom;
            camera.zoom = MathUtils.clamp(camera.zoom, 0.005f, 32/camera.viewportWidth);
            float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
            float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

            camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 64 - effectiveViewportWidth / 2f);
            camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 64 - effectiveViewportHeight / 2f);


            camera.update();
            System.out.println("zoom:"+camera.zoom);

            return true;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {

        }
    }

    //draws monster
    private void drawMon(Position pos, Batch batch){
        batch.draw(monTexts[pos.frame],pos.x,pos.y,1,1);
    }
    @Override
    public void resize(int width, int height) {
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
        renderer.dispose();
        stage.dispose();
        map.dispose();
        for (Texture text:monTexts){
            text.dispose();
        }
    }
    //Sends an update
    private String sendUpdate(Position explorer, ArrayList<Position> mons){
        StringBuilder string = new StringBuilder();
        string.append("Updatel");
        string.append(explorer.x+","+explorer.y+"l");
        for (Position p:mons){
            string.append(p.x+","+p.y+","+p.frame+"l");
        }
        //string.append("end");
        return string.toString();
    }
    //Parses an Update
    private void parseString(String string){
        System.out.println(string);
        String[] tokens = string.split("l");
        System.out.println(tokens[0]);

        if (tokens[0].equals("Move")){
            parseMove(tokens);
        }
        else if(tokens[0].equals("Start")){
        }
        else if (tokens[0].equals("connectionLost")){
            Gdx.app.exit();
            //Dialog dialog = new Dialog("Error", new Window.WindowStyle());
            //dialog.getContentTable().add(resetButton);
        }
    }
    //Sends win messsage
    private String sendWin(){
        return "Winl";
    }
    //Parses MoveSet
    private void parseMove(String[] tokens) {
        int dx,dy;
        String[] change = tokens[1].split(",");
        dx=Integer.parseInt(change[0]);
        dy=Integer.parseInt(change[1]);
        Move move;
        if (isDummy) {
            move = new Move(dx, dy,dummyPos,true,freeze);
        }
        else {
            move = new Move(dx, dy,freeze);
        }
        gameState.updateGame(move);
        totMoves+=1;
        try {
            game.message.setDataGame(sendUpdate(gameState.getExplorerPosition(),gameState.getMonsterPositions()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        totTime=0;
    }

}
