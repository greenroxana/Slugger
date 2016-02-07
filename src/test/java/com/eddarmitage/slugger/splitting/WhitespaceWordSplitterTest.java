package com.eddarmitage.slugger.splitting;

import com.eddarmitage.slugger.WordSplitter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhitespaceWordSplitterTest {

    private WordSplitter splitter;

    @Before
    public void setup() {
        splitter = WordSplitters.whitespaceWordSplitter();
    }

    @Test
    public void testWhitespaceWordSplitter_splitsOnSpaces() {
        assertThat(splitter.splitWords("Once I had a love and it was a gas")).hasSize(10);
    }

    @Test
    public void testWhitespaceWordSplitter_splitsOnMultipleSpaces() {
        assertThat(splitter.splitWords("All I  want   is    a    room   with  a view")).hasSize(9);
    }

    @Test
    public void testWhitespaceWordSplitter_splitsOnTabs() {
        assertThat(splitter.splitWords("When I met you in the restaurant\tyou could tell I was no debutante.")).hasSize(14);
    }

    @Test
    public void testWhitespaceWordSplitter_splitsOnNewLines() {
        assertThat(splitter.splitWords("She moves like she don't care \nSmooth as silk, cool as air")).hasSize(12);
    }

    @Test
    public void testWhitespaceWordSplitter_dealsWithInitialWhitespace() {
        assertThat(splitter.splitWords("   I know a girl from a lonely street")).hasSize(8);
    }

    @Test
    public void testWhitespaceWordSplitter_dealsWithTrailingWhitespace() {
        assertThat(splitter.splitWords("Color me your color, baby      ")).hasSize(5);
    }
}
