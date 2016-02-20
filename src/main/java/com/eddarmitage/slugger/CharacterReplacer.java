package com.eddarmitage.slugger;

import java.util.Locale;

class CharacterReplacer {

    private final Locale locale;
    private final boolean preserveCase;

    CharacterReplacer(Locale locale, boolean preserveCase) {
        this.locale = locale;
        this.preserveCase = preserveCase;
    }

    public CharacterReplacer withCasePreserved() {
        return new CharacterReplacer(locale, true);
    }

    public String replaceCharacters(String input) {
        return changeCase(input);
    }

    private String changeCase(String word) {
        return preserveCase ? word : word.toLowerCase(locale);
    }
}
