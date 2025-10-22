package sosgame.product;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUI extends JFrame {

    public static final int CELL_SIZE = 60;
    public static final int GRID_WIDTH = 4;
    public static final int GRID_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 4;

    private int canvasWidth, canvasHeight;

    private Board board;
    private GameBoardCanvas canvas;

    private JRadioButton simpleGame, generalGame;
    private JTextField boardSizeField;
    private JButton startButton;
    private JRadioButton blueS, blueO, redS, redO;
    private JLabel turnLabel;

    public GUI(Board board) {
        this.board = board;
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createControlPanel(), BorderLayout.NORTH);

        canvas = new GameBoardCanvas();
        resizeCanvas(board.getSize());
        add(canvas, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        turnLabel = new JLabel("Current turn: Blue");
        bottom.add(turnLabel);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private JPanel createControlPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 8, 5, 8);

        // Game type
        JLabel sosLabel = new JLabel("SOS");
        simpleGame = new JRadioButton("Simple game", true);
        generalGame = new JRadioButton("General game");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(simpleGame);
        typeGroup.add(generalGame);

        // Board size
        JLabel sizeLabel = new JLabel("Board size:");
        boardSizeField = new JTextField(Integer.toString(board.getSize()), 3);
        startButton = new JButton("Start");
        startButton.addActionListener(e -> startNewGame());

        // Blue player
        JLabel blueLabel = new JLabel("Blue player");
        blueS = new JRadioButton("S", true);
        blueO = new JRadioButton("O");
        ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueS);
        blueGroup.add(blueO);

        // Red player
        JLabel redLabel = new JLabel("Red player");
        redS = new JRadioButton("S", true);
        redO = new JRadioButton("O");
        ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redS);
        redGroup.add(redO);

        // Layout row 1
        c.gridx=0; c.gridy=0; p.add(sosLabel,c);
        c.gridx=1; p.add(simpleGame,c);
        c.gridx=2; p.add(generalGame,c);
        c.gridx=3; p.add(sizeLabel,c);
        c.gridx=4; p.add(boardSizeField,c);
        c.gridx=5; p.add(startButton,c);

        // Layout row 2
        c.gridy=1; c.gridx=0; p.add(blueLabel,c);
        c.gridx=1; p.add(blueS,c);
        c.gridx=2; p.add(blueO,c);
        c.gridx=3; p.add(redLabel,c);
        c.gridx=4; p.add(redS,c);
        c.gridx=5; p.add(redO,c);

        return p;
    }

    private void startNewGame() {
        int size;
        try {
            size = Integer.parseInt(boardSizeField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid board size.");
            return;
        }
        if (size < 2) size = 2;
        board = new Board(size, "");
        resizeCanvas(size);
        pack();
        repaint();
    }

    private void resizeCanvas(int size) {
        canvasWidth = CELL_SIZE * size;
        canvasHeight = CELL_SIZE * size;
        canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    }

    // ---------- drawing panel ----------
    class GameBoardCanvas extends JPanel {
        GameBoardCanvas() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int r = e.getY() / CELL_SIZE;
                    int c = e.getX() / CELL_SIZE;
                    board.makeMove(r, c, 'a');
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            drawGrid(g);
            drawMarks(g);
        }

        private void drawGrid(Graphics g) {
            g.setColor(Color.LIGHT_GRAY);
            for (int i=1;i<board.getSize();i++) {
                g.fillRoundRect(0, CELL_SIZE*i-GRID_HALF, canvasWidth, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
                g.fillRoundRect(CELL_SIZE*i-GRID_HALF, 0, GRID_WIDTH, canvasHeight, GRID_WIDTH, GRID_WIDTH);
            }
        }

        private void drawMarks(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int r=0;r<board.getSize();r++) {
                for (int c=0;c<board.getSize();c++) {
                    int v = board.getCell(r,c);
                    int x = c*CELL_SIZE + CELL_PADDING;
                    int y = r*CELL_SIZE + CELL_PADDING;
                    if (v==1) {
                        g2d.setColor(Color.RED);
                        g2d.drawLine(x,y,x+SYMBOL_SIZE,y+SYMBOL_SIZE);
                        g2d.drawLine(x+SYMBOL_SIZE,y,x,y+SYMBOL_SIZE);
                    } else if (v==2) {
                        g2d.setColor(Color.BLUE);
                        g2d.drawOval(x,y,SYMBOL_SIZE,SYMBOL_SIZE);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI(new Board(3,"")));
    }
}
