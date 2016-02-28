package com.eddarmitage.slugger.bdd.steps;

import com.eddarmitage.slugger.bdd.SluggerHelper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;

public class LengthCuttingSteps {

    private final SluggerHelper sluggerHelper;

    public LengthCuttingSteps(SluggerHelper sluggerHelper) {
        this.sluggerHelper = sluggerHelper;
    }

    @Given("^a target length of (\\d+) characters$")
    public void setTargetLength(Integer target) {
        sluggerHelper.set(sluggerHelper.get().withTargetLength(target));
    }

    @Given("^hard limits are enforced$")
    public void enforceHardLimits() throws Throwable {
        sluggerHelper.set(sluggerHelper.get().withHardLimitsEnforced());
    }

    @Then("^the output will be (\\d+) characters long$")
    public void checkOutputHasLength(int length) throws Throwable {
        assertThat(sluggerHelper.getOutput())
                .as("Check \"%s\" is %d characters long", sluggerHelper.getOutput(), length).hasSize(length);
    }

    @Then("^the output will be no longer than (\\d+) characters long$")
    public void checkOutputIsShorterThanLength (Integer length) {
        assertThat(shouldBeLessOrEqual(sluggerHelper.getOutput().length(), length))
                .as("Check length of \"%s\" is less than or equal to  %d", sluggerHelper.getOutput(), length);
    }

    @Then("^the output will be longer than (\\d+) characters long$")
    public void checkOutputIsLongerThanLength(Integer length) {
        assertThat(shouldBeGreater(sluggerHelper.getOutput().length(), length))
                .as("Check length of \"%s\" is greater than %d", sluggerHelper.getOutput(), length);
    }
}
