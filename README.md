# EA REST API

## Hlavní entity

* **Country**: stát (unikátní jméno)
* **Happiness**: data o štěstí pro stát a rok (odkaz na Country)
* **Prediction**: predikce štěstí pro stát a rok (odkaz na Country)

---

## Endpointy

### Základní REST endpointy

* `GET /countries` – seznam států

* `POST /countries` – vytvoření státu

* `GET /countries/{id}` – detail státu

* `PUT /countries/{id}` – aktualizace státu

* `DELETE /countries/{id}` – smazání státu

* `GET /happiness` – seznam happiness záznamů

* `POST /happiness` – vytvoření happiness

* `GET /happiness/{id}` – detail happiness záznamu

* `PUT /happiness/{id}` – aktualizace happiness

* `DELETE /happiness/{id}` – smazání happiness

* `GET /predictions` – seznam predikcí

* `POST /predictions` – vytvoření predikce

* `GET /predictions/{id}` – detail predikce

* `PUT /predictions/{id}` – aktualizace predikce

* `DELETE /predictions/{id}` – smazání predikce

* `POST /predictions/predict` – predikce na základě dat

### Výpočetní/analytické endpointy (`/calculations`)

* `GET /calculations`
  Vrací seznam výsledků všech předdefinovaných výpočtů (analytik).
* `GET /calculations/{type}`
  Vrací výsledek jednoho konkrétního výpočtu (např. correlation, averageScorePerYear atd.).
* `GET /calculations/score-trend/{country}`
  Vrací trend skóre pro zadanou zemi v čase (`{country}` je jméno státu; odpověď je mapování rok → skóre).
* `GET /calculations/extremes/{year}`
  Vrací státy s nejvyšším a nejnižším skóre pro zadaný rok (`{year}`).
* `GET /calculations/score-decomposition/{country}/{year}`
  Vrací rozklad výsledného skóre na dílčí složky pro zadaný stát a rok.

**Autorizace:** Bearer token přes Keycloak (`admin`/`user` role).

---

## Jak projekt spustit

1. **Zapni Docker Desktop** (pokud není aktivní).
2. **Naklonuj projekt** a otevři ho v **IntelliJ IDEA**.
3. **Spusť Docker kontejnery:**

   * V kořenovém adresáři spusť `start.bat` 
   
4. **Ověř, že databáze běží na portu `5433`** (z hostu, port v kontejneru je `5432`):

   * Připojení:

     * **Host:** `localhost`
     * **Port:** `5433`
     * **Uživatel:** `postgres`
     * **Heslo:** `123`
5. **Spusť hlavní aplikaci** (`Spring Boot Application`).
6. **Hotovo!**

---

## Dokumentace (Swagger UI)

Po spuštění aplikace najdeš kompletní OpenAPI/Swagger dokumentaci zde:

* [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)
  *(nebo případně [http://localhost:8090/swagger-ui/](http://localhost:8090/swagger-ui/) podle SpringDoc verze)*

* **OpenAPI JSON:**
  [http://localhost:8090/v3/api-docs](http://localhost:8090/v3/api-docs)

---

## Databáze

* **PostgreSQL** (běží jako kontejner přes `docker-compose`)
* Data jsou perzistentní ve svazku `postgres-data` (viz `docker-compose.yml`)
* Defaultní nastavení:

  * **User:** postgres
  * **Password:** 123
  * **Port:** 5433 (z hosta)

---

## Autentizace

* **Keycloak** je nasazen jako Docker kontejner na portu `8091` (`localhost:8091`)
* Realm: `ea`
* Uživatelé: `admin` heslo: `admin` , `user` heslo: `user` 
* Pro každý endpoint je potřeba Bearer token, který získáš z Keycloaku

---

## Testování

### Jak spustit všechny testy v IntelliJ IDEA

1. **Otevři projekt v IntelliJ IDEA**.
2. V levém panelu otevři složku `src/test/java`.
3. Klikni pravým tlačítkem na složku `java` (nebo přímo na složku s doménou projektu, např. `cz.mendelu.ea`).
4. Z nabídky vyber **Run 'All Tests'** (nebo "Spustit všechny testy").

   * Alternativně můžeš kliknout na zelenou šipku vedle složky a zvolit "Run Tests in...".
5. IntelliJ automaticky spustí **všechny unit, integrační i výkonnostní testy** v celém projektu.
6. Výsledky najdeš v dolním panelu "Run" / "Test Results".

### Alternativa: spuštění testů přes terminál

* **Unix/MacOS:**

  ```sh
  ./gradlew test
  ```
* **Windows:**

  ```bat
  gradlew.bat test
  ```

---
