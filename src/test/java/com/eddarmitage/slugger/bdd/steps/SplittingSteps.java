package com.eddarmitage.slugger.bdd.steps;

import com.eddarmitage.slugger.bdd.SluggerHelper;
import com.eddarmitage.slugger.splitting.WordSplitters;
import cucumber.api.java.en.Given;

public class SplittingSteps {

    private final SluggerHelper sluggerHelper;

    public SplittingSteps(SluggerHelper sluggerHelper) {
        this.sluggerHelper = sluggerHelper;
    }

    @Given("^camel case splitting is enabled$")
    public void enableCamelCaseSplitting() throws Throwable {
        sluggerHelper.set(sluggerHelper.get().withAdditionalWordSplitter(WordSplitters.camelCaseWordSplitter()));
    }

    @Given("^underscore splitting is enabled$")
    public void enableUnderscoreSplitting() throws Throwable {
        sluggerHelper.set(sluggerHelper.get().withAdditionalWordSplitter(WordSplitters.underscoreWordSplitter()));
    }
}
