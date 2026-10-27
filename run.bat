@echo off
javac TicTacToeAI.java
if errorlevel 1 pause & exit /b 1
java TicTacToeAI
