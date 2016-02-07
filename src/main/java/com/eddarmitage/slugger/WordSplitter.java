package com.eddarmitage.slugger;

import java.util.stream.Stream;

@FunctionalInterface
public interface WordSplitter {
    Stream<String> splitWords(String input);

    default WordSplitter withAdditionalSplitter(WordSplitter additionalSplitter) {
        return input -> this.splitWords(input).flatMap(additionalSplitter::splitWords);
    }
}
