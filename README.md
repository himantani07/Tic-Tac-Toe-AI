# Tic-Tac-Toe AI — Java Swing GUI

A clickable 3×3 Tic-Tac-Toe game. You play **X**; the AI plays **O**.

## Features
- Click empty squares to place X.
- AI automatically chooses O's move.
- Minimax algorithm with Alpha-Beta Pruning.
- AI cannot be beaten with correct play; perfect play ends in a draw.
- Restart button and win/loss/draw messages.
- Java 8+; no external libraries.

## Run in VS Code on Windows
1. Extract the ZIP and open the `tic-tac-toe-ai-gui` folder in VS Code.
2. Open Terminal → New Terminal.
3. Compile: `javac TicTacToeAI.java`
4. Run: `java TicTacToeAI`

Or double-click `run.bat` if Java's `javac` and `java` commands are on PATH.

## Algorithm
Minimax evaluates future outcomes. The AI maximizes its score and assumes the human minimizes it. Alpha-Beta Pruning skips branches that cannot change the final decision. Center/corner-first move ordering improves pruning.
