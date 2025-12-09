package sosgame.product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

@SuppressWarnings("serial")
public class GUI extends JFrame
{
    private static final int CELL_SIZE = 50;
    private static final int SYMBOL_SIZE = 36;
    private static final int COMPUTER_DELAY = 300;

    private BaseGame game;
    private GameBoardCanvas canvas;
    private JTextField boardSizeField;
    private JLabel turnLabel;
    private JRadioButton simpleGameButton;
    private JRadioButton generalGameButton;
    private JRadioButton blueHuman, blueComputer;
    private JRadioButton redHuman, redComputer;
    private JRadioButton blueS, blueO;
    private JRadioButton redS, redO;
    private JCheckBox recordCheckBox;
    private JButton replayButton;

    private Random rand = new Random();

    private final GameRecorder recorder = new GameRecorder();
    private boolean replaying = false;
    private Timer replayTimer;
    private GameRecorder.ReplayData replayData;
    private int replayIndex = 0;

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

        updateReplayButtonEnabled();
    }

    private void setupControls()
    {
        JPanel top = new JPanel(new FlowLayout());

        simpleGameButton = new JRadioButton("Simple", true);
        generalGameButton = new JRadioButton("General");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(simpleGameButton);
        modeGroup.add(generalGameButton);

        top.add(simpleGameButton);
        top.add(generalGameButton);

        top.add(new JLabel("Board size:"));
        boardSizeField = new JTextField("5", 3);
        top.add(boardSizeField);

        JButton start = new JButton("New Game");
        start.addActionListener(e -> startGame());
        top.add(start);

        replayButton = new JButton("Replay");
        replayButton.addActionListener(e -> replayLastGame());
        top.add(replayButton);

        add(top, BorderLayout.NORTH);

        JPanel side = new JPanel(new GridLayout(2, 1));

        JPanel bluePanel = new JPanel(new GridLayout(5, 1));
        bluePanel.add(new JLabel("Blue Player"));

        blueHuman = new JRadioButton("Human", true);
        blueComputer = new JRadioButton("Computer");

        ButtonGroup blueType = new ButtonGroup();
        blueType.add(blueHuman);
        blueType.add(blueComputer);

        bluePanel.add(blueHuman);

        blueS = new JRadioButton("S", true);
        blueO = new JRadioButton("O");
        ButtonGroup blueLetter = new ButtonGroup();
        blueLetter.add(blueS);
        blueLetter.add(blueO);
        bluePanel.add(blueS);
        bluePanel.add(blueO);

        bluePanel.add(blueComputer);

        side.add(bluePanel);

        JPanel redPanel = new JPanel(new GridLayout(5, 1));
        redPanel.add(new JLabel("Red Player"));

        redHuman = new JRadioButton("Human", true);
        redComputer = new JRadioButton("Computer");

        ButtonGroup redType = new ButtonGroup();
        redType.add(redHuman);
        redType.add(redComputer);

        redPanel.add(redHuman);

        redS = new JRadioButton("S", true);
        redO = new JRadioButton("O");
        ButtonGroup redLetter = new ButtonGroup();
        redLetter.add(redS);
        redLetter.add(redO);
        redPanel.add(redS);
        redPanel.add(redO);

        redPanel.add(redComputer);

        side.add(redPanel);

        add(side, BorderLayout.WEST);

        JPanel bottom = new JPanel(new BorderLayout());
        recordCheckBox = new JCheckBox("Record game");
        bottom.add(recordCheckBox, BorderLayout.WEST);

        turnLabel = new JLabel("Current turn: blue", SwingConstants.CENTER);
        bottom.add(turnLabel, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);
    }

    private void setupCanvas()
    {
        canvas = new GameBoardCanvas();
        canvas.setPreferredSize(new Dimension(400, 400));
        add(canvas, BorderLayout.CENTER);
    }

    private void startGame()
    {
        try {
            if (replayTimer != null && replayTimer.isRunning()) {
                replayTimer.stop();
            }
            replaying = false;

            int size = Integer.parseInt(boardSizeField.getText());
            if (size < 3) {
                JOptionPane.showMessageDialog(this, "Board size must be at least 3.");
                return;
            }

            boolean simpleMode = simpleGameButton.isSelected();
            game = simpleMode ?
                    new SimpleGame(size) :
                    new GeneralGame(size);

            Player blue = blueHuman.isSelected() ?
                    new HumanPlayer('B') : new ComputerPlayer('B');

            Player red = redHuman.isSelected() ?
                    new HumanPlayer('R') : new ComputerPlayer('R');

            game.setPlayers(blue, red);

            canvas.setPreferredSize(new Dimension(size * CELL_SIZE, size * CELL_SIZE));
            pack();
            canvas.repaint();

            turnLabel.setText("Current turn: blue");
            recorder.startRecording(size, simpleMode, recordCheckBox.isSelected());

            maybeComputerMove();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid board size.");
        }
    }

    private boolean isHumanTurn() {
        if (game == null || replaying) return false;
        return (game.getCurrentPlayer() == 'B') ? blueHuman.isSelected() : redHuman.isSelected();
    }

    private boolean isComputerTurn() {
        if (game == null || replaying) return false;
        return (game.getCurrentPlayer() == 'B') ? blueComputer.isSelected() : redComputer.isSelected();
    }

    private void maybeComputerMove()
    {
        if (game == null || game.isGameOver()) return;
        if (!isComputerTurn()) return;

        Timer t = new Timer(COMPUTER_DELAY, e -> {
            ((Timer)e.getSource()).stop();
            computerMove();
        });
        t.setRepeats(false);
        t.start();
    }

    private void applyMove(int r, int c, char letter) {
        if (game == null || game.isGameOver()) return;
        char player = game.getCurrentPlayer();
        boolean moved = game.makeMove(r, c, letter);
        if (moved) {
            recorder.recordMove(r, c, letter, player);
        }
    }

    private void computerMove()
    {
        if (game == null || game.isGameOver()) return;
        if (!isComputerTurn()) return;

        Player cpu = game.getCurrentPlayerObject();
        Move m = cpu.getMove(game);

        if (m != null)
            applyMove(m.r, m.c, m.letter);

        canvas.repaint();

        if (game.isGameOver()) {
            recorder.finishRecording();
            JOptionPane.showMessageDialog(this, "Game Over: " + game.getWinner());
            updateReplayButtonEnabled();
            return;
        }

        turnLabel.setText("Current turn: " +
                (game.getCurrentPlayer() == 'B' ? "blue" : "red"));
        maybeComputerMove();
    }

    private void updateReplayButtonEnabled() {
        replayButton.setEnabled(recorder.hasSavedGame());
    }

    private void replayLastGame() {
        if (replayTimer != null && replayTimer.isRunning()) {
            replayTimer.stop();
        }

        GameRecorder.ReplayData data = recorder.loadLastGame();
        if (data == null) {
            JOptionPane.showMessageDialog(this, "No recorded game found.");
            updateReplayButtonEnabled();
            return;
        }

        BaseGame g = data.simpleMode ? new SimpleGame(data.boardSize)
                                     : new GeneralGame(data.boardSize);
        g.setPlayers(new HumanPlayer('B'), new HumanPlayer('R'));
        startGameFromTest(g);

        replaying = true;
        replayData = data;
        replayIndex = 0;
        turnLabel.setText("Replaying recorded game...");

        replayTimer = new Timer(COMPUTER_DELAY, e -> {
            if (game == null) {
                ((Timer) e.getSource()).stop();
                replaying = false;
                return;
            }
            if (replayIndex >= replayData.moves.size()) {
                ((Timer) e.getSource()).stop();
                replaying = false;
                turnLabel.setText("Replay finished: " + game.getWinner());
                return;
            }

            GameRecorder.RecordedMove m = replayData.moves.get(replayIndex++);
            game.makeMove(m.r, m.c, m.letter);
            canvas.repaint();
        });
        replayTimer.start();
    }

    class GameBoardCanvas extends JPanel
    {
        public GameBoardCanvas()
        {
            addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent e)
                {
                    if (game == null || game.isGameOver()) return;
                    if (!isHumanTurn()) return;

                    int r = e.getY() / CELL_SIZE;
                    int c = e.getX() / CELL_SIZE;

                    if (r < 0 || r >= game.getSize() || c < 0 || c >= game.getSize())
                        return;

                    if (game.getCell(r, c) != ' ')
                        return;

                    char letter = (game.getCurrentPlayer() == 'B') ?
                            (blueS.isSelected() ? 'S' : 'O') :
                            (redS.isSelected() ? 'S' : 'O');

                    applyMove(r, c, letter);
                    repaint();

                    if (game.isGameOver()) {
                        recorder.finishRecording();
                        JOptionPane.showMessageDialog(GUI.this, "Game Over: " + game.getWinner());
                        updateReplayButtonEnabled();
                        return;
                    }

                    turnLabel.setText("Current turn: " +
                            (game.getCurrentPlayer() == 'B' ? "blue" : "red"));

                    maybeComputerMove();
                }
            });
        }

        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if (game == null) return;

            int size = game.getSize();
            g.setFont(new Font("Arial", Font.BOLD, SYMBOL_SIZE));

            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {

                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    char val = game.getCell(r, c);
                    if (val != ' ') {
                        g.setColor(game.getPlayerAt(r, c) == 'B'
                                ? new Color(0, 102, 204)
                                : new Color(204, 0, 0));

                        g.drawString("" + val,
                                c * CELL_SIZE + 16,
                                r * CELL_SIZE + 34);
                    }
                }
            }

            for (BaseGame.SOSLine line : game.scoredLines) {
                g.setColor(line.player == 'B' ?
                        new Color(0, 102, 204) :
                        new Color(204, 0, 0));

                g.drawLine(
                        line.c1 * CELL_SIZE + CELL_SIZE/2,
                        line.r1 * CELL_SIZE + CELL_SIZE/2,
                        line.c2 * CELL_SIZE + CELL_SIZE/2,
                        line.r2 * CELL_SIZE + CELL_SIZE/2
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
