package sosgame.product;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUI extends JFrame {

    public static final int CELL_SIZE = 100;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;

    private int CANVAS_WIDTH;
    private int CANVAS_HEIGHT;

    private GameBoardCanvas gameBoardCanvas;
    private Board board;

    public GUI(Board board) {
        this.board = board;
        setContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("SOS Game");
        setVisible(true);
    }

    public Board getBoard() {
        return board;
    }

    private void setContentPane() {
        gameBoardCanvas = new GameBoardCanvas();
        int size = board.getSize();
        CANVAS_WIDTH = CELL_SIZE * size;
        CANVAS_HEIGHT = CELL_SIZE * size;
        gameBoardCanvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(gameBoardCanvas, BorderLayout.CENTER);
    }

    class GameBoardCanvas extends JPanel {

        GameBoardCanvas() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int rowSelected = e.getY() / CELL_SIZE;
                    int colSelected = e.getX() / CELL_SIZE;

                    String[] options = {"S", "O"};
                    String choice = (String) JOptionPane.showInputDialog(
                            GUI.this,
                            "Choose a letter to place:",
                            "Make Move",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    if (choice != null && !choice.isEmpty()) {
                        board.makeMove(rowSelected, colSelected, choice.charAt(0));
                        repaint();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            drawGridLines(g);
            drawBoard(g);
        }

        private void drawGridLines(Graphics g) {
            g.setColor(Color.LIGHT_GRAY);
            int size = board.getSize();

            for (int row = 1; row < size; row++) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF,
                        CANVAS_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < size; col++) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0,
                        GRID_WIDTH, CANVAS_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
            }
        }

        private void drawBoard(Graphics g) {
            g.setFont(new Font("SansSerif", Font.BOLD, SYMBOL_SIZE));
            FontMetrics fm = g.getFontMetrics();
            int size = board.getSize();

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    char cell = board.getCell(row, col);
                    if (cell == 'S' || cell == 'O') {
                        int x = col * CELL_SIZE + (CELL_SIZE - fm.charWidth(cell)) / 2;
                        int y = row * CELL_SIZE + ((CELL_SIZE + fm.getAscent()) / 2) - CELL_PADDING;
                        g.setColor(cell == 'S' ? Color.RED : Color.BLUE);
                        g.drawString(String.valueOf(cell), x, y);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int size = 3;
            String mode = "Simple Game";
            new GUI(new Board(size, mode));
        });
    }
}
