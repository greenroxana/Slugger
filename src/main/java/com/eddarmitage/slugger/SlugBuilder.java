package com.eddarmitage.slugger;

import java.util.OptionalInt;
import java.util.StringJoiner;
import java.util.stream.Collector;

import static java.lang.Integer.min;

class SlugBuilder {

    private final OptionalInt targetLength;
    private final boolean enforceHardLimit;
    private final StringJoiner joiner;

    SlugBuilder(OptionalInt targetLength, boolean enforceHardLimit, CharSequence separator) {
        this.targetLength = targetLength;
        this.enforceHardLimit = enforceHardLimit;
        joiner = new StringJoiner(separator);
    }

    private SlugBuilder(OptionalInt targetLength, boolean enforceHardLimit, StringJoiner joiner) {
        this.targetLength = targetLength;
        this.enforceHardLimit = enforceHardLimit;
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
        return new SlugBuilder(targetLength, enforceHardLimit, joiner.merge(other.joiner));
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
        return (targetLength.isPresent() && joiner.length() + word.length() <= targetLength.getAsInt()) || !targetLength.isPresent();
    }

    public static Collector<String, SlugBuilder, String> collector(OptionalInt targetLength, boolean enforceHardLimit, CharSequence separator) {
        return Collector.of(
                () -> new SlugBuilder(targetLength, enforceHardLimit, separator),
                SlugBuilder::addWord,
                SlugBuilder::merge,
                SlugBuilder::build);
    }
}
