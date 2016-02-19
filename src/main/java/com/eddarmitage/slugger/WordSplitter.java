package com.eddarmitage.slugger;

import java.util.stream.Stream;

/**
 * A function that takes a String and splits it into a Stream of words.
 * <p>
 * Slugs are built by converting the input into a sequence of words, and then
 * joining the words together. A {@code WordSplitter} is what produces the
 * {@link Stream} of words that will make up the slug.
 */
@FunctionalInterface
public interface WordSplitter {
    /**
     * Splits a given input String into a Stream of words.
     *
     * @param input  the input
     * @return the stream of individual words
     */
    Stream<String> splitWords(String input);

    /**
     * Combines this {@code WordSplitter} instance with another.
     * <p>
     * In order to split an input into words based on multiple conditions (such
     * as splitting {@code camelCase} words as well as splitting on whitespace)
     * instances of this interface can be chained using this method.
     *
     * @param additionalSplitter  the {@code WordSplitter} to be chained on to this {@code WordSplitter}
     * @return the combined WordSplitter that splits words in multiple ways
     */
    default WordSplitter withAdditionalSplitter(WordSplitter additionalSplitter) {
        return input -> this.splitWords(input).flatMap(additionalSplitter::splitWords);
    }
}
