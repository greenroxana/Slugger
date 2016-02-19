package com.eddarmitage.slugger.splitting;

import com.eddarmitage.slugger.WordSplitter;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * {@code WordSplitter} implementations, as well as methods to create WordSplitters from a Regex String.
 */
public class WordSplitters {

    private static final Pattern WHITESPACE_WORD_PATTERN = Pattern.compile("\\s+");
    private static final Pattern CAMEL_CASE_SPLIT_PATTERN = Pattern.compile("((?=[A-Z][a-z])|(?<=[a-z])(?=[A-Z])(?=[A-Z]))");
    private static final Pattern UNDERSCORE_SPLIT_PATTERN = Pattern.compile("_+");

    private WordSplitters() {
        throw new AssertionError("Utility class not designed for instantiation");
    }

    /**
     * A {@code WordSplitter} instance that splits based on whitespace.
     *
     * @return a {@link WordSplitter} instance that splits based on whitespace
     */
    public static WordSplitter whitespaceWordSplitter() {
        return new RegexWordSplitter(WHITESPACE_WORD_PATTERN);
    }

    /**
     * A {@code WordSplitter} instance that splits camel-case inputs.
     *
     * @return a {@link WordSplitter} instance that splits camel-case inputs
     */
    public static WordSplitter camelCaseWordSplitter() {
        return new RegexWordSplitter(CAMEL_CASE_SPLIT_PATTERN);
    }

    /**
     * A {@code WordSplitter} instance that splits on underscores.
     *
     * @return a {@link WordSplitter} instance that splits on underscores
     */
    public static WordSplitter underscoreWordSplitter() {
        return new RegexWordSplitter(UNDERSCORE_SPLIT_PATTERN);
    }

    /**
     * A {@code WordSplitter} that splits according to a supplied regular expression {@code String}
     * <p>
     * The underlying implementation splits the provided input using the
     * {@link Pattern#splitAsStream(CharSequence)} method.
     *
     * @param regex  the regular expression that this splitter will be based on
     * @return a {@link WordSplitter} instance that splits according to the supplied regex
     * @throws PatternSyntaxException if the regex's syntax is invalid
     */
    public static WordSplitter withRegex(String regex) {
        return new RegexWordSplitter(Pattern.compile(regex));
    }

    /**
     * A {@code WordSplitter} that splits according to a supplied regular expression {@code String}
     * <p>
     * The underlying implementation splits the provided input using the
     * {@link Pattern#splitAsStream(CharSequence)} method.
     *
     * @param regex  the compiled regular expression pattern that this splitter will be based on
     * @return a {@link WordSplitter} instance that splits according to the supplied {@link Pattern}
     */
    public static WordSplitter withRegex(Pattern regex) {
        return new RegexWordSplitter(regex);
    }

}
