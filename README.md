Aplikacja do zapisywania przychodów i wydatków
-

Stos technologiczny:
- Java 11
- Spring Boot
- PostgreSQL

Używane biblioteki:
- Liquibase
- Mockito
- JUnit 5
- AssertJ
- Swagger
- Lombok
- jjwt

Kroki potrzebne do odpalenia aplikacji:
- utworzenie bazy danych "budget_main_db" za pomocą PostgreSQL
- utworzenie bazy danych "budget_test_db" za pomocą PostgreSQL
- do łączenia się z główną bazą danych i bazą danych do testów
  wykorzystywany jest domyślny profil o nazwie "postgres" i haśle "password"
  którego w razie potrzeby można zmienić w
  src/main/resources/application.properties
  src/test/java/resources/application.properties

Użytkowanie aplikacji:
- aplikacja odpala się lokalnie na porcie 8080
- do łatwiejszego korzystania z aplikacji wykorzystujemy swagger UI dostępy pod
  http://localhost:8080/swagger-ui.html
- aplikacja nie inicjalizuje bazy danych początkowymi wartościami, dlatego trzeba
  utworzyć nowego użytkownika i zalogować się za pomocą Authentication Controller
- aby się uwierzytelnić za pomocą Swagger UI trzeba wpisać
  "Bearer " + jwtToken który jest zwracany po zalogowaniu się.
