package com.eddarmitage.slugger.bdd.steps;

import com.eddarmitage.slugger.bdd.SluggerHelper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneralSteps {

    private final SluggerHelper sluggerHelper;

    public GeneralSteps(SluggerHelper sluggerHelper) {
        this.sluggerHelper = sluggerHelper;
    }

    @Given("^case will be preserved$")
    public void case_will_be_preserved() throws Throwable {
        sluggerHelper.set(sluggerHelper.get().withCasePreserved());
    }

    @Given("^a replacement from '(.)' to \"(.+)\"$")
    public void a_replacement(Character c, String replacement) throws Throwable {
        sluggerHelper.set(sluggerHelper.get().withReplacement(c, replacement));
    }

    @When("^the input string \"(.*)\" is sluggified$")
    public void sluggifyString(String input) {
        sluggerHelper.applyInput(input);
    }

    @Then("^the output will not contain any '(.)' characters")
    public void checkOutputHasNoSpaces(Character character) {
        assertThat(sluggerHelper.getOutput()).doesNotContain(character.toString());
    }

    @Then("^the output will contain (a|[0-9]+) '(.)' characters?$")
    public void checkOutputContainsCharacter(String quantity, Character c) {
        int expected = quantity.equals("a") ? 1 : Integer.parseInt(quantity);
        int actual = sluggerHelper.getOutput().split(c.toString(), -1).length - 1;
        assertThat(actual)
                .as("Check \"%s\" contains exactly %d occurrences of '%c'", sluggerHelper.getOutput(), expected, c)
                .isEqualTo(expected);
    }
}
