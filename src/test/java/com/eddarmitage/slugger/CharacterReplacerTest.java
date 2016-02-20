package com.eddarmitage.slugger;

import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class CharacterReplacerTest {

    @Test
    public void testCharactersConvertedToLowercase_whenPreserveCaseDisabled() {
        CharacterReplacer characterReplacer = new CharacterReplacer(Locale.ENGLISH, false);
        assertThat(characterReplacer.replaceCharacters("PsYcHiC SpIeS FrOm cHiNa tRy tO StEaL YoUr mInDs eLaTiOn"))
                .isEqualTo("psychic spies from china try to steal your minds elation");
    }

    @Test
    public void testCaseIsPreserved_whenPreserveCaseEnabled() {
        CharacterReplacer characterReplacer = new CharacterReplacer(Locale.ENGLISH, true);
        assertThat(characterReplacer.replaceCharacters("SoMeTiMeS I FeEl lIkE I DoNt hAvE A PaRtNeR"))
                .isEqualTo("SoMeTiMeS I FeEl lIkE I DoNt hAvE A PaRtNeR");
    }

    @Test
    public void testEnforcedCaseMutator_setsConstructorFlagCorrectly() {
        CharacterReplacer toLowerCaseReplacer = new CharacterReplacer(Locale.ENGLISH, false);
        CharacterReplacer preservedCaseReplacer = new CharacterReplacer(Locale.ENGLISH, true);

        String input = "CaNt sToP AdDiCtEd tO ThIs sHiNdIg";

        assertThat(toLowerCaseReplacer.withCasePreserved().replaceCharacters(input))
                .isEqualTo(preservedCaseReplacer.replaceCharacters(input));

    }
}