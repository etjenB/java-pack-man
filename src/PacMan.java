import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                pacman.updateDirection('U');
                break;
            case KeyEvent.VK_DOWN:
                pacman.updateDirection('D');
                break;
            case KeyEvent.VK_LEFT:
                pacman.updateDirection('L');
                break;
            case KeyEvent.VK_RIGHT:
                pacman.updateDirection('R');
                break;
        }
    }

    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U'; // U D L R
        int velocityX = 0;
        int velocityY = 0;

        Block(int x, int y, int width, int height, Image image){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction){
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for(Block wall : walls){
                if (collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity(){
            switch (this.direction){
                case 'U':
                    this.velocityX = 0;
                    this.velocityY = -tileSize/4;
                    break;
                case 'D':
                    this.velocityX = 0;
                    this.velocityY = tileSize/4;
                    break;
                case 'L':
                    this.velocityX = -tileSize/4;
                    this.velocityY = 0;
                    break;
                case 'R':
                    this.velocityX = tileSize/4;
                    this.velocityY = 0;
                    break;
            }
        }
    }

    private final int rowCount = 21;
    private final int columnCount = 19;
    private final int tileSize = 32;
    private final int boardWidth = tileSize * columnCount;
    private final int boardHeight = tileSize * rowCount;

    private Image wallImage;
    private Image powerFoodImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;

    PacMan(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        powerFoodImage = new ImageIcon(getClass().getResource("./powerFood.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                String row = tileMap[i];
                char tileMapChar = row.charAt(j);

                int x = j * tileSize;
                int y = i * tileSize;

                switch (tileMapChar) {
                    case 'X':
                        walls.add(new Block(x, y, tileSize, tileSize, wallImage));
                        break;
                    case 'b':
                        ghosts.add(new Block(x, y, tileSize, tileSize, blueGhostImage));
                        break;
                    case 'p':
                        ghosts.add(new Block(x, y, tileSize, tileSize, pinkGhostImage));
                        break;
                    case 'o':
                        ghosts.add(new Block(x, y, tileSize, tileSize, orangeGhostImage));
                        break;
                    case 'r':
                        ghosts.add(new Block(x, y, tileSize, tileSize, redGhostImage));
                        break;
                    case 'P':
                        pacman = new Block(x, y, tileSize, tileSize, pacmanRightImage);
                        break;
                    case ' ':
                        foods.add(new Block( x + 14, y + 14, 4, 4, null));
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(pacmanRightImage, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls){
            g.drawImage(wallImage, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
    }

    private void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
    }

    private boolean collision(Block b1, Block b2){
        return b1.x < b2.x + b2.width &&
                b1.x + b1.width > b2.x &&
                b1.y < b2.y + b2.height &&
                b1.y + b1.height > b2.y;
    }
}
