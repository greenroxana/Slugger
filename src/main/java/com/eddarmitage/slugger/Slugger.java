package com.eddarmitage.slugger;

import com.eddarmitage.slugger.splitting.WordSplitters;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.OptionalInt;

public class Slugger {
    private static final String NOT_SLUG_CHARS_REGEX = "\\W";
    private static final String DEFAULT_SEPARATOR = "-";

    private final OptionalInt targetLength;
    private final boolean enforceHardLimit;
    private final CharSequence separator;
    private final WordSplitter wordSplitter;


    /**
     * Creates a {@code Slugger} with the given configuration.
     *
     * @param targetLength the target length that most slugs will not normally be longer than.
     * @param enforceHardLimit force the slugs to be no longer than the target length
     * @param separator the character sequence used to join consecutive words in the slug
     */
    private Slugger(WordSplitter wordSplitter, OptionalInt targetLength, boolean enforceHardLimit, CharSequence separator) {
        this.wordSplitter = wordSplitter;
        this.targetLength = targetLength;
        this.enforceHardLimit = enforceHardLimit;
        this.separator = separator;
    }

    public static Slugger create() {
        return new Slugger(WordSplitters.whitespaceWordSplitter(), OptionalInt.empty(), false, DEFAULT_SEPARATOR);
    }

    public Slugger withTargetLength(final int targetLength) {
        return new Slugger(wordSplitter, OptionalInt.of(targetLength), enforceHardLimit, separator);
    }

    public Slugger withSeparator(CharSequence separator) {
        return new Slugger(wordSplitter, targetLength, enforceHardLimit, separator);
    }

    public Slugger withHardLimitsEnforced() {
        return new Slugger(wordSplitter, targetLength, true, separator);
    }

    public Slugger withWordSplitter(WordSplitter wordSplitter) {
        return new Slugger(wordSplitter, targetLength, enforceHardLimit, separator);
    }

    public Slugger withAdditionalWordSplitter(WordSplitter additionalWordSplitter) {
        return new Slugger(wordSplitter.withAdditionalSplitter(additionalWordSplitter), targetLength, enforceHardLimit, separator);
    }

    /**
     * Generates a slug-version of the provided input.
     *
     * <p>See the following samples of slugs generated with a {@code targetLength}
     * of 25 characters, as taken from the Spock Specification:
     *
     * <table summary="Sample inputs and outputs">
     *     <tr><th>Input</th><th>Output</th></tr>
     *     <tr><td>{@code "post"}</td><td>{@code "post"}</td></tr>
     *     <tr><td>{@code "POST!"}</td><td>{@code "post"}</td></tr>
     *     <tr><td>{@code "postTitle"}</td><td>{@code "post-title"}</td></tr>
     *     <tr><td>{@code "post_title"}</td><td>{@code "post-title"}</td></tr>
     *     <tr><td>{@code "This is a very long title for a post"}</td>
     *          <td>{@code "this-is-a-very-long-title"}</td></tr>
     * </table>
     *
     * @param input the input string to be turned into a slug
     * @return The URL-safe slug
     */
    public String sluggify(final String input) {
        if (input == null) {
            throw new NullPointerException("Input to Slugger.sluggify() is null");
        }

        return wordSplitter.splitWords(input.trim())
                .map(Slugger::sluggifyWord)
                .collect(SlugBuilder.collector(targetLength, enforceHardLimit, separator));
    }

    private static String sluggifyWord(String word) {
        return Normalizer.normalize(word, Form.NFC)
                .replaceAll(NOT_SLUG_CHARS_REGEX, "")
                .toLowerCase(Locale.ENGLISH);
    }
}
