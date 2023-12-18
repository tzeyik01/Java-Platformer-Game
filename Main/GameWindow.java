package Main;

import javax.swing.*;

/**
 * @author  Tze Yik Ong
 *
 * Class for game window
 */
public class GameWindow {

    private JFrame jframe;

    public GameWindow(GamePanel gamePanel){
        jframe = new JFrame();

        jframe.setSize(400, 400);
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

    }
}