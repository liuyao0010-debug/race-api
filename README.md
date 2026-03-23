# API REST — Gestion des inscriptions à une course

## 📌 Description

Ce projet consiste à développer une API REST avec Spring Boot permettant de gérer :

* les **coureurs (runners)**
* les **courses (races)**
* les **inscriptions (registrations)**

Chaque coureur peut participer à plusieurs courses, et chaque course peut accueillir plusieurs coureurs.

Les données sont stockées dans une base **PostgreSQL**.

---

## 🛠️ Technologies utilisées

* Java 25
* Spring Boot
* Spring Web
* Spring Data JPA
* Flyway
* PostgreSQL
* Docker
* Adminer

---

## 🚀 Lancement du projet

### 1. Démarrer la base de données

```bash
docker compose up -d
```

---

### 2. Accéder à Adminer

URL :

```
http://localhost:8081
```

Paramètres :

* System : PostgreSQL
* Server : race_postgres
* Username : race
* Password : race
* Database : race_db

---

### 3. Lancer l’application

```bash
mvn spring-boot:run
```

L’API est disponible sur :

```
http://localhost:8080
```

---

## 🗂️ Modèle de données

### Runner

* id
* firstName
* lastName
* email
* age

---

### Race

* id
* name
* date
* location
* maxParticipants

---

### Registration

* id
* runnerId
* raceId
* registrationDate

---

## 📡 Endpoints implémentés

### 🧍 Runners

* **GET /runners** → Lister les coureurs
* **GET /runners/{id}** → Récupérer un coureur
* **POST /runners** → Créer un coureur
* **PUT /runners/{id}** → Modifier un coureur
* **DELETE /runners/{id}** → Supprimer un coureur
* **GET /runners/{runnerId}/races** → Lister les courses d’un coureur

---

### 🏃 Races

* **GET /races** → Lister les courses
* **GET /races/{id}** → Récupérer une course
* **POST /races** → Créer une course
* **GET /races/{raceId}/participants/count** → Nombre de participants

---

### 📝 Registrations

* **POST /races/{raceId}/registrations** → Inscrire un coureur
* **GET /races/{raceId}/registrations** → Lister les participants

---

## ⚙️ Règles métier implémentées

* ❌ Un coureur ne peut pas être inscrit deux fois à la même course
  → retourne **409 Conflict**

* ❌ Une course ne peut pas dépasser le nombre maximum de participants
  → retourne **409 Conflict**

* ❌ Email invalide (ne contient pas @)
  → retourne **400 Bad Request**

* ❌ Runner ou Race inexistant
  → retourne **404 Not Found**

---

## 🎯 Bonus — Filtrage par localisation

Ajout d’un filtre sur le endpoint des courses :

```
GET /races?location=Paris
```

### Fonctionnement

* Si le paramètre `location` est fourni → retourne uniquement les courses dans cette ville
* Sinon → retourne toutes les courses

### Exemple

```
GET /races?location=Paris
```

Retour :

```json
[
  {
    "id": 1,
    "name": "Semi-marathon de Paris",
    "location": "Paris"
  }
]
```

---

## ✅ Tests

Les endpoints ont été testés avec Postman :

* Création de runners et races
* Inscription à une course
* Vérification des erreurs (404, 409, 400)
* Vérification du nombre de participants

---

## 📁 Structure du projet

```bash
src/main/java/com/takima/race/
├── runner/
├── race/
├── registration/
```

Chaque module contient :

* controller
* service
* repository
* entity

---

## 👨‍💻 Auteur

Projet réalisé dans le cadre d’un TP de développement API REST avec Spring Boot.
