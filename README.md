# SecureStorageLabJava

Projet Android développé en Java illustrant la mise en œuvre des différents mécanismes de stockage de données locales, avec une emphase sur la sécurité et l'isolation des informations sensibles.

## Objectifs du projet

Ce laboratoire permet de manipuler les API de persistance Android en respectant les standards de sécurité modernes (chiffrement via Keystore, isolation MODE_PRIVATE, gestion du cycle de vie des secrets).

## Résultats et Preuves de fonctionnement

L'application a été testée avec succès sur un émulateur API 30. Voici les points validés :

1. **Persistance des Préférences** : Le nom "Wissal", la langue "fr" et le thème sombre sont correctement sauvegardés et restaurés après redémarrage.
2. **Sécurité du Token** : Le jeton API est stocké de manière chiffrée. L'interface affiche uniquement le statut "Présent" et la longueur (22 caractères) pour éviter toute fuite visuelle.
3. **Gestion du Cycle de Vie** : Le token inclut un horodatage et expire automatiquement après une heure, garantissant une sécurité accrue.
4. **Stockage de Fichiers** : Les fichiers `students.json` et `note.txt` sont générés en stockage interne privé (`/data/data/com.example.securestoragelabjava/files/`).
5. **Hygiène du Stockage** : La fonction "Effacer" supprime instantanément toutes les préférences, les fichiers et purge le cache.

## Étapes de réalisation

### 1. Configuration et Dépendances
Initialisation du projet avec un SDK minimum 24 et ajout de la bibliothèque AndroidX Security Crypto pour le support du chiffrement matériel.

### 2. Gestion des Préférences (SharedPreferences)
Implémentation d'un gestionnaire pour les données non sensibles. Utilisation de la méthode `apply()` pour des opérations asynchrones.

### 3. Sécurisation des Secrets (EncryptedSharedPreferences)
Utilisation du framework Security Crypto pour chiffrer les jetons d'accès. Les clés sont protégées par le Android Keystore.

### 4. Stockage Interne (Fichiers Texte et JSON)
Lecture/Écriture en UTF-8 pour le texte brut et sérialisation JSON pour les listes d'objets.

### 5. Gestion du Cache
Utilisation du répertoire de cache pour les données temporaires avec mécanisme de purge.

## Bonnes Pratiques Appliquées

- Isolation des données : Utilisation systématique du mode privé.
- Confidentialité des logs : Zéro secret exposé dans Logcat.
- Masquage UI : Protection visuelle via `textPassword`.

## Spécifications Techniques

- Langage : Java
- SDK Minimum : 24
- Bibliothèque principale : androidx.security:security-crypto
