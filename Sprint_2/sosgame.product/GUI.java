package sosgame.product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class GUI extends JFrame {

    private static final int CELL_SIZE = 50;
    private static final int GRID_WIDTH = 2;
    private static final int SYMBOL_SIZE = 36;
    private static final Color BLUE_COLOR = new Color(0, 102, 204);
    private static final Color RED_COLOR = new Color(204, 0, 0);

    private Board board;
    private GameBoardCanvas canvas;

    private JTextField boardSizeField;
    private JLabel turnLabel;
    private JRadioButton simpleGameButton;
    private JRadioButton generalGameButton;
    private JRadioButton blueS, blueO, redS, redO;
    private JButton startButton;

    private boolean blueTurn = true;

    public GUI() {
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        createControlPanel();
        createCanvasPanel();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createControlPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        simpleGameButton = new JRadioButton("Simple game", true);
        generalGameButton = new JRadioButton("General game");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(simpleGameButton);
        modeGroup.add(generalGameButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("SOS"), gbc);

        gbc.gridx = 1;
        topPanel.add(simpleGameButton, gbc);
        gbc.gridx = 2;
        topPanel.add(generalGameButton, gbc);

        gbc.gridx = 3;
        topPanel.add(new JLabel("Board size:"), gbc);

        boardSizeField = new JTextField("8", 3);
        gbc.gridx = 4;
        topPanel.add(boardSizeField, gbc);

        startButton = new JButton("Start");
        startButton.addActionListener(e -> startGame());
        gbc.gridx = 5;
        topPanel.add(startButton, gbc);

        add(topPanel, BorderLayout.NORTH);
    }

    private void createCanvasPanel() {
        canvas = new GameBoardCanvas();
        canvas.setPreferredSize(new Dimension(400, 400));
        add(canvas, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel(new GridLayout(1, 2, 50, 0));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel bluePanel = new JPanel(new GridLayout(3, 1));
        bluePanel.add(new JLabel("Blue player", SwingConstants.CENTER));
        blueS = new JRadioButton("S", true);
        blueO = new JRadioButton("O");
        ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueS);
        blueGroup.add(blueO);
        bluePanel.add(blueS);
        bluePanel.add(blueO);

        JPanel redPanel = new JPanel(new GridLayout(3, 1));
        redPanel.add(new JLabel("Red player", SwingConstants.CENTER));
        redS = new JRadioButton("S", true);
        redO = new JRadioButton("O");
        ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redS);
        redGroup.add(redO);
        redPanel.add(redS);
        redPanel.add(redO);

        sidePanel.add(bluePanel);
        sidePanel.add(redPanel);

        add(sidePanel, BorderLayout.WEST);

        turnLabel = new JLabel("Current turn: blue", SwingConstants.CENTER);
        add(turnLabel, BorderLayout.SOUTH);
    }

    private void startGame() {
        try {
            int size = Integer.parseInt(boardSizeField.getText());
            if (size < 3) {
                JOptionPane.showMessageDialog(this, "Board size must be at least 3.", "Invalid Size", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String mode = simpleGameButton.isSelected() ? "Simple Game" : "General Game";
            board = new Board(size, mode);
            canvas.setPreferredSize(new Dimension(size * CELL_SIZE, size * CELL_SIZE));
            pack();
            canvas.repaint();
            blueTurn = true;
            turnLabel.setText("Current turn: blue");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid board size.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class GameBoardCanvas extends JPanel {
        GameBoardCanvas() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (board == null) return;

                    int row = e.getY() / CELL_SIZE;
                    int col = e.getX() / CELL_SIZE;

                    if (board.getCell(row, col) != ' ') return;

                    char letter = getCurrentPlayerLetter();
                    char player = blueTurn ? 'B' : 'R';

                    board.makeMove(row, col, letter, player);
                    repaint();

                    blueTurn = !blueTurn;
                    turnLabel.setText("Current turn: " + (blueTurn ? "blue" : "red"));
                }
            });
        }

        private char getCurrentPlayerLetter() {
            if (blueTurn)
                return blueS.isSelected() ? 'S' : 'O';
            else
                return redS.isSelected() ? 'S' : 'O';
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            if (board == null) return;

            int size = board.getSize();
            g.setColor(Color.LIGHT_GRAY);

            for (int i = 0; i <= size; i++) {
                g.fillRect(i * CELL_SIZE - GRID_WIDTH / 2, 0, GRID_WIDTH, size * CELL_SIZE);
                g.fillRect(0, i * CELL_SIZE - GRID_WIDTH / 2, size * CELL_SIZE, GRID_WIDTH);
            }

            g.setFont(new Font("SansSerif", Font.BOLD, SYMBOL_SIZE));
            FontMetrics fm = g.getFontMetrics();

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    char c = board.getCell(row, col);
                    if (c != ' ') {
                        int x = col * CELL_SIZE + (CELL_SIZE - fm.charWidth(c)) / 2;
                        int y = row * CELL_SIZE + ((CELL_SIZE + fm.getAscent()) / 2) - 8;

                        char player = board.getPlayerAt(row, col);
                        if (player == 'B') g.setColor(BLUE_COLOR);
                        else if (player == 'R') g.setColor(RED_COLOR);
                        else g.setColor(Color.BLACK);

                        g.drawString(String.valueOf(c), x, y);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
