# API Endpoints
## Note
The document tries to introduce the API endpoints of the backend. The document is not complete the API endpoints may change in the future. To get the latest information about the API endpoints, please refer to the source code itself.

## Endpoints
* POST /api/clients:
    * Description: Create a new client
    * Request: 
        * Body: 
            
            ```json
            {
                "client_name":"CLIENT_NAME(STRING)",
                "client_email":"CLIENT_EMAIL(STRING)",
                "passwd":"PASSWD(STRING)"
            }
            ```
    * Response:
        * if the client is created successfully:
            * HTTP code: 201
            * BODY:
                ```json
                {
                    "client_name":"CLIENT_NAME(STRING)",
                    "client_email":"CLIENT_EMAIL(STRING)"
                }
                ```
        * if the client is not created because the client_email is already in use:
            * HTTP code: 409 conflict
            * BODY:
                empty
        * Other errors:
            * HTTP code: 500 internal server error
            * BODY:
                empty
* POST /api/clients/login:
    * Description: Login a client
    * Request: 
        * Body:
            ```json
            {
                "client_email":"CLIENT_EMAIL(STRING)",
                "passwd":"PASSWD(STRING)"
            }
            ```
    * Response:
        * if the client is logged in successfully:
            * HTTP code: 200
            * BODY:
                ```json
                {
                    "client_email":"CLIENT_EMAIL(STRING)",
                    "jwt":"JWT(STRING)",
                    "expiration_time":"EXPIRATION_TIME(STRING)"
                }
                ```
        * if the client is not logged in successfully because the client_email or passwd is incorrect:
            * HTTP code: 401 unauthorized
            * BODY:
                empty
        * Other errors:
            * HTTP code: 500 internal server error
            * BODY:
                empty

* GET /api/clients/{client_id}/access?type={type}&role={role}:
    * Description: Get client information.Can be used to show all companies and groups, that the client owns/ is admin of/ is member of, etc.

    valid type-role pairs:

    | type | role |
    |------|------|
    | company | owner |
    | company | admin |
    | company | member |
    | group | owner |
    | group | admin |
    | group | write_member |
    | group | member |

    * Request:
        * Header:
            * Authorization: Bearer jwt string
    * Response:
        * if the jwt is valid:
            
            * HTTP code: 200
            * BODY:
                ```json
                [
                    "CompanyName0",
                    "CompanyName1",
                    "CompanyName2"
                ]
                ```
                or
                ```json
                [
                    "GroupId0",
                    "GroupId1",
                    "GroupId2"
                ]
                ```
        * if the jwt is invalid or the client_id is not the same as the client_id in the jwt:
            * HTTP code: 401 unauthorized
            * BODY:
                empty
        * Client not found:
            * HTTP code: 404 not found
            * BODY:
                empty
        * Other errors:
            * HTTP code: 500 internal server error
            * BODY:
                empty

* GET /s/{short_url_suffix}:
    * Description: Redirect to the original URL
    * Request:
        * Path:
            * the full path would be used, including company prefix and short URL suffix
    * Response:
        * if the short URL exists:
            * Header: HTTP code 302, redirection to the original URL
        * if the short URL does not exist|is deleted|is not activated:
            * Header: HTTP code 302, redirection to "https://www.example.com"

* POST /api/url:
    * Description: Create a new short URL
    * Request:
        * Header: Authorization Bearer jwt string
        * Body:

            ```json
            {
                "suffix":"SHORT_URL(STRING)",
                "original_url":"ORIGINAL_URL(STRING)",
                "expiration_time_unix":"EXPIRATION_TIME(STRING)",
                "isActivated":"IS_ACTIVATED(BOOLEAN)",
                "groupId":"GROUP_ID(STRING)"
            }
            ```
    * Response:
        * if the short URL is created successfully:

            * HTTP code: 204
            * BODY:
                empty
        * if the short URL is not created beacuse of an invalid jwt | client has no permission to create short URL in the group:
            * HTTP code: 401 unauthorized
            * BODY:
                empty
        * if the client is not found or the group is not found:
            * HTTP code: 404 not found
            * BODY:
                empty
        * if the short URL is not created because the short URL is already in use:
            * HTTP code: 409 conflict
            * BODY:
                empty
        * Other errors:
            * HTTP code: 500 internal server error
            * BODY:
                empty
       
* DELETE /api/url?short_url={short_url}:
    * Description: Delete a short URL
    * Request:
        * Header: Authorization Bearer jwt string
    * Response:
        * if no error occurs:
            * HTTP code: 204
            * BODY:
                empty
        * if JWT is invalid | the client has no permission to delete the short URL:
            * HTTP code: 401 unauthorized
            * BODY:
                empty
        * if the short URL is not found:
            * HTTP code: 404 not found
            * BODY:
                empty
        * if the client is not found:
            * HTTP code: 400 bad request
            * BODY:
                empty
        * Other errors:
            * HTTP code: 500 internal server error
            * BODY:
                empty
       
* POST /api/companies
    * Description: Create a new company
    * Request:
        * Header: Authorization Bearer jwt string
        * Body:
            ```json
            {
                "company_name":"COMPANY_NAME(STRING)"
            }
            ```
    * Response:
        * if the company is created successfully:
            * HTTP code: 201
            * BODY:
                ```json
                {
                    "company_name":"COMPANY_NAME(STRING)",
                    "company_subscription_type":"COMPANY_SUBSCRIPTION_TYPE(STRING)",
                    "company_subscription_expiration_time":"COMPANY_SUBSCRIPTION_EXPIRATION_TIME(STRING)"
                }
                ```
        * if JWT is invalid:
            * HTTP code: 401 unauthorized
            * BODY:
                empty

        * if the company is not created because the company_prefix is already in use| other db related errors:
            * HTTP code: 409 conflict
            * BODY:
                empty
        * if the client is not found:
            * HTTP code: 400 bad request
            * BODY:
                empty
        * Other errors:
            * HTTP code: 500 internal server error
            * BODY:
                empty

* POST /api/companies/{companyName}/groups:
    * Description: Create a new group
    * Request:
        * Header: Authorization Bearer jwt string
        * Body:
            ```json
            {
                "group_name":"GROUP_NAME(STRING)"
            }
            ```
    * Response:
        * if the group is created successfully:
            * HTTP code: 201
            * BODY:
                ```json
                {
                    "group_id":"GROUP_ID(STRING)",
                    "group_name":"GROUP_NAME(STRING)",
                    "company_id":"COMPANY_ID(STRING)"
                }
                ```
        * if JWT is invalid | Client has no permission to create a group in the company:
            * HTTP code: 401 unauthorized
            * BODY:
                empty
        * if Client is not found:
            * HTTP code: 400 bad request
            * BODY:
                empty
        * Other errors:
            * HTTP code: 500 internal server error
            * BODY:
                empty

* POST /api/companies/{companyName}/members:
    * Description: Add a member to the company
    * Request:
        * Header: Authorization Bearer jwt string
        * Body:
            ```json
            {
                "email":"CLIENT_EMAIL(STRING)"
            }
            ```
    * Response:
        * if the member is added successfully:
            * HTTP code: 204
            * BODY:
                empty
        * if JWT is invalid | Client has no permission to add a member to the company:
            * HTTP code: 401 unauthorized
            * BODY:
                empty
        * if Client is not found:
            * HTTP code: 400 bad request
            * BODY:
                empty
        * if the member is not found:
            * HTTP code: 404 not found
            * BODY:
                empty
        * Other errors:
            * HTTP code: 500 internal server error
            * BODY:
                empty
* PUT

