import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = tileSize * columnCount;
        int boardHeight = tileSize * rowCount;

        JFrame frame = new JFrame("Pac Man v1.0");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacMan = new PacMan();
        frame.add(pacMan);
        frame.pack();
        pacMan.requestFocus();
        frame.setVisible(true);
    }
}