package src.entities;

import src.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static src.utils.Constants.PlayerConstants.*;

public class Player extends Entity{

    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up , right, down;
    private float playerSpeed = 2.0f;


    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
    }

    public void update(){
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g){
        g.drawImage(animations[playerAction][animationIndex],(int)x,(int)y, width, height,null);
    }



    /**
     * Update which image to use in the image array for the various animations
     */
    private void updateAnimationTick() {
        animationTick ++;

        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex ++;

            if(animationIndex > getSpriteAmount(playerAction) - 1) {
                animationIndex = 0;
                attacking = false;
            }
        }
    }

    /**
     * Set the animation type to use for the player character
     */
    private void setAnimation() {
        int startAnimation = playerAction;

        if(moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if(attacking)
            playerAction = ATTACK_1;

        if(startAnimation != playerAction)
            resetAnimationTick();
    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    /**
     * Update the player position
     */
    private void updatePosition() {

        moving = false;

        if(left && !right) {
            x -= playerSpeed;
            moving = true;
        }
        else if (right && !left) {
            x += playerSpeed;
            moving = true;
        }

        if(up && !down) {
            y -= playerSpeed;
            moving = true;
        }
        else if (down && !up) {
            y += playerSpeed;
            moving = true;
        }
    }

    private void loadAnimations() {
            BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

            animations = new BufferedImage[9][6];
            for(int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
    }

    /**
     * Reset the boolean left, up, right, down to false
     */
    public void resetDirBooleans(){
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
