package com.eddarmitage.slugger.bdd;

import com.eddarmitage.slugger.Slugger;
import com.eddarmitage.slugger.splitting.WordSplitters;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;

public class StepDefinitions {

    private Slugger slugger = Slugger.create();
    private String output = "";

    @Given("^a target length of (\\d+) characters$")
    public void setTargetLength(Integer target) {
        slugger = slugger.withTargetLength(target);
    }

    @Given("^a separator of \"(.*)\"$")
    public void specifySeperator(String separator) {
        slugger = slugger.withSeparator(separator);
    }

    @Given("^hard limits are enforced$")
    public void enforceHardLimits() throws Throwable {
        slugger = slugger.withHardLimitsEnforced();
    }

    @Given("^camel case splitting is enabled$")
    public void enableCamelCaseSplitting() throws Throwable {
        slugger = slugger.withAdditionalWordSplitter(WordSplitters.camelCaseWordSplitter());
    }

    @Given("^underscore splitting is enabled$")
    public void enableUnderscoreSplitting() throws Throwable {
        slugger = slugger.withAdditionalWordSplitter(WordSplitters.underscoreWordSplitter());
    }

    @Given("^case will be preserved$")
    public void case_will_be_preserved() throws Throwable {
        slugger = slugger.withCasePreserved();
    }

    @Given("^a replacement from '(.)' to \"(.+)\"$")
    public void a_replacement(Character c, String replacement) throws Throwable {
        slugger = slugger.withReplacement(c, replacement);
    }

    @When("^the input string \"(.*)\" is sluggified$")
    public void sluffigyString(String input) {
        output = slugger.sluggify(input);
    }

    @Then("^the output will not contain any '(.)' characters")
    public void checkOutputHasNoSpaces(Character character) {
        assertThat(output).doesNotContain(character.toString());
    }

    @Then("^the output will be (\\d+) characters long$")
    public void checkOutputHasLength(int length) throws Throwable {
        assertThat(output).as("Check \"%s\" is %d characters long", output, length).hasSize(length);

    }

    @Then("^the output will be no longer than (\\d+) characters long$")
    public void checkOutputIsShorterThanLength (Integer length) {
        assertThat(shouldBeLessOrEqual(output.length(), length)).as("Check length of \"%s\" is less than or equal to  %d", output, length);
    }

    @Then("^the output will be longer than (\\d+) characters long$")
    public void checkOutputIsLongerThanLength(Integer length) {
        assertThat(shouldBeGreater(output.length(), length)).as("Check length of \"%s\" is greater than %d", output, length);
    }

    @Then("^the output will contain (a|[0-9]+) '(.)' characters?$")
    public void checkOutputContainsCharacter(String quantity, Character character) throws Throwable {
        int expected = quantity.equals("a") ? 1 : Integer.parseInt(quantity);
        int actual = output.split(character.toString(), -1).length - 1;
        assertThat(actual)
                .as("Check \"%s\" contains exactly %d occurrences of '%c'", output, expected, character)
                .isEqualTo(expected);
    }
}
