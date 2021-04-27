Feature: Incident Creation

  As an EasyVista user, I want to create an Incident in Jira so that the information between Jira and EasyVista can be shared.

  Background:
    Given the site URL is http://jira-ev.mylittleco.fr
  Scenario Outline: Creating an incident
    Given an incident is created in EasyVista
    And the incident priority in EasyVista is <evpriority>
    And the incident summary in EasyVista is <evsummary>
    When the incident is sent to Jira by <user> authenticated by <password>
    And <user> is connected to Jira with password <password>
    Then the incident is created in Jira
    And the incident priority in Jira is <jirapriority>
    And the incident summary in Jira is <evsummary>
    Examples:
      | evpriority | evsummary        | user | password | jirapriority |
      | "2"        | "Hello everyone" | paul | popaul   | "2-Medium"   |
