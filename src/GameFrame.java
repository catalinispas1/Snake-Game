import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(new GamePanel());
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
