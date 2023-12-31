package src.entities;

import src.main.Game;

import java.awt.geom.Rectangle2D;

import static src.utils.Constants.EnemyConstants.*;
import static src.utils.Constants.*;
import static src.utils.HelpMethods.*;
import static src.utils.Constants.Directions.*;

/**
 * @author Tze Yik Ong
 * Superclass containing methods and variables for all enemies
 */
public abstract class Enemy extends Entity {
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    protected void firstUpdateCheck(int[][] lvlData){
        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void move(int[][] lvlData){
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (isFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }

    protected void turnTowardsPlayer(Player player){
        if (player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);

        if(playerTileY == tileY)
            if(isPlayerInRange(player))
                if(isSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;

        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int distanceToPlayer = (int) Math.abs(player.hitbox.x - hitbox.x);
        return distanceToPlayer <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player){
        int distanceToPlayer = (int) Math.abs(player.hitbox.x - hitbox.x);
        return distanceToPlayer <= attackDistance;
    }

    protected void newState(int enemyState){
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int value){
        currentHealth -= value;

        if(currentHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-getEnemyDmg(enemyType));
        attackChecked = true;
    }

    protected void updateInAir(int[][] lvlData){
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANISPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(enemyType, state)) {
                aniIndex = 0;

                switch (state){
                    case ATTACK,HIT -> state = IDLE;
                    case DEAD -> active =false;
                }
            }
        }
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }

}