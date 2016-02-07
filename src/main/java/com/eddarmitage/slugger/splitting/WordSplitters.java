package com.eddarmitage.slugger.splitting;

import com.eddarmitage.slugger.WordSplitter;

import java.util.regex.Pattern;

public class WordSplitters {

    private static final Pattern CAMEL_CASE_SPLIT_PATTERN = Pattern.compile("((?<=[a-z])(?=[A-Z])(?=[A-Z][a-z]))");
    private static final Pattern UNDERSCORE_SPLIT_PATTERN = Pattern.compile("_+");

    private WordSplitters() {

    }

    public static WordSplitter camelCaseWordSplitter() {
        return new RegexWordSplitter(CAMEL_CASE_SPLIT_PATTERN);
    }

    public static WordSplitter underscoreWordSplitter() {
        return new RegexWordSplitter(UNDERSCORE_SPLIT_PATTERN);
    }

    public static WordSplitter withRegex(String regex) {
        return new RegexWordSplitter(Pattern.compile(regex));
    }

    public static WordSplitter withRegex(Pattern regex) {
        return new RegexWordSplitter(regex);
    }

}
