package com.codegym.games.moonlander;

import com.codegym.engine.cell.*;

public class MoonLanderGame extends Game {
    public static final int WIDTH=64;
    public static final int HEIGHT=64;
    private Rocket rocket;
    private GameObject landscape;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private GameObject platform;
    private boolean isGameStopped;
    private int score;


    public void initialize(){
        showGrid(false);
        setScreenSize(WIDTH,HEIGHT);
        createGame();
    }

    private void win(){
        rocket.land();
        isGameStopped=true;
        showMessageDialog(Color.WHITE,"GG",Color.GREEN,60);
        stopTurnTimer();
    }

    private void gameOver(){
        rocket.crash();
        score=0;
        isGameStopped=true;
        showMessageDialog(Color.GREENYELLOW,"Arrrrrrrgh",Color.RED,75);
        stopTurnTimer();
    }

    private void createGame(){
        createGameObjects();
        isGameStopped=false;
        isUpPressed=false;
        isLeftPressed=false;
        isRightPressed=false;
        drawScene();
        score=1000;
        setTurnTimer(50);
    }

    private void drawScene(){
        for (int y=0;y<HEIGHT;y++){
            for (int x=0;x<WIDTH;x++){
                setCellColor(y,x,Color.BLACK);
            }
        }
        landscape.draw(this);
        rocket.draw(this);
    }

    private void createGameObjects(){
        rocket=new Rocket(WIDTH/2,0);
        landscape=new GameObject(0,25,ShapeMatrix.LANDSCAPE);
        platform=new GameObject(23,MoonLanderGame.HEIGHT-1,ShapeMatrix.PLATFORM);
    }

    @Override
    public void onTurn(int x){
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        if(score>0) score--;
        check();
        setScore(score);
        drawScene();
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x>=0 && x<WIDTH && y>=0 && y<HEIGHT)
            super.setCellColor(x, y, color);

    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped){
            if (key==Key.SPACE){
                createGame();
                return;
            }
        }
        switch (key){
                case UP : {
                    isUpPressed=true;
                    return;
                }
                case RIGHT:{
                    isRightPressed=true;
                    isLeftPressed=false;
                    return;
                }
                case LEFT:{
                    isLeftPressed=true;
                    isRightPressed=false;
                    //return;
                }
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        switch (key){
            case UP : {
                isUpPressed=false;
                return;
            }
            case RIGHT:{
                isRightPressed=false;
                return;
            }
            case LEFT:{
                isLeftPressed=false;
                //return;
            }
        }
    }

    private void check(){
        if (rocket.isCollision(landscape) && !rocket.isCollision(platform)) gameOver();
        else if (rocket.isCollision(platform) && rocket.isStopped()) win();
    }
}


