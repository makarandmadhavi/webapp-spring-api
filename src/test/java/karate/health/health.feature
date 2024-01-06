Feature: Hello World

  Background:
    Given url 'http://localhost:8080'
    Given path '/healthz'

  Scenario: Healthz returns success on startup

    When method GET
    Then status 200