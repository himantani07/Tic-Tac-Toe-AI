import java.awt.*;
import javax.swing.*;

public class TicTacToeAI {
    private static final char X = 'X', O = 'O', EMPTY = ' ';
    private static final int[] ORDER = {4, 0, 2, 6, 8, 1, 3, 5, 7};
    private final char[] board = new char[9];
    private final JButton[] buttons = new JButton[9];
    private final JLabel status = new JLabel("Your turn , Select a Box to Place (X).", SwingConstants.CENTER);
    private final JFrame frame = new JFrame("Tic-Tac-Toe AI");
    private boolean gameOver = false, humanTurn = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToeAI().show());
    }

    private void show() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("TIC-TAC-TOE AI", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(35, 48, 85));
        title.setBorder(BorderFactory.createEmptyBorder(16, 10, 4, 10));
        frame.add(title, BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(8, 8));
        middle.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        status.setFont(new Font("SansSerif", Font.PLAIN, 15));
        middle.add(status, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 3, 7, 7));
        grid.setPreferredSize(new Dimension(360, 360));
        for (int i = 0; i < 9; i++) {
            final int p = i;
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("SansSerif", Font.BOLD, 54));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setForeground(new Color(35, 100, 190));
            buttons[i].setBorder(BorderFactory.createLineBorder(new Color(205, 212, 228), 2));
            buttons[i].addActionListener(e -> humanMove(p));
            grid.add(buttons[i]);
        }
        middle.add(grid, BorderLayout.CENTER);
        frame.add(middle, BorderLayout.CENTER);

        JButton restart = new JButton("Restart Game");
        restart.setFont(new Font("SansSerif", Font.BOLD, 15));
        restart.addActionListener(e -> reset());
        JPanel bottom = new JPanel();
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 14, 10));
        bottom.add(restart);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void humanMove(int p) {
        if (gameOver || !humanTurn || board[p] != EMPTY) return;
        move(p, X);
        if (checkEnd()) return;

        humanTurn = false;
        status.setText("AI is thinking...");
        setBoardEnabled(false);

        Timer timer = new Timer(250, e -> {
            ((Timer) e.getSource()).stop();
            int pAI = bestMove();
            if (pAI >= 0) move(pAI, O);
            if (!checkEnd()) {
                humanTurn = true;
                status.setText("Your turn , Select a Box to Place (X).");
                setBoardEnabled(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void move(int p, char player) {
        board[p] = player;
        buttons[p].setText(String.valueOf(player));
        buttons[p].setEnabled(false);
        buttons[p].setForeground(player == X ? new Color(35, 100, 190) : new Color(210, 70, 75));
    }

    private boolean checkEnd() {
        char winner = winner(board);
        if (winner == X) end("You win! Great job.");
        else if (winner == O) end("AI wins. Try again!");
        else if (full(board)) end("It's a draw!");
        else return false;
        return true;
    }

    private void end(String message) {
        gameOver = true;
        humanTurn = false;
        setBoardEnabled(false);
        status.setText(message);
        JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setBoardEnabled(boolean enabled) {
        for (int i = 0; i < 9; i++) buttons[i].setEnabled(enabled && board[i] == EMPTY);
    }

    private void reset() {
        for (int i = 0; i < 9; i++) {
            board[i] = EMPTY;
            buttons[i].setText("");
            buttons[i].setEnabled(true);
            buttons[i].setBackground(Color.WHITE);
        }
        gameOver = false;
        humanTurn = true;
        status.setText("Your turn , Select a Box to Place (X).");
    }

    private int bestMove() {
        int bestScore = Integer.MIN_VALUE, best = -1;
        for (int p : ORDER) {
            if (board[p] == EMPTY) {
                board[p] = O;
                int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                board[p] = EMPTY;
                if (score > bestScore) { bestScore = score; best = p; }
            }
        }
        return best;
    }

    private int minimax(int depth, boolean maximizing, int alpha, int beta) {
        char w = winner(board);
        if (w == O) return 10 - depth;
        if (w == X) return depth - 10;
        if (full(board)) return 0;

        if (maximizing) {
            int best = Integer.MIN_VALUE;
            for (int p : ORDER) {
                if (board[p] == EMPTY) {
                    board[p] = O;
                    best = Math.max(best, minimax(depth + 1, false, alpha, beta));
                    board[p] = EMPTY;
                    alpha = Math.max(alpha, best);
                    if (beta <= alpha) break;
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int p : ORDER) {
                if (board[p] == EMPTY) {
                    board[p] = X;
                    best = Math.min(best, minimax(depth + 1, true, alpha, beta));
                    board[p] = EMPTY;
                    beta = Math.min(beta, best);
                    if (beta <= alpha) break;
                }
            }
            return best;
        }
    }

    private char winner(char[] b) {
        int[][] lines = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        for (int[] line : lines) {
            char c = b[line[0]];
            if (c != EMPTY && c == b[line[1]] && c == b[line[2]]) return c;
        }
        return EMPTY;
    }

    private boolean full(char[] b) {
        for (char c : b) if (c == EMPTY) return false;
        return true;
    }
}
