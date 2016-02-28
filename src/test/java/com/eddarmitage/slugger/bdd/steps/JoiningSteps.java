package com.eddarmitage.slugger.bdd.steps;

import com.eddarmitage.slugger.bdd.SluggerHelper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.assertj.core.api.Assertions.assertThat;

public class JoiningSteps {

    private final SluggerHelper sluggerHelper;
    private String separator = "-";

    public JoiningSteps(SluggerHelper sluggerHelper) {
        this.sluggerHelper = sluggerHelper;
    }

    @Given("^a separator of \"(.*)\"$")
    public void specifySeperator(String separator) {
        this.separator = separator;
        sluggerHelper.set(sluggerHelper.get().withSeparator(separator));
    }

    @Then("^the output will contain ([0-9]+) words$")
    public void countOutputWords(int expected) {
        int actual = sluggerHelper.getOutput().split(separator).length;
        assertThat(actual).isEqualTo(expected);
    }
}
