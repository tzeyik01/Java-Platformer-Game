package Main;

import Input.KeyboardInputs;
import Input.MouseInputs;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private int xDelta = 100;
    private int yDelta = 100;

    public GamePanel(){
        mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.fillRect(xDelta,yDelta,200,50);

    }

    public void changeXDelta(int value){
        xDelta += value;
        repaint();
    }

    public void changeYDelta(int value){
        yDelta += value;
        repaint();
    }

    public void setRectPosition(int x, int y){
        xDelta = x;
        yDelta = y;
        repaint();
    }
}