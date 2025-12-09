package sosgame.product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {

    private static final String FILE_NAME = "last_game_recording.txt";

    private boolean recording = false;
    private int boardSize;
    private boolean simpleMode;
    private final List<RecordedMove> moves = new ArrayList<>();

    public static class RecordedMove {
        public final int r, c;
        public final char letter;
        public final char player;

        public RecordedMove(int r, int c, char letter, char player) {
            this.r = r;
            this.c = c;
            this.letter = letter;
            this.player = player;
        }
    }

    public static class ReplayData {
        public final int boardSize;
        public final boolean simpleMode;
        public final List<RecordedMove> moves;

        public ReplayData(int boardSize, boolean simpleMode, List<RecordedMove> moves) {
            this.boardSize = boardSize;
            this.simpleMode = simpleMode;
            this.moves = moves;
        }
    }

    public void startRecording(int size, boolean simpleMode, boolean enabled) {
        moves.clear();
        this.boardSize = size;
        this.simpleMode = simpleMode;
        this.recording = enabled;
    }

    public void recordMove(int r, int c, char letter, char player) {
        if (!recording) return;
        moves.add(new RecordedMove(r, c, letter, player));
    }

    public void finishRecording() {
        if (!recording || moves.isEmpty()) {
            recording = false;
            return;
        }
        saveToFile();
        recording = false;
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write(boardSize + " " + (simpleMode ? "simple" : "general"));
            bw.newLine();
            for (RecordedMove m : moves) {
                bw.write(m.player + " " + m.r + " " + m.c + " " + m.letter);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasSavedGame() {
        return new File(FILE_NAME).exists();
    }

    public ReplayData loadLastGame() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String headerLine = br.readLine();
            if (headerLine == null) return null;

            String[] header = headerLine.trim().split("\\s+");
            if (header.length < 2) return null;

            int size = Integer.parseInt(header[0]);
            boolean simple = header[1].equalsIgnoreCase("simple");

            List<RecordedMove> loadedMoves = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 4) continue;

                char player = parts[0].charAt(0);
                int r = Integer.parseInt(parts[1]);
                int c = Integer.parseInt(parts[2]);
                char letter = parts[3].charAt(0);

                loadedMoves.add(new RecordedMove(r, c, letter, player));
            }

            if (loadedMoves.isEmpty()) return null;

            return new ReplayData(size, simple, loadedMoves);

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
