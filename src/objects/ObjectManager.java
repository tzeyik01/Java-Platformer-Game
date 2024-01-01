package src.objects;

import src.entities.Player;
import src.gamestates.Playing;
import src.levels.Level;
import src.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static src.utils.Constants.ObjectConstants.*;

/**
 * @author  Tze Yik Ong
 * Class to manage the loading of the game objects
 */
public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage spikeImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImgs();
    }

    public void checkSpikesTouched(Player player){
        for(Spike s: spikes)
            if(s.getHitbox().intersects(player.getHitbox()))
                player.kill();
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        for(Potion p: potions)
            if(p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    public void applyEffectToPlayer(Potion potion){
        if(potion.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox){
        for(GameContainer gc: containers)
            if(gc.isActive() && !gc.doAnimation) {
                if(gc.getHitbox().intersects(attackBox)){
                    gc.setAnimation(true);
                    int type = 0;
                    if(gc.getObjType() == BARREL)
                        type = 1;

                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 4), type));
                    return;
                }
            }
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotionsList());
        containers = new ArrayList<>(newLevel.getContainersList());
        spikes = newLevel.getSpikesList();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.getSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for(int j = 0; j < potionImgs.length; j ++)
            for(int i = 0; i < potionImgs[j].length; i ++)
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprite = LoadSave.getSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for(int j = 0; j < containerImgs.length; j ++)
            for(int i = 0; i < containerImgs[j].length; i ++)
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

        spikeImg = LoadSave.getSpriteAtlas(LoadSave.TRAP_ATLAS);
    }

    public void update(){
        for(Potion p: potions)
            if(p.isActive())
                p.update();

        for (GameContainer gc: containers)
            if(gc.isActive())
                gc.update();
    }

    public void draw(Graphics g, int xLvlOffset){
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for(Spike s: spikes)
            g.drawImage(spikeImg,
                    (int)(s.getHitbox().x - xLvlOffset),
                    (int)(s.getHitbox().y - s.getyDrawOffset()),
                    SPIKE_WIDTH,
                    SPIKE_HEIGHT,
                    null);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc: containers)
            if(gc.isActive()) {
                int type = 0;
                if(gc.getObjType() == BARREL)
                    type = 1;

                g.drawImage(containerImgs[type][gc.getAniIndex()],
                        (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                        (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Potion p: potions)
            if(p.isActive()) {
                int type = 0;
                if(p.getObjType() == RED_POTION)
                    type = 1;

                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                        (int) (p.getHitbox().y - p.getyDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
    }

    public void resetAllObject() {
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for(Potion p: potions)
            p.reset();

        for (GameContainer gc: containers)
            gc.reset();
    }
}
