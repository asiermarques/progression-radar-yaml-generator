# Progression Radar yaml generator

[![CircleCI](https://circleci.com/gh/asiermarques/progression-radar-yaml-generator.svg?style=svg)](https://circleci.com/gh/asiermarques/progression-radar-yaml-generator)  

![Cats Friendly Badge](https://typelevel.org/cats/img/cats-badge-tiny.png) 

A command line application to generate the data yaml files from external sources for [Progression Radar](https://github.com/asiermarques/progression-radar) tool.

Note: at this point, only the *jira* datasource is implemented, but you can implement many other sources in a easy way

## Running the project

### Configure the environment variables and run the application

The first step is configure the variables for the sources.  
At this point there is only support for Jira source  

```
export JIRA_URL=https://your_jira_space.atlassian.net/rest/api/2/
export JIRA_USERNAME=asiermarques@your_domain.com
export JIRA_TOKEN=yourtoken
export JIRA_PROJECT=TEST
```

...and run the app

```
sbt run
```

### Export the data

With this simple command you can export all the issues in the specified Jira project and they will be transformed in the Progress Radar yaml format
```
generate --source jira --output-file /target_dir/data.yaml
```

## Running the tests

You can run the tests with the command

```
sbt tests
```