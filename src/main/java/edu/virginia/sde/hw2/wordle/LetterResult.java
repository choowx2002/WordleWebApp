package edu.virginia.sde.hw2.wordle;

/**
 * This enum gives the possible outcomes for each letter in a guess in Wordle.
 */

public enum LetterResult {
    /**
     * Correct letter and correct location
     */
    GREEN,
    /**
     * The letter appears in the answer, but not at this location
     */
    YELLOW, //Correct Letter, but incorrect location
    /**
     * The letter does not appear in the answer
     */
    GRAY //Incorrect Letter
}
