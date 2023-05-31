import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    final int width = 1320;
    final int height = 900;
    final int unitSize = 30;
    final int gameUnits = width/unitSize * height;
    final int delay = 55;
    Timer timer;
    int bodyParts = 20;
    int[] x = new int[gameUnits];
    int[] y = new int[gameUnits];
    Queue<Character> commands;
    char command;
    int appleX;
    int appleY;
    Random random;
    boolean gameRunning = true;
    JButton button;
    int score = 0;
    GamePanel(){
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(1,50,32)); //dark green
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setLayout(null);

        timer = new Timer(delay, this);
        commands = new LinkedList<>();
        commands.offer('d');
        timer.start();
        random = new Random();

        appleX = random.nextInt(width / unitSize);
        appleY = random.nextInt(height / unitSize);

        button = new JButton();
        button.setVisible(false);
        button.addActionListener(this);
        button.setBounds(width/2 - 150 / 2,height/2 - 50 / 2,150,50);
        button.setText("Play Again !");
        this.add(button);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawScore(g);
        drawSnake(g);
        drawApple(g);
        repaint();
    }
    private void drawApple(Graphics g){
        g.setColor(Color.RED);
        g.fillOval(appleX * unitSize, appleY * unitSize, unitSize, unitSize);
    }
    private void generateApple(){
        boolean validPosition = false;
        while (!validPosition) {
            appleX = random.nextInt(width / unitSize);
            appleY = random.nextInt(height / unitSize);
            validPosition = true;
            for (int i = 0; i < bodyParts; i++) {
                if (x[i] == appleX * unitSize && y[i] == appleY * unitSize) {
                    validPosition = false;
                    break;
                }
            }
        }
    }
    private void drawSnake(Graphics g) {
        for (int i = 0; i < bodyParts; i++) {
            //if(!gameRunning) break;
            if(i == 0 && gameRunning) g.setColor(new Color(59, 1, 1)); // dark red
            else g.setColor(Color.BLACK);
            if(x[i] == 0 && y[i] == 0 && !gameRunning){
                continue;
            }
            g.fillRect(x[i], y[i], unitSize, unitSize);
        }
    }
    private void drawScore(Graphics g){
        if(gameRunning) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.PLAIN, 60));
            g.drawString("" + score, width / 2, 40);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.PLAIN, 80));
            g.drawString("Your score: " + score, width / 2 - 300, height / 2 - 70);
        }
    }
    private void pull(){
        if(commands.size() > 1){
            commands.poll();
        }
    }
    private void move(){
        if(!gameRunning) return;
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if(commands.peek() == 'd') {
            x[0] = x[0] + unitSize;
            pull();
        }
        else if(commands.peek() == 'a') {
            x[0] = x[0] - unitSize;
            pull();
        }
        else if(commands.peek() == 'w') {
            y[0] = y[0] - unitSize;
            pull();
        }
        else if(commands.peek() == 's') {
            y[0] = y[0] + unitSize;
            pull();
        }
        if (x[0] == appleX * unitSize && y[0] == appleY * unitSize) {
            for (int i = 0; i < 10; i++) {
                bodyParts++;
                x[bodyParts] = x[bodyParts - 1];
                y[bodyParts] = y[bodyParts - 1];
            }
            score++;
            generateApple();
        }

        for(int i = 1; i <= bodyParts; i++){
            if(x[0] == x[i] && y[0] == y[i]){
                bodyParts = 50;
                x[0] = 0;
                y[0] = 0;
                commands.poll();
                commands.offer('d');
                gameRunning = false;
                button.setVisible(true);
                break;
            }
        }
        if(x[0] > width - unitSize || x[0] < 0 || y[0] < 0 || y[0] > height - unitSize){
            commands.poll();
            commands.offer('d');
            gameRunning = false;
            button.setVisible(true);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if(e.getSource() == button){
            gameRunning = true;
            for(int i = 1; i <= bodyParts; i++){
                x[i] = -unitSize;
                y[i] = 0;
            }
            bodyParts = 20;
            x[0] = 0;
            y[0] = 0;
            score = 0;
            button.setVisible(false);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        command = commands.peek();
        switch (e.getKeyChar()){
            case 'w':
                if(command != 's') {
                    commands.offer('w');
                }
                break;
            case 's':
                if(command != 'w') {
                    commands.offer('s');
                }
                break;
            case 'a':
                if(command != 'd') {
                    commands.offer('a');
                }
                break;
            case 'd':
                if(command != 'a') {
                    commands.offer('d');
                }
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
