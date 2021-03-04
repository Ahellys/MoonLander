package com.codegym.games.moonlander;

import com.codegym.engine.cell.*;

import java.util.Arrays;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double speedX = 0;
    private double boost = 0.05;
    private double slowdown = boost / 10;
    private RocketFire downFire;
    private RocketFire leftFire;
    private RocketFire rightFire;



    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);
        downFire = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_DOWN_1, ShapeMatrix.FIRE_DOWN_2, ShapeMatrix.FIRE_DOWN_3));
        leftFire = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_SIDE_1, ShapeMatrix.FIRE_SIDE_2));
        rightFire = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_SIDE_1, ShapeMatrix.FIRE_SIDE_2));
    }

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            speedY -= boost;
        } else {
            speedY += boost;
        }
        y += speedY;

        if (isLeftPressed) {
            speedX -= boost;
            x += speedX;
        } else if (isRightPressed) {
            speedX += boost;
            x += speedX;
        } else if (speedX > slowdown) {
            speedX -= slowdown;
        } else if (speedX < -slowdown) {
            speedX += slowdown;
        } else {
            speedX = 0;
        }
        x += speedX;
        checkBorders();
        switchFire(isUpPressed,isLeftPressed,isRightPressed);
    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }

    private void checkBorders() {
        if (x < 0) {
            x = 0;
            speedX = 0;
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }
        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }

    public boolean isStopped() {
        return speedY < 10 * boost;
    }

    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;
                }
            }
        }
        return false;
    }

    private void switchFire(boolean isUpPressed,boolean isLeftPressed, boolean isRightPressed){

        if (isUpPressed){
            this.downFire.x=this.x+(this.width/2);
            this.downFire.y=this.y+this.height;
            this.downFire.show();
        } else this.downFire.hide();

        if(isLeftPressed){
            this.leftFire.x=this.x+this.width;
            this.leftFire.y=this.y+this.height;
            this.leftFire.show();
        } else  this.leftFire.hide();

        if(isRightPressed){
            this.rightFire.x=this.x-ShapeMatrix.FIRE_SIDE_1[0].length;
            this.rightFire.y=this.y+this.height;
            this.rightFire.show();
        } else this.rightFire.hide();

    }

    public void land(){
        this.y-=1;
    }

    public void crash() {
        this.matrix=ShapeMatrix.ROCKET_CRASH;
    }
}
