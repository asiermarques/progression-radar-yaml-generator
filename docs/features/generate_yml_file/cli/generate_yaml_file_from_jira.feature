Feature: generate the yaml file from jira using command line

  As a progression radar user
  I want to be able to generate a yaml file from Jira
  in order to use it in the progression radar webapp

  Background:

    The user will make use of the generate command in order to generate the YAML file that Progression Radar will use
    to show the different Categories and Skills that we have documented in the company.

    The generate command takes two arguments:
    - --output-file argument takes the absolute file url
    - --source one specifies the source of the

    shell:>generate --source jira --output-file /the_target_path/file.yml

    In this case, the source where we have all the information is Jira, and we are using the status as the Category name
    and the summary and description of the issue, as the information of every KPI.
    Also, we use the label to express the level of the KPI. The system will extract the number in the label that will be
    used as the level so the text on the label is irrelevant; the number is the info that matters.

    Given a Jira server with a project with a key "CP"
    And the project has the following issues data:
    | issue summary              | label        | status                |
    | active listening           | Level 1      | communication         |
    | drives meetings            | Level 2      | communication         |
    | solid principles           | Level 1      | software engineering  |
    | DDD                        | L 2          | software engineering  |

  Scenario: creating the yaml file without errors

    When the user executes the generate command with the "jira" source and the full path of the target file
    Then the client receives a success output with the path of the created file
    And the file contains the following yaml content:
    """
    - key: comm
      name: communication
      description: ""
      kpis:
      - summary: active listening
        description: ""
        level: 1
        tags: []
      - summary: drives meetings
        description: ""
        level: 2
        tags: []
    - key: soft
      name: software engineering
      description: ""
      kpis:
      - summary: solid principles
        description: ""
        level: 1
        tags: []
      - summary: DDD
        description: ""
        level: 2
        tags: []
    """
