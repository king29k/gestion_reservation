# Système de Gestion des Réservations de Matériel Scientifique – API REST

## Description
Le **Système de Gestion des Réservations de Matériel Scientifique – API REST** est une application backend conçue pour les laboratoires afin de gérer efficacement leurs équipements scientifiques. Cette API permet aux chercheurs de réserver du matériel, de suivre son état, et aux gestionnaires d’administrer les équipements, de planifier les maintenances et de superviser les réservations.

L'application est un backend **100% RESTful** basé sur **Spring Boot**, sécurisé avec **JWT**, utilisant **MySQL** pour le stockage des données en production et **H2** pour les tests. Elle est documentée avec **Swagger UI** pour faciliter l’exploration et le test des endpoints.

## Fonctionnalités principales
- **Authentification** : Inscription et connexion sécurisées avec JWT, avec rôles (`CHERCHEUR`, `GESTIONNAIRE`).
- **Gestion des équipements** : Ajout, mise à jour, suppression, et filtrage des équipements par catégorie, état ou localisation.
- **Réservations** : Réservation d’équipements pour des périodes définies, avec gestion des conflits de disponibilité.
- **Maintenance** : Planification et suivi des interventions de maintenance, avec historique.
- **Statistiques** : Tableau de bord avec des métriques sur les réservations et les maintenances (implémentation partielle).
- **Documentation API** : Swagger UI pour explorer et tester les endpoints REST.

## Architecture
- **Framework** : Spring Boot 3.2.0
- **API REST** : Gestion des utilisateurs, équipements, réservations, et maintenances via `/api/auth/*` et `/api/lab/*`.
- **Sécurité** : Spring Security avec JWT pour l’authentification et l’autorisation.
- **Base de données** :
  - **MySQL** : Pour la production.
  - **H2** : Base de données en mémoire pour les tests.
- **Documentation** : Springdoc OpenAPI pour générer la documentation API, accessible via `/swagger-ui.html`.

## Prérequis
- **Java** : JDK 17 (recommandé pour stabilité) ou JDK 21 (expérimental, peut causer des erreurs avec certaines dépendances).
- **Maven** : 3.8.0 ou supérieur.
- **MySQL** : 8.0 ou supérieur.
- **IDE** : IntelliJ IDEA (recommandé), Eclipse, ou tout autre IDE compatible.
- **Navigateur** : Chrome, Firefox, Brave, ou tout autre navigateur moderne pour accéder à Swagger UI.

## Installation
### 1. Cloner le dépôt
```bash
git clonehttps://github.com/king29k/lab_equip_reservation.git
cd lab_equip_reservation
```

### 2. Configurer Java
- **Recommandé : Utiliser Java 17** (LTS, stable) :
  - Téléchargez JDK 17 depuis [Adoptium](https://adoptium.net) (ex. Temurin 17.0.10) ou [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive.html).
  - Configurez `JAVA_HOME` :
    ```bash
    export JAVA_HOME=/path/to/jdk-17
    export PATH=$JAVA_HOME/bin:$PATH
    ```
  - Vérifiez :
    ```bash
    java -version
    # Attendu : openjdk 17.0.10
    ```
  - Mettez à jour `pom.xml` :
    ```xml
    <properties>
        <java.version>17</java.version>
    </properties>
    ```
- **Optionnel : Utiliser Java 21** (expérimental) :
  - Si vous utilisez Java 21, notez que des problèmes de compatibilité avec certaines dépendances (ex. Springdoc, Lombok) peuvent survenir. Si des erreurs comme `java.lang.ExceptionInInitializerError` apparaissent, passez à Java 17.

### 3. Configurer la base de données
1. Créez une base de données MySQL :
   ```sql
   CREATE DATABASE reservation_db;
   ```
2. Mettez à jour les paramètres de connexion dans `src/main/resources/application.properties` :
   ```properties
   # Base de données MySQL (production)
   spring.datasource.url=jdbc:mysql://localhost:3306/scientific_equipment
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

   # Base de données H2 (tests)
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

   # Secret JWT
   jwt.secret=your_jwt_secret_key

   # Logging (facultatif, pour le débogage)
   logging.level.org.springframework=INFO
   logging.level.com.example.scientific=DEBUG
   ```

### 4. Compiler et exécuter l’application
```bash
mvn clean install -U
mvn spring-boot:run
```
L’application sera disponible à `http://localhost:8080`.

### 5. Accéder à l’API
- **Swagger UI** : Accédez à `http://localhost:8080/swagger-ui.html` pour explorer et tester les endpoints REST.
- **Authentification** :
  - Inscrivez-vous via `POST /api/auth/register`.
  - Connectez-vous via `POST /api/auth/login` pour obtenir un token JWT.
  - Utilisez ce token dans Swagger via le bouton "Authorize" pour tester les endpoints sécurisés.

## Structure du projet
```
scientific-equipment-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── scientific/
│   │   │               ├── config/         # Configurations (SecurityConfig, SwaggerConfig)
│   │   │               ├── controller/     # Contrôleurs REST (AuthController, EquipmentController, etc.)
│   │   │               ├── dto/            # Objets de transfert (EquipmentDTO, ReservationDTO, etc.)
│   │   │               ├── model/          # Entités JPA (User, Equipment, Reservation, Maintenance, Role)
│   │   │               ├── repository/     # Interfaces JPA (UserRepository, EquipmentRepository, etc.)
│   │   │               ├── security/       # Configuration de sécurité (JwtUtil, UserDetailsServiceImpl)
│   │   │               ├── service/        # Logique métier (AuthService, EquipmentService, etc.)
│   │   │               └── ScientificEquipmentApiApplication.java # Classe principale
│   │   └── resources/
│   │       └── application.properties     # Configuration de l’application
│   ├── test/                              # Tests unitaires et d’intégration
│   │   ├── java/
│   │   │   └── com/example/scientific/
│   │   │       ├── controller/            # Tests des contrôleurs
│   │   │       ├── service/               # Tests des services
│   │   │       └── repository/            # Tests des repositories
│   │   └── resources/
│   │       └── application-test.properties # Configuration pour les tests
├── target/                                # Fichiers compilés
├── .gitignore                             # Fichiers ignorés par Git
├── pom.xml                                # Dépendances Maven
└── README.md                              # Documentation du projet
```

## Endpoints principaux
| Méthode | Endpoint                          | Description                              | Rôle requis       |
|---------|-----------------------------------|------------------------------------------|-------------------|
| POST    | `/api/auth/register`             | Inscription d’un nouvel utilisateur      | Aucun             |
| POST    | `/api/auth/login`                | Connexion et génération de JWT           | Aucun             |
| GET     | `/api/lab/equipments`            | Liste des équipements                    | CHERCHEUR, GESTIONNAIRE |
| POST    | `/api/lab/equipments`            | Ajout d’un équipement                    | GESTIONNAIRE      |
| PUT     | `/api/lab/equipments/{id}`       | Mise à jour d’un équipement              | GESTIONNAIRE      |
| DELETE  | `/api/lab/equipments/{id}`       | Suppression d’un équipement              | GESTIONNAIRE      |
| POST    | `/api/lab/reservations`          | Création d’une réservation               | CHERCHEUR         |
| GET     | `/api/lab/reservations`          | Liste des réservations                   | CHERCHEUR, GESTIONNAIRE |
| GET     | `/api/lab/maintenances`          | Liste des maintenances                   | GESTIONNAIRE      |
| POST    | `/api/lab/maintenances`          | Planification d’une maintenance          | GESTIONNAIRE      |

## Authentification
### 🔑 Inscription
```http
POST /api/auth/register
Content-Type: application/json
```
**Body** :
```json
{
  "username": "jean",
  "email": "jean@example.com",
  "password": "password123",
  "role": "CHERCHEUR"
}
```

### 🔐 Connexion
```http
POST /api/auth/login
Content-Type: application/json
```
**Body** :
```json
{
  "username": "jean",
  "password": "password123"
}
```
**Réponse** :
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
Collez ce token dans Swagger via le bouton "Authorize" pour tester les endpoints sécurisés.

## Sécurité
- **Authentification** : Basée sur JWT, avec rôles `CHERCHEUR` et `GESTIONNAIRE`.
- **Autorisation** : Les endpoints sensibles sont protégés via `@PreAuthorize` (Spring Security).
- **Mot de passe** : Hashé avec BCrypt (fourni par Spring Security).

## Tests
- **Tests unitaires** : Utilisation de JUnit et Mockito pour tester les services et repositories (ex. `AuthServiceTest`, `EquipmentServiceTest`).
- **Tests d’intégration** : Utilisation de Spring Boot Test avec H2 pour tester les endpoints REST.
  Pour exécuter les tests :
  ```bash
  mvn test
  ```

## Dépannage
### Erreur `java.lang.ExceptionInInitializerError` avec `com.sun.tools.javac.code.TypeTag::UNKNOWN`
- **Cause** : Problème de compatibilité entre Java 21/24 et certaines dépendances (ex. Springdoc OpenAPI, Lombok).
- **Solution recommandée** :
  1. Passez à **Java 17** (LTS, stable) comme indiqué dans la section *Prérequis*.
  2. Si vous utilisez Java 21 :
     - **Désactivez temporairement Lombok** :
       - Commentez la dépendance Lombok dans `pom.xml` :
         ```xml
         <!--
         <dependency>
             <groupId>org.projectlombok</groupId>
             <artifactId>lombok</artifactId>
             <version>1.18.34</version>
             <optional>true</optional>
         </dependency>
         -->
         ```
       - Ajoutez des getters/setters explicites dans les DTOs (ex. `EquipmentDTO`).
     - **Désactivez Springdoc** :
       - Commentez la dépendance Springdoc :
         ```xml
         <!--
         <dependency>
             <groupId>org.springdoc</groupId>
             <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
             <version>2.2.0</version>
         </dependency>
         -->
         ```
       - Supprimez `SwaggerConfig.java` et les annotations Swagger dans les contrôleurs.
     - Recompilez :
       ```bash
       mvn clean install -U
       mvn spring-boot:run
       ```
- **Débogage** :
  - Activez les logs détaillés dans `application.properties` :
    ```properties
    logging.level.org.springframework=DEBUG
    logging.level.org.springdoc=DEBUG
    logging.level.com.example.scientific=DEBUG
    ```
  - Utilisez Maven en mode debug :
    ```bash
    mvn clean install -X > build.log
    ```

## Améliorations futures
- Intégration de notifications en temps réel (ex. via WebSocket).
- Ajout de notifications par email avec Spring Mail.
- Mise en cache des données fréquemment accédées (Spring Cache).
- Ajout de tests supplémentaires pour couvrir les cas limites.
- Optimisation des performances pour les recherches et filtrages complexes.


## Contact
Pour toute question ou suggestion, contactez [kingrobot790@gmail.com](mailto:kingrobot790@gmail.com).
 
