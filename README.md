# SecureStorageLabJava

Projet Android développé en Java illustrant la mise en œuvre des différents mécanismes de stockage de données locales, avec une emphase sur la sécurité et l'isolation des informations sensibles.

## Objectifs du projet

Ce laboratoire permet de manipuler les API de persistance Android en respectant les standards de sécurité modernes (chiffrement via Keystore, isolation MODE_PRIVATE, gestion du cycle de vie des secrets).

## Preuves de fonctionnement

### 1. Stockage Sécurisé et Préférences
Démonstration de la restauration des préférences utilisateur et de la détection d'un token chiffré (longueur affichée, contenu masqué).

### 2. Gestion des Fichiers (JSON & Texte)
Validation de la lecture d'un fichier JSON contenant une liste d'objets et d'une note en UTF-8.

### 3. Démonstration Vidéo
Voici la démonstration complète des fonctionnalités de l'application :

 [Voir la vidéo](./video.mp4)


## Étapes de réalisation

### 1. Configuration et Dépendances
SDK minimum 24 et intégration de `androidx.security:security-crypto`.

### 2. SharedPreferences
Persistance du Nom, de la Langue et du Thème en mode privé.

### 3. EncryptedSharedPreferences
Chiffrement AES-256 du token API avec expiration automatique (TTL 1h).

### 4. Stockage Interne
Fichiers `students.json` et `note.txt` isolés via `MODE_PRIVATE`.

### 5. Gestion du Cache
Stockage temporaire dans `cacheDir` avec purge manuelle.

## Bonnes Pratiques Appliquées

- Isolation stricte (Mode privé)
- Zéro donnée sensible dans les logs système
- Masquage des saisies sensibles (`textPassword`)
- Nettoyage intégral possible via l'interface

## Spécifications Techniques

- Langage : Java  
- SDK Minimum : 24  
- Bibliothèque : androidx.security:security-crypto
