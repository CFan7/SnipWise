# API Endpoints
## Note
The document tries to introduce the API endpoints of the backend. The document is not complete the API endpoints may change in the future. To get the latest information about the API endpoints, please refer to the source code itself.

## Endpoints
* POST /api/clients:
    * Description: Create a new client
    * Request: 
        * Body: 
            * client_name: string
            * client_email: string
            * passwd: string
    * Response:
        * if the client is created successfully:
            http code 201 created
            * JSON: 
                * client_id: string
                * client_name: string
                * client_email: string
        * if the client is not created successfully:
            http code 409 conflict

* POST /api/clients/login:
    * Description: Login a client
    * Request: 
        * Body: 
            * client_email: string
            * passwd: string
    * Response:
        * if the client is logged in successfully:
            http code 200 ok
            * JSON: 
                * client_id: string
                * jwt: string
                * expiration_time: string
        * if the client is not logged in successfully:
            http code 401 unauthorized

* GET /api/clients/{client_id}:
    * Description: Get client information. Currently only the client himself/herself can access this endpoint.
    * Request:
        * Authorization: Bearer jwt string
    * Response:
        * if the jwt is valid:
            http code 200 ok
            * JSON: 
                * client_id: string
                * client_name: string
                * client_email: string
        * if the jwt is invalid or the client_id is not the same as the client_id in the jwt:
            http code 401 unauthorized

* GET /s/{short_url_suffix}:
    * Description: Redirect to the original URL
    * Request:
        * None
    * Response:
        * if the short URL exists:
            http code 302 redirection to the original URL
        * if the short URL does not exist:
            http code 302 redirection to "https://www.example.com"

* POST /api/url:
    * Description: Create a new short URL
    * Request:
        * Authorization: Bearer jwt string
        * Body:
            * short_url: string
            * original_url: string
            * expiration_time: string
            * isActivated: boolean
            * group_id: string
    * Response:
        * if the short URL is created successfully:
            http code 201 created
            * JSON:
                * short_url: string
                * original_url: string
                * expiration_time: string
                * isDeleted: boolean
                * isActivated: boolean
                * group_id: string
                * client_id: string
        * if the short URL is not created successfully:
            http code 400 bad request
        * if the jwt is invalid:
            http code 401 unauthorized
        
* DELETE /api/url?short_url={short_url}:
    * Description: Delete a short URL
    * Request:
        * Authorization: Bearer jwt string
    * Response:
        * if the short URL is deleted successfully:
            http code 200 ok
        * if the short URL is not deleted successfully:
            http code 400 bad request
        * if the jwt is invalid:
            http code 401 unauthorized





