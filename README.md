# Gestion des Congés

Ce projet est une application de gestion des congés d'employés d'une entreprise, développée en JavaFX et connectée à une base de données MySQL.

## Fonctionnalités

1. **Voir les demandes de congés** :
    - Affiche une liste de toutes les demandes de congés sous forme de tableau.

2. **Ajouter une demande de congé** :
    - Permet aux utilisateurs d'ajouter une nouvelle demande via un formulaire avec des menus déroulants dynamiques pour les employés et les types de congés.

3. **Modifier une demande de congé** :
    - Permet de mettre à jour les informations d'une demande existante.

4. **Supprimer une demande de congé** :
    - Supprime une demande de congé sélectionnée avec une confirmation.

## Prérequis

1. **Java** : JDK 11 ou version supérieure.
2. **JavaFX** : Bibliothèque JavaFX.
3. **MySQL** : Serveur MySQL avec une base de données configurée.
4. **IDE** : IntelliJ IDEA, Eclipse ou tout autre IDE compatible.

## Installation

### 1. Configurer la base de données

1. Créez une base de données MySQL avec le script fourni dans `vacation_manager.sql`.
2. Ajoutez les données de test incluses dans le script.

### 2. Configurer l'application

1. Clonez ce dépôt :
   ```bash
   git clone https://github.com/jaouad4/application-gestion-conges.git
   ```

2. Importez le projet dans votre IDE.
3. Assurez-vous que le chemin vers JavaFX est configuré dans les paramètres du projet.
4. Modifiez les informations de connexion à la base de données dans `DBConnection.java` si nécessaire :
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/vacation_manager";
   private static final String USER = "root";
   private static final String PASSWORD = "";
   ```

## Exécution

1. Lancez l'application en exécutant la classe `Main.java`.
2. L'interface utilisateur s'ouvre et affiche la liste des demandes de congés existantes.

## Structure du Projet

```plaintext
src/
|-- application/
    |-- Main.java                 // Point d'entrée de l'application
    |
    |-- dao/                     // Accès aux données
    |   |-- DBConnection.java
    |   |-- VacationRequestDAO.java
    |   |-- EmployeeDAO.java
    |   |-- VacationTypeDAO.java
    |
    |-- metier/                  // Logique métier
    |   |-- VacationRequestService.java
    |
    |-- model/                   // Modèles de données
    |   |-- VacationRequest.java
    |   |-- Employee.java
    |   |-- VacationType.java
    |
    |-- presentation/            // Interface utilisateur
        |-- VacationRequestController.java
        |-- vacation_request.fxml
```

## Fonctionnalités Techniques

### 1. **Connexion à la Base de Données**
La classe `DBConnection` gère la connexion à la base de données MySQL.

### 2. **Gestion des Demandes de Congés**
La classe `VacationRequestDAO` fournit les méthodes suivantes :
- `getAllRequests()` : Récupère toutes les demandes de congés.
- `addVacationRequest()` : Ajoute une nouvelle demande.
- `updateVacationRequest()` : Met à jour une demande existante.
- `deleteVacationRequest()` : Supprime une demande par ID.

### 3. **Logique Métier**
La classe `VacationRequestService` encapsule la logique métier, en intégrant les DAO pour les demandes, les employés et les types de congés.

### 4. **Interface Utilisateur**
- `VacationRequestController` gère les interactions utilisateur.
- `vacation_request.fxml` définit l'interface graphique avec JavaFX.

## Auteur
Ce projet a été réalisé dans le cadre d'apprentissage JavaFX et MySQL.

## Licence
Ce projet est sous licence MIT. Vous êtes libre de l'utiliser et de le modifier.
