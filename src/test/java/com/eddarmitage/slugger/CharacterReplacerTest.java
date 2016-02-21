package com.eddarmitage.slugger;

import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class CharacterReplacerTest {

    @Test
    public void testCharactersConvertedToLowercase_whenPreserveCaseDisabled() {
        CharacterReplacer characterReplacer = new CharacterReplacer(Locale.ENGLISH, false);
        assertThat(characterReplacer.replaceCharacters("PsychicSpiesFromChinaTryToStealYourMindsElation"))
                .isEqualTo("psychicspiesfromchinatrytostealyourmindselation");
    }

    @Test
    public void testCaseIsPreserved_whenPreserveCaseEnabled() {
        CharacterReplacer characterReplacer = new CharacterReplacer(Locale.ENGLISH, true);
        assertThat(characterReplacer.replaceCharacters("SometimesIFeelLikeIDontHaveAPartner"))
                .isEqualTo("SometimesIFeelLikeIDontHaveAPartner");
    }

    @Test
    public void testEnforcedCaseMutator_setsConstructorFlagCorrectly() {
        CharacterReplacer toLowerCaseReplacer = new CharacterReplacer(Locale.ENGLISH, false);
        CharacterReplacer preservedCaseReplacer = new CharacterReplacer(Locale.ENGLISH, true);

        String input = "CantStopAddictedToThisShindig";

        assertThat(toLowerCaseReplacer.withCasePreserved().replaceCharacters(input))
                .isEqualTo(preservedCaseReplacer.replaceCharacters(input));
    }

    @Test
    public void testCharactersAreReplaced_whenSpecificReplacementIsGiven() {
        CharacterReplacer characterReplacer = new CharacterReplacer(Locale.ENGLISH, false)
                .withReplacement('ä', "a")
                .withReplacement('Ä', "A");

        assertThat(characterReplacer.replaceCharacters("StändingInLineToSeeTheShowTonightÄndTheresÄLightOn"))
                .isEqualTo("standinginlinetoseetheshowtonightandtheresalighton");
    }
}