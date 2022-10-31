## Example of a backend application with http4k

### Provided REST endpoints

This example application provides some basic REST endpoints by using the [http4](https://www.http4k.org/) framework:
* `POST /login`  
  Authenticates a user by email & password. The response will contain a JsonWebToken (JWT).
  * Authentication required: no
  * Authorization required: no
  * Request body example:   
   ```json
  {
     "email": "fritz@yahoo.com",
     "password": "fritz123"
  }
   ```
  * Existing users:
    * Email: "paula@example.com" Password: "paula123" Roles: none
    * Email: "fritz@example.com" Password: "fritz123" Roles: USER
    * Email: "heide@example.com" Password: "heide123" Roles: USER, ADMIN
* `GET /ping/anonymous`
    * Authentication required: no
    * Authorization required: no
* `GET /ping/user`  
   Requires an authorization header with the JWT from the `/login` as Bearer token. 
   * Authentication required: yes
   * Authorization required: USER or ADMIN
* `GET /ping/admin`  
  Requires an authorization header with the JWT from the `/login` as Bearer token.
    * Authentication required: yes
    * Authorization required: ADMIN

### Running the application

The Kotlin file `Main.kt` contains a `main()` function which will be starting the server by default on ports 8080 (see `config/.env.dev`). Make sure to add the program argument `dev` when running the application.
