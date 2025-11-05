package sosgame.product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class GUI extends JFrame
{
    private static final int CELL_SIZE = 50;
    private static final int SYMBOL_SIZE = 36;

    private BaseGame game;
    private GameBoardCanvas canvas;

    private JTextField boardSizeField;
    private JLabel turnLabel;
    private JRadioButton simpleGameButton;
    private JRadioButton generalGameButton;
    private JRadioButton blueS, blueO, redS, redO;

    public GUI()
    {
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupControls();
        setupCanvas();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupControls()
    {
        JPanel top = new JPanel(new FlowLayout());

        simpleGameButton = new JRadioButton("Simple", true);
        generalGameButton = new JRadioButton("General");
        ButtonGroup g = new ButtonGroup();
        g.add(simpleGameButton);
        g.add(generalGameButton);

        top.add(simpleGameButton);
        top.add(generalGameButton);

        boardSizeField = new JTextField("5", 3);
        top.add(new JLabel("Board size: "));
        top.add(boardSizeField);

        JButton start = new JButton("Start");
        start.addActionListener(e -> startGame());
        top.add(start);

        add(top, BorderLayout.NORTH);

        JPanel side = new JPanel(new GridLayout(2, 1));

        JPanel bluePanel = new JPanel(new GridLayout(3, 1));
        bluePanel.add(new JLabel("Blue"));
        blueS = new JRadioButton("S", true);
        blueO = new JRadioButton("O");
        ButtonGroup bG = new ButtonGroup();
        bG.add(blueS);
        bG.add(blueO);
        bluePanel.add(blueS);
        bluePanel.add(blueO);
        side.add(bluePanel);

        JPanel redPanel = new JPanel(new GridLayout(3, 1));
        redPanel.add(new JLabel("Red"));
        redS = new JRadioButton("S", true);
        redO = new JRadioButton("O");
        ButtonGroup rG = new ButtonGroup();
        rG.add(redS);
        rG.add(redO);
        redPanel.add(redS);
        redPanel.add(redO);
        side.add(redPanel);

        add(side, BorderLayout.WEST);

        turnLabel = new JLabel("Current turn: blue", SwingConstants.CENTER);
        add(turnLabel, BorderLayout.SOUTH);
    }

    private void setupCanvas()
    {
        canvas = new GameBoardCanvas();
        canvas.setPreferredSize(new Dimension(400, 400));
        add(canvas, BorderLayout.CENTER);
    }

    private void startGame()
    {
        try
        {
            int size = Integer.parseInt(boardSizeField.getText());
            if (size < 3)
            {
                JOptionPane.showMessageDialog(this, "Board size must be at least 3.");
                return;
            }

            if (simpleGameButton.isSelected())
                game = new SimpleGame(size);
            else
                game = new GeneralGame(size);

            canvas.setPreferredSize(new Dimension(size * CELL_SIZE, size * CELL_SIZE));
            pack();
            canvas.repaint();

            turnLabel.setText("Current turn: blue");
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "Invalid board size.");
        }
    }

    class GameBoardCanvas extends JPanel
    {
        public GameBoardCanvas()
        {
            addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent e)
                {
                    if (game == null || game.isGameOver())
                        return;

                    int r = e.getY() / CELL_SIZE;
                    int c = e.getX() / CELL_SIZE;

                    if (r < 0 || r >= game.getSize() || c < 0 || c >= game.getSize())
                        return;

                    if (game.getCell(r, c) != ' ')
                        return;

                    char letter = (game.getCurrentPlayer() == 'B')
                            ? (blueS.isSelected() ? 'S' : 'O')
                            : (redS.isSelected() ? 'S' : 'O');

                    game.makeMove(r, c, letter);
                    repaint();

                    if (game.isGameOver())
                    {
                        JOptionPane.showMessageDialog(GUI.this, "Game Over: " + game.getWinner());
                        return;
                    }

                    turnLabel.setText("Current turn: " +
                            (game.getCurrentPlayer() == 'B' ? "blue" : "red"));
                }
            });
        }

        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if (game == null)
                return;

            int size = game.getSize();
            g.setFont(new Font("Arial", Font.BOLD, SYMBOL_SIZE));

            for (int r = 0; r < size; r++)
            {
                for (int c = 0; c < size; c++)
                {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    char val = game.getCell(r, c);
                    if (val != ' ')
                    {
                        g.setColor(game.getPlayerAt(r, c) == 'B'
                                ? new Color(0, 102, 204)
                                : new Color(204, 0, 0));

                        g.drawString("" + val,
                                c * CELL_SIZE + 16,
                                r * CELL_SIZE + 34);
                    }
                }
            }

            for (BaseGame.SOSLine line : game.scoredLines)
            {
                g.setColor(line.player == 'B' ? new Color(0, 102, 204) : new Color(204, 0, 0));
                g.drawLine(
                        line.c1 * CELL_SIZE + CELL_SIZE / 2,
                        line.r1 * CELL_SIZE + CELL_SIZE / 2,
                        line.c2 * CELL_SIZE + CELL_SIZE / 2,
                        line.r2 * CELL_SIZE + CELL_SIZE / 2
                );
            }
        }
    }
    
    public void startGameFromTest(BaseGame g)
    {
        this.game = g;
        canvas.setPreferredSize(new Dimension(g.getSize() * CELL_SIZE, g.getSize() * CELL_SIZE));
        pack();
        repaint();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(GUI::new);
    }
}

