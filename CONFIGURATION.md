# Configuration du Pipeline CI/CD - Events Project

## 🔑 Credentials Jenkins à configurer

### 1. DockerHub (ID: `docker-hub-credentials`)
- **Type**: Username with password
- **Username**: votre username DockerHub (ex: `mariembouchaddakh`)
- **Password**: votre token DockerHub

### 2. Git (ID: `git1`)
- **Type**: Username with password ou SSH
- **URL**: `https://github.com/mariembouchaddakh/eventsProject.git`
- **Credentials**: votre token GitHub ou clé SSH

### 3. SonarQube Token (ID: `sonar-token`)
- **Type**: Secret text
- **Secret**: votre token SonarQube (généré depuis SonarQube)

## 📝 Modifications à faire dans le Jenkinsfile

### 1. URL Git
```groovy
git branch: 'master', url: 'https://github.com/mariembouchaddakh/eventsProject.git', 
credentialsId: 'git1'
```
**À modifier** : Remplacez l'URL par votre repository Git réel.

### 2. Docker Repository
```groovy
DOCKER_REPO = 'mariembouchaddakh/events-project'
```
**À modifier** : Remplacez `mariembouchaddakh` par votre username DockerHub.

### 3. SonarQube URL
```groovy
-Dsonar.host.url=http://10.0.2.15:9000
```
**À modifier** : Remplacez `10.0.2.15:9000` par l'IP/hostname de votre serveur SonarQube.

### 4. Nexus URL (dans pom.xml)
```xml
<url>http://nexus-server:8081/repository/maven-releases/</url>
```
**À modifier** : Remplacez `nexus-server:8081` par l'URL de votre serveur Nexus.

## 🐳 Configuration Docker

### Docker Compose
Le fichier `docker-compose.yml` lance automatiquement :
- MySQL (port 3306)
- Spring Boot App (port 8089)
- Prometheus (port 9090)
- Grafana (port 3000)

### Variables d'environnement
Les variables sont définies dans `docker-compose.yml` :
- `SPRING_DATASOURCE_URL`: URL de connexion MySQL
- `SPRING_DATASOURCE_USERNAME`: root
- `SPRING_DATASOURCE_PASSWORD`: rootpassword

## 📊 Accès aux services

### Application Spring Boot
- **URL**: http://localhost:8089/events
- **Swagger UI**: http://localhost:8089/events/swagger-ui.html
- **API Docs**: http://localhost:8089/events/api-docs
- **Health Check**: http://localhost:8089/events/actuator/health
- **Prometheus Metrics**: http://localhost:8089/events/actuator/prometheus

### Grafana
- **URL**: http://localhost:3000
- **Login**: admin / admin
- **Dashboard**: Events Project Monitoring (chargé automatiquement)

### Prometheus
- **URL**: http://localhost:9090

## 🧪 Tests avec Postman/Swagger

### Exemple : Ajouter un participant
```bash
POST http://localhost:8089/events/event/addPart
Content-Type: application/json

{
  "nom": "Doe",
  "prenom": "John",
  "tache": "INVITE"
}
```

### Exemple : Récupérer les logistiques
```bash
GET http://localhost:8089/events/event/getLogs/2024-01-01/2024-12-31
```

## 🔍 SonarQube

### Configuration
- **Project Key**: `eventsProject`
- **Host URL**: À configurer dans le Jenkinsfile
- **Token**: À configurer dans Jenkins credentials

### Quality Gate
Le pipeline attend la validation du Quality Gate avant de continuer.

## 📦 Nexus

### Configuration Maven
Créer un fichier `~/.m2/settings.xml` avec les credentials Nexus (voir `settings.xml.example`).

### Repository
- **Releases**: `http://nexus-server:8081/repository/maven-releases/`
- **Snapshots**: `http://nexus-server:8081/repository/maven-snapshots/`

## ✅ Checklist avant exécution

- [ ] Credentials Jenkins configurés (DockerHub, Git, SonarQube)
- [ ] URL Git mise à jour dans Jenkinsfile
- [ ] Docker Repository mis à jour dans Jenkinsfile
- [ ] SonarQube URL mise à jour dans Jenkinsfile
- [ ] Nexus URL mise à jour dans pom.xml
- [ ] Docker et Docker Compose installés sur le serveur Jenkins
- [ ] SonarQube accessible depuis Jenkins
- [ ] Nexus accessible depuis Jenkins (si utilisé)

## 🚀 Exécution

1. Créer un nouveau Pipeline job dans Jenkins
2. Configurer : Pipeline script from SCM
3. Spécifier : Git repository + Script Path: `Jenkinsfile`
4. Lancer le build

