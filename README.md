[![ASERVO Software GmbH](https://aservo.github.io/img/aservo_atlassian_banner.png)](https://www.aservo.com/en/atlassian)

ConfAPI for Jira
================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.aservo.atlassian/jira-confapi-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.aservo.atlassian/jira-confapi-plugin)
[![Build Status](https://circleci.com/gh/aservo/jira-confapi-plugin.svg?style=shield)](https://circleci.com/gh/aservo/jira-confapi-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=aservo_jira-confapi-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=aservo_jira-confapi-plugin)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aservo_jira-confapi-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=aservo_jira-confapi-plugin)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

REST API for automated Jira configuration.

Related Documentation
---------------------

* [Atlassian REST API design guidelines version 1](https://developer.atlassian.com/server/framework/atlassian-sdk/atlassian-rest-api-design-guidelines-version-1/)

Resources
---------

All resources produce JSON (media type:  `application/json`) results.

### Settings

Access important Jira settings like the title, the base url, the mode
etc.

* #### `GET /rest/confapi/1/settings`

  Get Jira application settings.

  __Responses__

  ![Status 200][status-200]

  ```javascript
  {
    "baseurl": "http://localhost:2990/jira",
    "mode": "private",
    "title": "Your Company Jira"
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

* #### `PUT /rest/confapi/1/settings`

  Set Jira application settings.

  __Request Body__

  Media type: `application/json`

  Content: Settings, for example:

  ```javascript
  {
    "baseurl": "http://localhost:2990/jira",
    "mode": "private",
    "title": "Your Company Jira"
  }
  ```

  __Request Parameters__

  None.

  __Responses__

  ![Status 200][status-200]

  Returned if request could be executed without major exceptions.

  The response will contain a list of errors that occurred while setting
  some specific values such as a string that was too long, for example:

  ```
  {
    "errorMessages": [
        "The length of the application title must not exceed 255 characters"
    ],
    "errors": {}
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

### Licenses

The Jira license API is a bit weird and needs to be well understood.
Just like in the web interface, different license keys can theoretically
be set for each application (Jira Core, Jira Software, etc.). However,
the entered license key is always set for all applications for which it
is valid.

For example:

1. Jira Core and Jira Software each have their own license key. If a
license key is now entered (in the web interface or via the REST API)
with which both applications can be licensed, this license key is stored
for both applications.

2. Jira Core and Jira Software use a common license key. If a license
key is now entered (in the web interface or via the REST API) with which
only Jira Core can be licensed, the license key is also only stored for
Jira Core.

So again, an entered license key is always stored for all applications
for which it is valid. The web interface might suggest that you can
select the desired application, but this is not true.

* #### `GET /rest/confapi/1/licenses`

  Get the license keys together with the application keys of the
  applications using this license key.

  __Responses__

  ![Status 200][status-200]

  ```javascript
  {
    "licenses": [
      {
        "key": "AAA...",
        "applicationKeys": [
          "jira-software"
        ]
      },
      {
        "key": "AAA...",
        "applicationKeys": [
          "jira-core",
          "jira-servicedesk"
        ]
      }
    ]
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

* #### `PUT /rest/confapi/1/licenses`

  Set a license by its license key.

  __Request Body__

  Media type: `text/plain`

  Content: License key, for example:

  ```
  AAA...
  ```

  __Request Parameters__

  | parameter   | type      | description                                                                    |
  | ----------- | --------- | ------------------------------------------------------------------------------ |
  | `clear`     | _boolean_ | Clear all licenses before setting the new license, optional, defaults to false |

  __Responses__

  ![Status 200][status-200]

  ```javascript
  {
    "key": "AAA...",
    "applicationKeys": [
      "jira-core",
      "jira-servicedesk",
      "jira-software"
    ]
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

[status-200]: https://img.shields.io/badge/status-200-brightgreen.svg
[status-400]: https://img.shields.io/badge/status-400-red.svg
[status-401]: https://img.shields.io/badge/status-401-red.svg
[status-403]: https://img.shields.io/badge/status-403-red.svg
[status-404]: https://img.shields.io/badge/status-404-red.svg
