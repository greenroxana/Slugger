package com.eddarmitage.slugger.splitting;

import com.eddarmitage.slugger.WordSplitter;

import java.util.regex.Pattern;
import java.util.stream.Stream;

class RegexWordSplitter implements WordSplitter {

    private final Pattern wordSplitPattern;

    RegexWordSplitter(Pattern wordSplitPattern) {
        this.wordSplitPattern = wordSplitPattern;
    }

    @Override
    public Stream<String> splitWords(String input) {
        return wordSplitPattern.splitAsStream(input);
    }
}
