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
                    "client_id":"CLIENT_ID(STRING)",
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
                    "client_id":"CLIENT_ID(STRING)",
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

* GET /api/clients/{client_id}:
    * Description: Get client information. Currently only the client himself/herself can access this endpoint.
    * Request:
        * Header:
            * Authorization: Bearer jwt string
    * Response:
        * if the jwt is valid:
            
            * HTTP code: 200
            * BODY:
                ```json
                {
                    "client_id":"CLIENT_ID(STRING)",
                    "client_name":"CLIENT_NAME(STRING)",
                    "client_email":"CLIENT_EMAIL(STRING)"
                }
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
                "short_url":"SHORT_URL(STRING)",
                "original_url":"ORIGINAL_URL(STRING)",
                "expiration_time":"EXPIRATION_TIME(STRING)",
                "isActivated":"IS_ACTIVATED(BOOLEAN)",
                "group_id":"GROUP_ID(STRING)"
            }
            ```
    * Response:
        * if the short URL is created successfully:

            * HTTP code: 201
            * BODY:
                ```json
                {
                    "short_url":"SHORT_URL(STRING)",
                    "original_url":"ORIGINAL_URL(STRING)",
                    "expiration_time":"EXPIRATION_TIME(STRING)",
                    "isDeleted":"IS_DELETED(BOOLEAN)",
                    "isActivated":"IS_ACTIVATED(BOOLEAN)",
                    "group_id":"GROUP_ID(STRING)",
                    "creator_id":"CLIENT_ID(STRING)"
                }
                ```
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
                "company_name":"COMPANY_NAME(STRING)",
            }
            ```
    * Response:
        * if the company is created successfully:
            * HTTP code: 201
            * BODY:
                ```json
                {
                    "company_id":"COMPANY_ID(STRING)",
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

* POST /api/{company_id}/groups:
    * Description: Create a new group
    * Request:
        * Header: Authorization Bearer jwt string
        * Body:
            ```json
            {
                "group_name":"GROUP_NAME(STRING)",
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
