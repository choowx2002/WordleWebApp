package edu.virginia.sde.hw2.wordle;

import java.util.Random;

import static edu.virginia.sde.hw2.wordle.GameStatus.*;

/**
 * This class represents a game of Wordle.
 */

public class Game {

    /**
     * The default number of guesses the player starts with
     */
    public static final int STARTING_GUESSES = 5;
    /**
     * A dictionary containing the list of words the player is allowed to guess
     */
    private final Dictionary guessDictionary;

    /**
     * The word the player is trying to guess
     */
    private final String answer;

    /**
     * The number of guesses the player has remaining
     */
    private int guessesRemaining;

    /**
     * The current status of the game.
     */
    private GameStatus gameStatus;


    /**
     * Random number generator for selecting random answers.
     */
    private static final Random random = new Random();

    /**
     * Creates a "default" wordle game, using the default guess dictionary
     * (see {@link DefaultDictionaries#getGuessesDictionary()}), selecting a random word from the default answer dictionary
     * (see {@link DefaultDictionaries#getAnswersDictionary()}) as the answer the player is trying to guess, giving
     * 6 guesses for the user to start with, and setting the game status to {@link GameStatus#PLAYING}.
     */
    public Game() {
        this(DefaultDictionaries.getGuessesDictionary(),
                DefaultDictionaries.getAnswersDictionary().getRandomWord(random),
                STARTING_GUESSES,
                PLAYING);
    }

    /**
     * Constructor for Wordle Game
     * @param guessDictionary the dictionary of allowed guesses
     * @param answer the answer the player is trying to guess
     * @param guessesRemaining the number of guesses remaining the player has at the time the game is created
     * @param gameStatus the initial game status
     */
    protected Game(Dictionary guessDictionary, String answer, int guessesRemaining, GameStatus gameStatus) {
        validate(guessDictionary, answer, guessesRemaining, gameStatus);

        this.guessDictionary = guessDictionary;
        this.answer = answer;
        this.guessesRemaining = guessesRemaining;
        this.gameStatus = gameStatus;
    }

    /**
     * Get the dictionary containing the valid guesses for this game
     */
    public Dictionary getGuessDictionary() {
        return guessDictionary;
    }

    /**
     * Get the game's answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Get the number of guesses remaining
     */
    public int getGuessesRemaining() {
        return guessesRemaining;
    }

    /**
     * Get the current game status;
     * <ul>
     * <li>{@link GameStatus#PLAYING} - the game is ongoing</li>
     * <li>{@link GameStatus#WIN} - the game is over, and the player won</li>
     * <li>{@link GameStatus#LOSS} - the game is over, and the player lost</li>
     * </ul>
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Returns true if the game is over (either {@link GameStatus#WIN} or {@link GameStatus#LOSS})
     */
    public boolean isGameOver() {
        return gameStatus != PLAYING;
    }

    /**
     * Handles a player's guess in Wordle. This includes getting the result of the guess, as well as decrementing the
     * game's guesses remaining, and handling win and loss conditions.
     *
     * @param guess A player guessed word
     * @throws GameAlreadyOverException if the game is already over (see {@link Game#isGameOver()}
     * @throws IllegalWordException if the word is not in the games Guess Dictionary - That is,
     *                                  it's not a real 5-letter word.
     */
    public GuessResult submitGuess(String guess) {
        if (isGameOver())
            throw new GameAlreadyOverException("Game is already over");
        if (!guessDictionary.contains(guess))
            throw new IllegalWordException("Invalid word provided");
        GuessResult result = new GuessResult(guess, answer);
        if (result.isCorrect())
            gameStatus = WIN;
        else if (guessesRemaining <= 1)
            gameStatus = LOSS;
        guessesRemaining--;

        return result;
    }

    private static void validate(Dictionary guessDictionary, String answer, int guessesRemaining, GameStatus gameStatus) {
        validateGuessDictionary(guessDictionary);
        validateAnswer(answer, guessDictionary);
        validateGuessesRemaining(guessesRemaining);
        validateGameStatus(gameStatus, guessesRemaining);
    }

    private static void validateGuessDictionary(Dictionary guessDictionary) {
        if (guessDictionary.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a Game with an empty guessDictionary");
        }
    }

    private static void validateAnswer(String answer, Dictionary guessDictionary) {
        if (answer == null) {
            throw new IllegalArgumentException("Answer cannot be null");
        }
        if (!guessDictionary.contains(answer)) {
            throw new IllegalArgumentException(String.format(
                    "Answer %s is not in the provided Guess Dictionary", answer));
        }
    }

    private static void validateGuessesRemaining(int guessesRemaining) {
        if (guessesRemaining < 0) {
            throw new IllegalArgumentException("The starting guessesRemaining value must be non-negative");
        }
    }

    private static void validateGameStatus(GameStatus gameStatus, int guessesRemaining) {
        if (gameStatus == null) {
            throw new IllegalArgumentException("gameStatus cannot be null");
        }
        if (guessesRemaining > 0 && gameStatus == LOSS) {
            throw new IllegalArgumentException("The game cannot be in a LOSS state with more than zero guesses remaining");
        }
        if (guessesRemaining == 0 && gameStatus == PLAYING) {
            throw new IllegalArgumentException("The game cannot be in a PLAYING state with zero guesses remaining");
        }
    }
}
