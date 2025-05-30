# EA REST API

## Hlavní entity
- **Country**: stát (unikátní jméno)
- **Happiness**: data o štěstí pro stát a rok (odkaz na Country)
- **Prediction**: predikce štěstí pro stát a rok (odkaz na Country)

## Endpointy

- `GET /countries` – seznam států
- `POST /countries` – vytvoření státu
- `GET /happiness` – seznam happiness záznamů
- `POST /happiness` – vytvoření happiness
- `GET /predictions` – seznam predikcí
- `POST /predictions` – vytvoření predikce

**Autorizace:** Bearer token přes Keycloak (admin/user role)

## Swagger
Po spuštění [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Databáze
- Postgres, kontejnerizováno v docker-compose

## CI/CD
- Testy: spustitelné jedním příkazem `./gradlew test` / `mvn test`
