package com.eddarmitage.slugger.splitting;

import com.eddarmitage.slugger.WordSplitter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnderscoreWordSplitterTest {
    private WordSplitter splitter;

    @Before
    public void setup() {
        splitter = WordSplitters.underscoreWordSplitter();
    }

    @Test
    public void testUnderscoreWordSplitter_splitsSimpleCase() {
        assertThat(splitter.splitWords("ground_control_to_major_tom")).hasSize(5);
    }

    @Test
    public void testUnderscoreWordSplitter_doesntSplitOnWhitespace() {
        assertThat(splitter.splitWords("i, i wish you could swim, like the dolphins, like dolphins can swim")).hasSize(1);
    }

    @Test
    public void testUnderscoreWordSplitter_dealsWithDoubleUnderscores() {
        assertThat(splitter.splitWords("__youve_got_your_mother_in_a_whirl__")).hasSize(7);
    }
}
