[![ASERVO Software GmbH](https://aservo.github.io/img/aservo_atlassian_banner.png)](https://www.aservo.com/en/atlassian)

ConfAPI for JIRA
================

[![Build Status](https://api.travis-ci.org/aservo/jira-confapi-plugin.svg?branch=master)](https://travis-ci.org/aservo/jira-confapi-plugin)
[![Coverage Status](https://coveralls.io/repos/github/aservo/jira-confapi-plugin/badge.svg?branch=master)](https://coveralls.io/github/aservo/jira-confapi-plugin?branch=master)
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

REST API for automated JIRA configuration.

Resources
---------

All resources produce JSON (media type:  `application/json`) results.

### License

The JIRA license API is a bit weird and needs to be well understood.
Just like in the web interface, different license keys can theoretically
be set for each application (JIRA Core, JIRA Software, etc.). However,
the entered license key is always set for all applications for which it
is valid.

For example:

1. JIRA Core and JIRA Software each have their own license key. If a
license key is now entered (in the web interface or via the REST API)
with which both applications can be licensed, this license key is stored
for both applications.

2. JIRA Core and JIRA Software use a common license key. If a license
key is now entered (in the web interface or via the REST API) with which
only JIRA Core can be licensed, the license key is also only stored for
JIRA Core.

So again, an entered license key is always stored for all applications
for which it is valid. The web interface might suggest that you can
select the desired application, but this is not true.

#### `/rest/confapi/1/license`

* `PUT /rest/confapi/1/license`

  Set a license by its license key.

  __Request Body__

  Media type: `text/plain`

  Content: License key, for example:

  ```
  AAA...
  ```

  __Request Parameters__

  | parameter   | type      | description         |
  | ----------- | --------- | ------------------- |
  | `clear`     | _boolean_ | Not implemented yet |

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

#### `/rest/confapi/1/licenses`

* `GET /rest/confapi/1/licenses`

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

[status-200]: https://img.shields.io/badge/status-200-brightgreen.svg
[status-400]: https://img.shields.io/badge/status-400-red.svg
[status-401]: https://img.shields.io/badge/status-401-red.svg
[status-403]: https://img.shields.io/badge/status-403-red.svg
[status-404]: https://img.shields.io/badge/status-404-red.svg