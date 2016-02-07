package com.eddarmitage.slugger.splitting;

import com.eddarmitage.slugger.WordSplitter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CamelCaseWordSplitterTest {

    private WordSplitter splitter;

    @Before
    public void setup() {
        splitter = WordSplitters.camelCaseWordSplitter();
    }

    @Test
    public void testCamelCaseSplitter_splitsOnCaseChange() {
        assertThat(splitter.splitWords("TheseMistCoveredMountains")).hasSize(4);
    }

    @Test
    public void testCamelCaseSplitter_doesntSplitOnWhitespace() {
        assertThat(splitter.splitWords("a love-struck romeo sings a streetsuss serenade")).hasSize(1);
    }

    @Test
    public void testCamelCaseSplitter_splitsOnConsecutiveUppercaseLetters() {
        assertThat(splitter.splitWords("iAmJeremiahDixonIAmAGeordieBoy")).hasSize(9);
    }

    @Test
    public void testCamelCaseSplitter_doesntSplitAllCapsInput() {
        assertThat(splitter.splitWords("THEY HAD A LAST SUPPER THE DAY OF THE BEACHING")).hasSize(1);
    }

    @Test
    public void testCamelCaseSplitter_doesntSplitAccronyms() {
            assertThat(splitter.splitWords("YouPlayTheGuitarOnTheMTV")).hasSize(7);
    }

    @Test
    public void testCamelCaseSplitter_splitsCorrectlyAfterAnAccronym() {
        assertThat(splitter.splitWords("YouPlayTheGuitarOnTheMTVThatAintWorkin")).hasSize(10);
    }
}
