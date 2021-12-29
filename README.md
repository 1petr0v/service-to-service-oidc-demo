# Service-to-service Spring 5 + OAuth2 (OIDC) integration

## Architecture

<img src="./doc/service-to-service-oidc-diagram.png" width="1000">

## Demo access control model

<img src="./doc/access-model.png" width="1000">

## Running

Requirements:
- Java 17 (can be downgraded to 8, if needed with minor code change) to run Client and Resource services
- Docker (optional) to launch a default intance of Keycloak
- cURL (or it's equivalent)
- ~~Keyboard and mouse~~

1. Start [Keycloak](https://www.keycloak.org/) in docker: `docker run -p 8080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak:16.1.0`. If you don't want to use docker, feel free to launch stand-alone instance.
2. Configure Keycloak realm, clients and scopes as I describe in the article (coming soon!).
3. Update clients and their secrets for each service at respective `application.yaml` files.
4. Run *all 3 services*. Example: `cd {PROJECT_ROOT_FOLDER}/service1 && gradle bootRun`.

## Testing

### Integ testing

Assuming, all 3 services and OIDC provider running.


```
cd {PROJECT_ROOT_FOLDER}/integ-test
python3 integ-test.py
```

You should see something like this:

<img src="./doc/test-results.png" width="1000">

### Manual testing

Obtain bearer token for **client1**: 

```
curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=client_credentials&client_id=service1&client_secret=28zlyEXPHBtOOg1SOeA17xCnbCjeaFrG' "http://localhost:8080/auth/realms/demo/protocol/openid-connect/token"
```

Based on the access control model, given token should give access to **Service2.Resource2** and **Service3.Resource2**: 

```
curl -H "Authorization: Bearer {access_token_from_response}" http://localhost:10002/resource2
```
