Feature: show the available sources from command line

  As a progression radar user
  I want to be able to show all the sources available
  in order to choose one source to generate the yaml files

  Scenario: obtaining the list of available sources when sources are implemented
    Given the following implemented sources:
    | jira   |
    | trello |
    When the user executes the show-sources command
    Then the client receives the terminal output "jira, trello"

  Scenario: obtaining an empty list of sources when there are no sources implemented
    Given there are no implemented sources
    When the user executes the show-sources command
    Then the client receives the terminal output "There are no implementations yet"