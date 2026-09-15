# Poseidon

Application web sécurisée de back-office pour la gestion d'instruments financiers : CRUD complet sur 6 entités (Bid, CurvePoint, Rating, Rule, Trade, User) avec authentification et gestion des utilisateurs.

## Stack technique

Java 21 · Spring Boot · Spring Security · Spring Data JPA · Thymeleaf · MySQL · Bean Validation · Lombok · JUnit / Mockito · JaCoCo (couverture de tests)

## Lancer le projet

Prérequis : Java 21, Maven (ou le wrapper fourni), une base MySQL locale.

```bash
git clone https://github.com/doui445/poseidon.git
cd poseidon
# créer la base MySQL "poseidon" et adapter src/main/resources/application.properties si besoin
./mvnw spring-boot:run
```

L'application est ensuite disponible sur `http://localhost:8080` (page de connexion).

## Tests

```bash
./mvnw test
```

Rapport de couverture JaCoCo généré dans `target/site/jacoco/index.html`.
