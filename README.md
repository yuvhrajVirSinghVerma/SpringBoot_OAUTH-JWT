# SpringBoot_OAUTH-JWT

### Steps for getting  client id and client secret 
1. [ ] ##### Go to [console](https://console.cloud.google.com/auth/clients) create an OAuth2 client, specify redirect uri there 
2. [ ] ##### Go to [DataAccess](https://console.cloud.google.com/auth/scopes) to set scopes for your app

### Flow of OAUTH2
#### **FILTERS AND COMPONENTS INCLUDED**
1. ##### **DefaultLoginPageGeneratingFilter** -> generates login page where we get button for google signin
2. ##### **OAuth2AuthorizationRequestRedirectFilter** -> redirects the user to the OAuth2 provider’s login page -> /oauth2/authorization/google will redirect to google signin page
3. ##### **OAuth2LoginAuthenticationFilter** -> handles the callback from the OAuth2 provider after the user logs in , it creates Authentication obj from access token and id_token(oidc) with help of **OidcAuthorizationCodeAuthenticationProvider** received and stores in security context holder -> login/oauth2/code/google?code=example  this is redirect uri we configured in console
4. ##### **CustomOAuthService** -> our custom implementation of OidcUserService is called from **OidcAuthorizationCodeAuthenticationProvider** , here we save user info in db
5. ##### **CustomSuccessHandler** -> our custom implementation of SimpleUrlAuthenticationSuccessHandler is called as we continue on filter chain , here we generate the jwt

#### Generally Front end hits this url -> localhost:8080/oauth2/authorization/google  and  /login/oauth2/code/google these url are spring managed URLs so that spring oauth flow can intercepts it and build a googles authorization url https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?
#### Googles authorization url is populated by client cide, secret, scope in query param and on response we get a login page this is all done by **OAuth2AuthorizationRequestRedirectFilter**
[//]: # (FLOW CHART FROM CHAT GPT)

[//]: # ()
[//]: # (+--------------------+                       +--------------------------+)

[//]: # (|   React Frontend   |                       | Spring Boot Backend      |)

[//]: # (+--------------------+                       +--------------------------+)

[//]: # (|                                                  |)

[//]: # (| 1. User visits /login                            |)

[//]: # (|------------------------------------------------->|)

[//]: # (|                                                  |)

[//]: # (| 2. User clicks "Login with Google"               |)

[//]: # (|                                                  |)

[//]: # (| 3. Redirect to /oauth2/authorization/google      |)

[//]: # (|------------------------------------------------->|)

[//]: # (|                                                  |)

[//]: # (|        [OAuth2AuthorizationRequestRedirectFilter]|)

[//]: # (|            - Builds auth URL for Google          |)

[//]: # (|            - Redirects user to Google            |)

[//]: # (|<-------------------------------------------------|)

[//]: # (|                                                  |)

[//]: # (| 4. Google Login Page &#40;User authenticates&#41;        |)

[//]: # (|-------------------------------------------------> &#40;Google&#41;)

[//]: # (|                                                  |)

[//]: # (| 5. Google redirects back to:                     |)

[//]: # (|    /login/oauth2/code/google?code=abc&state=xyz  |)

[//]: # (|<-------------------------------------------------|)

[//]: # (|                                                  |)

[//]: # (|     [OAuth2LoginAuthenticationFilter]            |)

[//]: # (|     - Intercepts callback URL                    |)

[//]: # (|     - Exchanges code for token                   |)

[//]: # (|     - Calls user info endpoint                   |)

[//]: # (|     - Delegates to one of:                       |)

[//]: # (|         - DefaultOAuth2UserService               |)

[//]: # (|         - OidcUserService &#40;for OpenID Connect&#41;   |)

[//]: # (|         - CustomOAuth2UserService &#40;optional&#41;     |)

[//]: # (|                                                  |)

[//]: # (|     [AuthenticationSuccessHandler]               |)

[//]: # (|     - Creates and signs JWT                      |)

[//]: # (|     - Optionally stores user in DB               |)

[//]: # (|     - Redirects to frontend with token           |)

[//]: # (|       e.g., /oauth2/redirect?token=xyz           |)

[//]: # (|------------------------------------------------->|)

[//]: # (|                                                  |)

[//]: # (| 6. React receives token                          |)

[//]: # (|    - Stores in localStorage or cookie            |)

[//]: # (|                                                  |)

[//]: # (| 7. React makes secure API call:                  |)

[//]: # (|    GET /api/user                                 |)

[//]: # (|    Authorization: Bearer <JWT>                   |)

[//]: # (|------------------------------------------------->|)

[//]: # (|                                                  |)

[//]: # (|     [JwtAuthenticationFilter] &#40;Custom&#41;           |)

[//]: # (|     - Extracts token from header                 |)

[//]: # (|     - Validates and parses JWT                   |)

[//]: # (|     - Sets Authentication in SecurityContext     |)

[//]: # (|                                                  |)

[//]: # (|     [SecurityContextPersistenceFilter]           |)

[//]: # (|     - Applies authentication to context          |)

[//]: # (|                                                  |)

[//]: # (|     [Controller]                                 |)

[//]: # (|     - Handles request &#40;e.g., returns user data&#41;  |)

[//]: # (|<-------------------------------------------------|)
