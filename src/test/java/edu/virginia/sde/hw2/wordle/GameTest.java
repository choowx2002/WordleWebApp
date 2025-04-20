package edu.virginia.sde.hw2.wordle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static edu.virginia.sde.hw2.wordle.GameStatus.*;
import static edu.virginia.sde.hw2.wordle.LetterResult.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private static Dictionary defaultGuessesDictionary, defaultAnswersDictionary;
    @BeforeAll
    public static void initialize() {
        defaultGuessesDictionary = DefaultDictionaries.getGuessesDictionary();
        defaultAnswersDictionary = DefaultDictionaries.getAnswersDictionary();
    }
    @BeforeEach
    void setUp() {
        game = new Game();
    }
    @Test
    public void test_init_zeroArgumentConstructor() {
        var game = new Game();

        assertEquals(defaultGuessesDictionary, game.getGuessDictionary());
        assertTrue(defaultAnswersDictionary.contains(game.getAnswer()));
        assertEquals(5, game.getGuessesRemaining());
        assertEquals(PLAYING, game.getGameStatus());
    }

    @Test
    public void test_init_4ArgumentConstructor() {
        var game = new Game(defaultGuessesDictionary, "TREND", 6, PLAYING);

        assertEquals(defaultGuessesDictionary, game.getGuessDictionary());
        assertEquals("TREND", game.getAnswer());
        assertEquals(5, game.getGuessesRemaining());
        assertEquals(PLAYING, game.getGameStatus());
    }

    @Test
    public void test_isGameOver_WIN_True() {
        var game = new Game(defaultGuessesDictionary, "TREND", 5, WIN);

        assertEquals(WIN, game.getGameStatus());
        assertTrue(game.isGameOver());
    }
    @Test
    void submitGuess_IncorrectGuess() {
        GuessResult result = game.submitGuess("TREND");
        assertFalse(result.isCorrect());
        assertEquals(PLAYING, game.getGameStatus());
    }
    @Test
    void submitGuess_GameAlreadyOver() {
        Game gameSpecific = new Game(DefaultDictionaries.getGuessesDictionary(), "apple", 0, LOSS);
        assertThrows(GameAlreadyOverException.class, () -> gameSpecific.submitGuess("apple"));
    }
    @Test
    void testSubmitGuess_IllegalWord() {
        assertThrows(IllegalWordException.class, () -> game.submitGuess("lione"));  //
    }
    @Test
    void submitGuess_DecrementedGuesses() {
        int initialGuesses = game.getGuessesRemaining();
        game.submitGuess("lions");
        assertEquals(initialGuesses - 1, game.getGuessesRemaining());
    }

    @Test
    void submitGuess_GameOverAfterAllGuessesUsed() {
        Game gameSpecific = new Game(DefaultDictionaries.getGuessesDictionary(), "lions", 1, PLAYING);
        gameSpecific.submitGuess("peach");
        assertEquals(0, gameSpecific.getGuessesRemaining());
        assertEquals(LOSS, gameSpecific.getGameStatus());
    }

    @Test
    public void emptyStringGuess() {
        assertThrows(IllegalWordException.class, () -> game.submitGuess(""));
    }
    @Test
    public void guessWithWhitespace() {
        assertThrows(IllegalWordException.class, () -> game.submitGuess("lion "));
    }

    @Test
    public void caseInsensitivity() {
        Game gameSpecific = new Game(defaultGuessesDictionary, "lions", 6, PLAYING);
        GuessResult result = gameSpecific.submitGuess("LIONS");
        assertTrue(result.isCorrect());
        assertEquals(WIN, gameSpecific.getGameStatus());
    }
    @Test
    public void overMaxGuesses() {
        Game gameSpecific = new Game(defaultGuessesDictionary, "lions", 1, PLAYING);
        gameSpecific.submitGuess("tiger");
        assertEquals(0, gameSpecific.getGuessesRemaining());
        assertEquals(LOSS, gameSpecific.getGameStatus());
        assertThrows(GameAlreadyOverException.class, () -> gameSpecific.submitGuess("lions"));
    }

    @Test
    public void illegalWordDoesNotDecrementGuess() {
        int initialGuesses = game.getGuessesRemaining();
        assertThrows(IllegalWordException.class, () -> game.submitGuess("lion!"));
        assertEquals(initialGuesses, game.getGuessesRemaining());
    }

    @Test
    public void submitGuessAfterLoss() {
        Game gameSpecific = new Game(defaultGuessesDictionary, "lions", 0, LOSS);
        assertThrows(GameAlreadyOverException.class, () -> gameSpecific.submitGuess("lions"));
    }


}