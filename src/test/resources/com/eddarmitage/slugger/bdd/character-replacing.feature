Feature: Remove characters in slugs, replacing them where appropriate
  # Enter feature description here

  Scenario: Special characters should be removed from inputs
    When the input string "test input $%.@€/]{" is sluggified
    Then the output will contain a '-' character
