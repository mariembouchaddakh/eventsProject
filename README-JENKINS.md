# Guide d'utilisation du Pipeline Jenkins CI/CD

## 📋 Prérequis

1. **Jenkins** installé et configuré
2. **Docker** et **Docker Compose** installés
3. **SonarQube** configuré et accessible
4. **Nexus** configuré (optionnel)
5. **DockerHub** compte créé

## 🔧 Configuration Jenkins

### 1. Plugins nécessaires
Installer les plugins suivants dans Jenkins :
- Pipeline
- Docker Pipeline
- SonarQube Scanner
- Git
- Maven Integration
- Email Extension (optionnel)

### 2. Credentials à configurer

Dans Jenkins → Credentials → Add Credentials :

1. **DockerHub** (ID: `dockerhub-credentials`)
   - Type: Username with password
   - Username: votre username DockerHub
   - Password: votre token DockerHub

2. **SonarQube Token** (ID: `sonar-token`)
   - Type: Secret text
   - Secret: votre token SonarQube

3. **Nexus** (ID: `nexus-releases`)
   - Type: Username with password
   - Username: votre username Nexus
   - Password: votre password Nexus

### 3. Configuration SonarQube

Dans Jenkins → Manage Jenkins → Configure System → SonarQube servers :
- Name: `SonarQube`
- Server URL: `http://votre-sonarqube:9000`
- Server authentication token: utiliser le credential `sonar-token`

## 🚀 Utilisation

### Créer un nouveau Pipeline

1. Dans Jenkins, créer un nouveau **Pipeline** job
2. Dans la configuration :
   - **Definition**: Pipeline script from SCM
   - **SCM**: Git
   - **Repository URL**: URL de votre repository Git
   - **Script Path**: `Jenkinsfile`

### Exécuter le Pipeline

Le pipeline s'exécute automatiquement lors d'un push sur la branche configurée, ou manuellement via "Build Now".

## 📊 Étapes du Pipeline

1. **Checkout** : Récupération du code depuis Git
2. **Compilation** : Compilation Maven
3. **Tests Unitaires** : Exécution des tests JUnit
4. **SonarQube** : Analyse de qualité de code
5. **Quality Gate** : Vérification des critères de qualité
6. **Préparation Version** : Préparation de l'artefact
7. **Nexus** : Déploiement vers Nexus
8. **Build Docker** : Création de l'image Docker
9. **Push DockerHub** : Publication sur DockerHub
10. **Docker Compose** : Lancement des conteneurs
11. **Health Check** : Vérification de la santé de l'application

## 🐳 Docker Compose

Le fichier `docker-compose.yml` lance :
- **MySQL** : Base de données (port 3306)
- **Spring Boot App** : Application (port 8089)
- **Prometheus** : Monitoring (port 9090)
- **Grafana** : Dashboards (port 3000)

### Commandes utiles

```bash
# Démarrer tous les services
docker-compose up -d

# Voir les logs
docker-compose logs -f

# Arrêter tous les services
docker-compose down

# Rebuild et redémarrer
docker-compose up -d --build
```

## 📈 Monitoring

### Prometheus
- URL: http://localhost:9090
- Métriques Spring Boot: http://localhost:8089/events/actuator/prometheus

### Grafana
- URL: http://localhost:3000
- Login: `admin` / `admin`
- Le dashboard "Events Project Monitoring" est automatiquement chargé

## 🧪 Tests API

### Swagger UI
- URL: http://localhost:8089/events/swagger-ui.html
- Documentation API: http://localhost:8089/events/api-docs

### Exemples de tests avec curl

```bash
# Ajouter un participant
curl -X POST http://localhost:8089/events/event/addPart \
  -H "Content-Type: application/json" \
  -d '{"nom":"Doe","prenom":"John","tache":"INVITE"}'

# Récupérer les logistiques
curl http://localhost:8089/events/event/getLogs/2024-01-01/2024-12-31
```

## 🔍 SonarQube

Après l'exécution du pipeline, consulter les résultats dans SonarQube :
- URL: http://votre-sonarqube:9000
- Projet: `eventsProject`

## ⚠️ Notes importantes

- Ajuster les URLs et credentials selon votre environnement
- Le pipeline utilise des variables d'environnement Jenkins
- Les ports peuvent être modifiés dans `docker-compose.yml`
- Pour la production, utiliser des secrets managés

