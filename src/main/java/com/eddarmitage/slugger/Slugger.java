package com.eddarmitage.slugger;

import com.eddarmitage.slugger.splitting.WordSplitters;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.OptionalInt;

public class Slugger {
    private static final String NOT_SLUG_CHARS_REGEX = "\\W";
    private static final String DEFAULT_SEPARATOR = "-";

    private final WordSplitter wordSplitter;

    private final OptionalInt targetLength;
    private final boolean enforceHardLimit;
    private final CharSequence separator;
    private final boolean preserveCase;
    private final Locale locale;


    /**
     * Creates a {@code Slugger} with the given configuration.
     *
     * @param targetLength     the target length that most slugs will not normally be longer than.
     * @param enforceHardLimit force the slugs to be no longer than the target length
     * @param separator        the character sequence used to join consecutive words in the slug
     */
    private Slugger(WordSplitter wordSplitter, OptionalInt targetLength, boolean enforceHardLimit, boolean preserveCase, CharSequence separator) {
        this.wordSplitter = wordSplitter;
        this.targetLength = targetLength;
        this.enforceHardLimit = enforceHardLimit;
        this.preserveCase = preserveCase;
        this.locale = Locale.getDefault();
        this.separator = separator;
    }

    public static Slugger create() {
        return new Slugger(WordSplitters.whitespaceWordSplitter(), OptionalInt.empty(), false, false, DEFAULT_SEPARATOR);
    }

    public Slugger withTargetLength(final int targetLength) {
        return new Slugger(wordSplitter, OptionalInt.of(targetLength), enforceHardLimit, preserveCase, separator);
    }

    public Slugger withSeparator(CharSequence separator) {
        return new Slugger(wordSplitter, targetLength, enforceHardLimit, preserveCase, separator);
    }

    public Slugger withHardLimitsEnforced() {
        return new Slugger(wordSplitter, targetLength, true, preserveCase, separator);
    }

    public Slugger withWordSplitter(WordSplitter wordSplitter) {
        return new Slugger(wordSplitter, targetLength, enforceHardLimit, preserveCase, separator);
    }

    public Slugger withAdditionalWordSplitter(WordSplitter additionalWordSplitter) {
        return new Slugger(wordSplitter.withAdditionalSplitter(additionalWordSplitter), targetLength, enforceHardLimit, preserveCase, separator);
    }

    public Slugger withCasePreserved() {
        return new Slugger(wordSplitter, targetLength, enforceHardLimit, true, separator);
    }

    /**
     * Generates a slug-version of the provided input.
     *
     * @param input the input string to be turned into a slug
     * @return The URL-safe slug
     */
    public String sluggify(final String input) {
        if (input == null) {
            throw new NullPointerException("Input to Slugger.sluggify() is null");
        }

        return wordSplitter.splitWords(input.trim())
                .map(this::replaceNonSlugCharacters)
                .map(this::changeCase)
                .collect(SlugBuilder.collector(targetLength, enforceHardLimit, separator));
    }

    private String replaceNonSlugCharacters(String word) {
        return Normalizer.normalize(word, Form.NFC)
                .replaceAll(NOT_SLUG_CHARS_REGEX, "");
    }

    private String changeCase(String word) {
        return preserveCase ? word : word.toLowerCase(locale);
    }
}
