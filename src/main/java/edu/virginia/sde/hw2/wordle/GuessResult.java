package edu.virginia.sde.hw2.wordle;

import java.util.Arrays;

import static edu.virginia.sde.hw2.wordle.LetterResult.*;

/**
 * This class handles getting the result from a guess in a Wordle Game. This class is used by
 * {@link Game#submitGuess(String)}.
 */
public class GuessResult {
    private final String guess;
    private final String answer;

    /**
     * Constructor for GuessResult
     * @param guess - the Wordle player's guessed word
     * @param answer - the word the player is trying to guess
     * @throws IllegalArgumentException if either word is not 5-characters long.
     */
    public GuessResult(String guess, String answer) {
        validateWordLengths(guess, answer);
        this.guess = guess;
        this.answer = answer;
    }

    private static void validateWordLengths(String guess, String answer) {
        if (guess.length() != WordValidator.WORDLE_WORD_LENGTH || answer.length() != WordValidator.WORDLE_WORD_LENGTH) {
            throw new IllegalArgumentException(String.format("""
                            Invalid GuessResult initialization:
                                guess: %s
                                answer: %s
                            Words must be %d letters long.""",
                    guess, answer, WordValidator.WORDLE_WORD_LENGTH));
        }
    }

    /**
     * Returns the guess submitted by the player.
     */
    public String getGuess() {
        return guess;
    }

    /**
     * Returns the answer the player is trying to guess.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Returns true if the player's guess matches the answer (case-insensitive)
     */
    public boolean isCorrect() {return this.guess.equalsIgnoreCase(this.answer);}

    /**
     * Returns the {@link LetterResult} array of GREEN, YELLOW, and GRAY based on how well the player's guess. This
     * function is case-insensitive.
     */
    public LetterResult[] getLetterResults() {
        LetterResult[] results = new LetterResult[WordValidator.WORDLE_WORD_LENGTH];

        String lowerGuess = guess.toLowerCase();
        String lowerAnswer = answer.toLowerCase();


        boolean[] matchedInAnswer = new boolean[WordValidator.WORDLE_WORD_LENGTH];
        for (int i = 0; i < WordValidator.WORDLE_WORD_LENGTH; i++) {
            if (lowerGuess.charAt(i) == lowerAnswer.charAt(i)) {
                results[i] = GREEN;
                matchedInAnswer[i] = true;
            }
        }
        for (int i = 0; i < WordValidator.WORDLE_WORD_LENGTH; i++) {
            if (results[i] == null) {
                for (int j = 0; j < WordValidator.WORDLE_WORD_LENGTH; j++) {
                    if (!matchedInAnswer[j] && lowerGuess.charAt(i) == lowerAnswer.charAt(j)) {
                        results[i] = YELLOW;
                        matchedInAnswer[j] = true;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < WordValidator.WORDLE_WORD_LENGTH; i++) {
            if (results[i] == null)
                results[i] = GRAY;
        }
        return results;

    }
}
