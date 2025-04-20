package edu.virginia.sde.hw2.wordle;

import java.util.Scanner;

/**
 * A command-line application for playing Wordle.
 */

public class PlayWordle {
    private static final Scanner scanner = new Scanner(System.in);
    private static Game game;

    public static void main(String[] args) {
        do {
            playNewWordleGame();
            displayWinLossMessage();
        } while (isPlayingAgain());
        System.out.println("Thank you for playing!");
    }

    private static void playNewWordleGame() {
        System.out.println("Welcome to Wordle! Try to guess the 5-letter word!");
        game = new Game();
        while (!game.isGameOver()) {
            System.out.printf("You have %d guesses remaining\n", game.getGuessesRemaining());
            var guess = getGuessFromUser();
            try {
                var guessResult = game.submitGuess(guess);
                displayGuessResult(guessResult);
            } catch (IllegalArgumentException e) {
                System.out.printf("Your guess %s is invalid. Try again with a 5-letter real word!\n", guess);
            }
        }
    }

    private static void displayWinLossMessage() {
        if (game.getGameStatus() == GameStatus.WIN) {
            System.out.printf("Congratulations! You won! You took %d guesses\n",
                    Game.STARTING_GUESSES - game.getGuessesRemaining());
        } else {
            System.out.printf("I'm sorry, you lost! The correct word was %s.\n", game.getAnswer());
        }
    }

    private static boolean isPlayingAgain() {
        while (true) {
            System.out.println("Would you like to play again? (Yes/No): ");
            var userEntry = scanner.nextLine();
            if (userEntry.toUpperCase().startsWith("Y")) {
                return true;
            } else if (userEntry.toUpperCase().startsWith("N")) {
                return false;
            }
            System.out.println("I'm sorry, I can't understand that. Please enter Yes or No.");
        }
    }

    private static String getGuessFromUser() {
        System.out.print("Enter a guess: ");
        return scanner.nextLine();
    }

    private static void displayGuessResult(GuessResult guessResult) {
        var guess = guessResult.getGuess();
        var letterResults = guessResult.getLetterResults();
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < letterResults.length; index++) {
            stringBuilder.append(switch (letterResults[index]) {
                case BLUE -> getBlueLetter(guess.charAt(index));
                case YELLOW -> getYellowLetter(guess.charAt(index));
                case GRAY -> getGrayLetter(guess.charAt(index));
            });
        }
        System.out.println(stringBuilder);
    }

    private static String getBlueLetter(char guessLetter) {
        return String.format("\033[1;42m%c\033[0m", guessLetter);
    }

    private static String getYellowLetter(char guessLetter) {
        return String.format("\033[1;43m%c\033[0m", guessLetter);
    }

    private static String getGrayLetter(char guessLetter) {
        return String.format("\033[1;47m%c\033[0m", guessLetter);
    }
}
