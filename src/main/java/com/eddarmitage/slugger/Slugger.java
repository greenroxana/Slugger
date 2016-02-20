package com.eddarmitage.slugger;

import com.eddarmitage.slugger.splitting.WordSplitters;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.OptionalInt;

/**
 * An object used to produce URL-friendly slugs
 * <p>
 * Instances of a {@code Slugger} object provide a way to generate "slugs",
 * that can be used in URLs on the web. Inputs can be split into words in a
 * variety of ways and the length of the output can be restricted if required.
 * Any @{link CharSequence} can be used as the joining character between
 * consecutive words. Characters that would require escaping in a URL are
 * removed or replaced, as appropriate.
 */
public class Slugger {
    private static final String NOT_SLUG_CHARS_REGEX = "\\W";
    private static final String DEFAULT_SEPARATOR = "-";

    private final WordSplitter wordSplitter;

    private final OptionalInt targetLength;
    private final boolean enforceHardLimit;
    private final CharSequence separator;
    private CharacterReplacer characterReplacer;


    /**
     * Creates a {@code Slugger} with the given configuration.
     * @param targetLength  the target length that most slugs will not normally be longer than.
     * @param enforceHardLimit  force the slugs to be no longer than the target length
     * @param separator  the character sequence used to join consecutive words in the slug
     * @param characterReplacer  the character replacer that will be used to remove illegal characters
     */
    private Slugger(WordSplitter wordSplitter, OptionalInt targetLength, boolean enforceHardLimit, CharSequence separator, CharacterReplacer characterReplacer) {
        this.wordSplitter = wordSplitter;
        this.targetLength = targetLength;
        this.enforceHardLimit = enforceHardLimit;
        this.characterReplacer = characterReplacer;
        this.separator = separator;
    }

    /**
     * Creates a basic Slugger with the default configuration.
     * <p>
     * Creates a slugger, that splits the input based on whitespace, has no target output length,
     * produces lower-case output (using the the default {@link Locale}), and joins words with
     * hyphens.
     *
     * @return a basic Slugger
     */
    public static Slugger create() {
        return new Slugger(WordSplitters.whitespaceWordSplitter(), OptionalInt.empty(), false, DEFAULT_SEPARATOR, new CharacterReplacer(Locale.getDefault(), false));
    }

    /**
     * Return a copy of this {@code Slugger} with a given target length set.
     * <p>
     * The output of the returned {@code Slugger} will not normally be longer
     * than {@code targetLength} characters long, unless the first word
     * produced by the {@link WordSplitter} is longer than this limit, and hard
     * limits are not being enforced.
     *
     * @param targetLength  the length that the output should be shorter than
     * @return a {@code Slugger} based on this slugger but with a target length
     */
    public Slugger withTargetLength(final int targetLength) {
        return new Slugger(wordSplitter, OptionalInt.of(targetLength), enforceHardLimit, separator, characterReplacer);
    }

    /**
     * Return a copy of this {@code Slugger} that enforces hard limits.
     * <p>
     * When hard limits are enforced, the output of {@link #sluggify(String)}
     * will never be longer than {@code targetLength}. Note that
     * {@code targetLength} has to be set before hard limits are enforced.
     *
     * @return a {@code Slugger} based on this slugger but that enforces hard limits
     * @throws IllegalArgumentException if no {@code targetLength} has been set
     */
    public Slugger withHardLimitsEnforced() {
        if (!targetLength.isPresent()) {
            throw new IllegalArgumentException("Can't enforce hard limits without target length");
        }
        return new Slugger(wordSplitter, targetLength, true, separator, characterReplacer);
    }

    /**
     * Return a copy of this {@code Slugger} with the specified seperator sequence
     * <p>
     * Specify the {@link CharSequence} that will be used to join words in the
     * slug.
     *
     * @param separator  the separator to be used in slugs
     * @return a {@code Slugger} based on this slugger but with the given word-seperator
     */
    public Slugger withSeparator(CharSequence separator) {
        return new Slugger(wordSplitter, targetLength, enforceHardLimit, separator, characterReplacer);
    }

    /**
     * Return a copy of this {@code Slugger} with the specified {@code WordSplitter}
     * <p>
     * Replaces the {@link WordSplitter} used by this slugger with the one
     * supplied as a parameter.
     *
     * @param wordSplitter  the {@code WordSplitter} to be used
     * @return a {@code Slugger} based on this slugger but with the given {@code WordSplitter}
     */
    public Slugger withWordSplitter(WordSplitter wordSplitter) {
        return new Slugger(wordSplitter, targetLength, enforceHardLimit, separator, characterReplacer);
    }

    /**
     * Return a copy of this {@code Slugger} that also splits according to the supplied {@code WordSplitter}
     * <p>
     * The supplied {@link WordSplitter} will be chained onto the current one,
     * so that the input to {@link #sluggify(String)} will be split according
     * to both the existing splitter <em>and</em> the additional splitter.
     *
     * @param additionalWordSplitter  the {@code WordSplitter} to be chained onto the current {@code WordSplitter}
     * @return a {@code Slugger} based on this slugger but with the given {@code WordSplitter} chained to the current {@code WordSplitter}
     */
    public Slugger withAdditionalWordSplitter(WordSplitter additionalWordSplitter) {
        return new Slugger(wordSplitter.withAdditionalSplitter(additionalWordSplitter), targetLength, enforceHardLimit, separator, characterReplacer);
    }

    /**
     * Returns a version of this slugger that preserves the case of the input.
     * <p>
     * The slugger returned from this method will produce slugs that match the
     * case of the input provided to {@link #sluggify(String)}.
     *
     * @return a {@code Slugger} based on this slugger that preserves input case
     */
    public Slugger withCasePreserved() {
        return new Slugger(wordSplitter, targetLength, enforceHardLimit, separator, characterReplacer.withCasePreserved());
    }

    /**
     * Generates a slug-version of the provided input.
     * <p>
     * The output will nto contain any characters that require escaping for use
     * in a URL. The output will consist of a number of <em>words</em> joined
     * by a separator string. How the words are split, how words are joined and
     * other options are determined by the configuration of the Slugger.
     *
     * @param input  the input string to be turned into a slug
     * @return The URL-safe slug, which may be an empty String
     * @throws NullPointerException if the input is null
     */
    public String sluggify(final String input) {
        if (input == null) {
            throw new NullPointerException("Input to Slugger.sluggify() is null");
        }

        return wordSplitter.splitWords(input.trim())
                .map(characterReplacer::replaceCharacters)
                .map(this::replaceNonSlugCharacters)
                .collect(SlugBuilder.collector(targetLength, enforceHardLimit, separator));
    }

    private String replaceNonSlugCharacters(String word) {
        return Normalizer.normalize(word, Form.NFC)
                .replaceAll(NOT_SLUG_CHARS_REGEX, "");
    }
}
