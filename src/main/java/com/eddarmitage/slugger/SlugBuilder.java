package com.eddarmitage.slugger;

import java.util.OptionalInt;
import java.util.StringJoiner;
import java.util.stream.Collector;

import static java.lang.Integer.min;

/**
 * A SlugBuilder acts like a {@link java.util.StringJoiner}, but with the
 * ability to constrain the length of the result.
 *
 * If a target length has been provided, then the SlugBuilder will produce an
 * output that is no longer than this length, except for the case when the
 * first addition to an empty SlugBuilder would exceed this limit, and
 * {@code enforceHardLimit} is set to False.
 *
 * If {@code enforceHardLimit} is enabled, then even in this case the initial
 * word added to the SlugBuilder will be truncated, such that the output is no
 * longer than the target length.
 */
class SlugBuilder {

    private final OptionalInt targetLength;
    private final boolean enforceHardLimit;
    private final CharSequence separator;
    private final StringJoiner joiner;

    SlugBuilder(CharSequence separator) {
        this.targetLength = OptionalInt.empty();
        this.enforceHardLimit = false;
        this.separator = separator;
        this.joiner = new StringJoiner(separator);
    }

    SlugBuilder(OptionalInt targetLength, boolean enforceHardLimit, CharSequence separator) {
        this.targetLength = targetLength;
        this.enforceHardLimit = enforceHardLimit;
        this.separator = separator;
        joiner = new StringJoiner(separator);
    }

    private SlugBuilder(OptionalInt targetLength, boolean enforceHardLimit, CharSequence separator, StringJoiner joiner) {
        this.targetLength = targetLength;
        this.enforceHardLimit = enforceHardLimit;
        this.separator = separator;
        this.joiner = joiner;
    }

    public void addWord(String word) {
        if (word.isEmpty()) {
            return;
        }

        if (joiner.length() == 0) {
            addFirstWord(word);
        } else if (wordWillFit(word)) {
            joiner.add(word);
        }
    }

    public SlugBuilder merge(SlugBuilder other) {
        return new SlugBuilder(targetLength, enforceHardLimit, separator, joiner.merge(other.joiner));
    }

    public String build() {
        return joiner.toString();
    }

    private void addFirstWord(String word) {
        if (enforceHardLimit) {
            joiner.add(word.substring(0, min(word.length(), targetLength.getAsInt())));
        } else {
            joiner.add(word);
        }
    }

    private boolean wordWillFit(String word) {
        return !targetLength.isPresent() || joiner.length() + word.length() + separator.length() <= targetLength.getAsInt();
    }

    public static Collector<String, SlugBuilder, String> collector(OptionalInt targetLength, boolean enforceHardLimit, CharSequence separator) {
        return Collector.of(
                () -> new SlugBuilder(targetLength, enforceHardLimit, separator),
                SlugBuilder::addWord,
                SlugBuilder::merge,
                SlugBuilder::build);
    }
}
