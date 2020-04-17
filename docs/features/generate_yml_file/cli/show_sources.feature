Feature: show the available sources from command line
  Scenario: obtaining the list of available sources when sources are implemented
    Given the following implemented sources:
    | jira   |
    | trello |
    When the user executes the show-sources command
    Then the client receives the terminal output "jira, trello"