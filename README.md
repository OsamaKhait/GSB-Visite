# GSB-Visite

Application mobile Android de gestion des visites médicales pour les visiteurs du laboratoire fictif GSB (Galaxy Swiss Bourdin).

## 📱 Présentation

GSB-Visite permet aux visiteurs médicaux de :
- Se connecter via une API sécurisée (authentification avec token)
- Consulter la liste des praticiens
- Enregistrer un rapport de visite (date, bilan, motif)
- Voir l’historique de leurs visites

Cette application a été développée dans le cadre du BTS SIO SLAM 2025 (2e semestre).

## 🔧 Technologies utilisées

- **Android Studio**
- **Java**
- **XML** (interfaces utilisateur)
- **Retrofit** (API REST)
- **JSON** (échange de données)
- **API GSB** (serveur distant, non inclus)

## 🧱 Architecture

L’application est structurée selon le modèle **MVC** :
- **Modèle** : classes Java représentant les entités (Visiteur, Praticien, RapportVisite)
- **Vue** : interfaces XML des écrans (connexion, liste praticiens, création rapport, historique)
- **Contrôleur** : activités Android (LoginActivity, PraticiensActivity, RapportActivity...)

## 📂 Structure du projet

```
GSB-Visite/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gsbvisite/
│   │   │   │   ├── activity/         # Écrans principaux (login, praticiens, rapports)
│   │   │   │   ├── model/            # Entités Visiteur, Praticien, RapportVisite
│   │   │   │   ├── adapter/          # Adaptateurs RecyclerView
│   │   │   │   ├── api/              # Interface Retrofit pour les endpoints API
│   │   │   ├── res/layout/           # Fichiers XML des interfaces
│   │   │   ├── AndroidManifest.xml
│   │   └── ...
├── README.md
```

## 🔐 Authentification

Le visiteur se connecte avec son login et mot de passe. Un **token** est récupéré depuis l’API et stocké localement pour les requêtes suivantes.  
Toutes les requêtes nécessitent ce token dans l’en-tête HTTP.

## 🔄 Fonctionnalités principales

- 🔓 Connexion sécurisée avec session persistante
- 👨‍⚕️ Consultation de la liste des praticiens
- 📝 Création de rapport de visite
- 📅 Consultation de l’historique des visites par mois ou par praticien
- 🗑️ Suppression/modification possible d’un rapport non validé

## ✅ Pré-requis

- Android Studio installé
- JDK 11 ou supérieur
- Une instance de l’API REST GSB configurée et accessible

## 🚀 Lancer le projet

1. Cloner ou décompresser le projet
2. Ouvrir dans Android Studio
3. Connecter un appareil ou utiliser un émulateur
4. Cliquer sur **Run** ▶️

## 📸 Captures d’écran

> *(À insérer : écran de connexion, liste praticiens, formulaire de rapport, historique...)*

## 👨‍💻 Auteur

Réalisé par **Osama KHAIT** – BTS SIO SLAM 2025  
Lycée Gabriel Fauré – Annecy

---

Projet pédagogique – Toute ressemblance avec une entreprise réelle est fortuite.
