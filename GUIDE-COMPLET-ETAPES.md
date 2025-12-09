# 📋 Guide Complet - Mise en place du Pipeline CI/CD

## ✅ Étape 1 : Tests Unitaires (DÉJÀ FAIT)

Les tests unitaires avec Mockito sont déjà créés :
- ✅ `EventServicesImplTest.java` - Tests du service
- ✅ `EventRestControllerTest.java` - Tests du contrôleur REST

**Vérification** : Exécutez `mvn test` pour vérifier que tous les tests passent.

---

## 🔧 Étape 2 : Configuration Jenkins

### 2.1 Installation et Prérequis

1. **Vérifier que Jenkins est installé et démarré**
   ```bash
   # Vérifier le statut
   sudo systemctl status jenkins
   # Ou accéder à http://localhost:8080
   ```

2. **Plugins Jenkins nécessaires** (Installer via Jenkins → Manage Jenkins → Plugins)
   - ✅ Pipeline
   - ✅ Docker Pipeline
   - ✅ Git
   - ✅ Maven Integration
   - ✅ SonarQube Scanner
   - ✅ JUnit
   - ✅ GitHub (pour webhook auto-trigger)

### 2.2 Configuration des Credentials

Dans Jenkins → **Manage Jenkins** → **Credentials** → **System** → **Global credentials** :

#### A. DockerHub Credentials
1. Cliquez sur **"Add Credentials"**
2. **Kind** : Username with password
3. **Scope** : Global
4. **Username** : `mariembouchaddakh` (votre username DockerHub)
5. **Password** : Votre token DockerHub (Settings → Security → New access token)
6. **ID** : `docker-hub-credentials` ⚠️ **EXACTEMENT ce nom**
7. **Description** : DockerHub credentials
8. Cliquez sur **OK**

#### B. Git Credentials
1. **Add Credentials**
2. **Kind** : Username with password (ou SSH Username with private key)
3. **Username** : Votre username GitHub
4. **Password** : Votre Personal Access Token GitHub
5. **ID** : `git1` ⚠️ **EXACTEMENT ce nom**
6. Cliquez sur **OK**

#### C. SonarQube Token
1. **Générer le token dans SonarQube** :
   - Allez sur SonarQube → My Account → Security
   - Générer un nouveau token (ex: `jenkins-token`)
   - **COPIEZ-LE** (vous ne le reverrez plus)
2. Dans Jenkins → **Add Credentials**
3. **Kind** : Secret text
4. **Secret** : Collez le token SonarQube
5. **ID** : `sonar-token` ⚠️ **EXACTEMENT ce nom**
6. Cliquez sur **OK**

### 2.3 Configuration SonarQube dans Jenkins

1. Jenkins → **Manage Jenkins** → **Configure System**
2. Section **"SonarQube servers"**
3. Cliquez sur **"Add SonarQube"**
4. **Name** : `SonarQube` ⚠️ **EXACTEMENT ce nom**
5. **Server URL** : `http://10.0.2.15:9000` (ou votre IP SonarQube)
6. **Server authentication token** : Sélectionnez `sonar-token` (le credential créé)
7. Cliquez sur **"Save"**

### 2.4 Créer le Pipeline Job

1. Jenkins → **New Item**
2. **Item name** : `eventsProject-pipeline`
3. Sélectionnez **"Pipeline"**
4. Cliquez sur **OK**

5. Dans la configuration du Pipeline :
   - **Definition** : Pipeline script from SCM
   - **SCM** : Git
   - **Repository URL** : `https://github.com/mariembouchaddakh/eventsProject.git`
   - **Credentials** : Sélectionnez `git1`
   - **Branches to build** : `*/main` (ou `*/master`)
   - **Script Path** : `Jenkinsfile` ⚠️ **EXACTEMENT ce nom**
   - Cliquez sur **Save**

### 2.5 Configuration Webhook GitHub (Auto-trigger)

1. **Dans GitHub** : Allez sur votre repo → **Settings** → **Webhooks**
2. Cliquez sur **"Add webhook"**
3. **Payload URL** : `http://VOTRE_IP_JENKINS:8080/github-webhook/`
   - Exemple : `http://10.0.2.15:8080/github-webhook/`
4. **Content type** : `application/json`
5. **Which events** : Sélectionnez **"Just the push event"**
6. Cliquez sur **"Add webhook"**

**Alternative** : Si webhook ne fonctionne pas, utilisez **"Build with Parameters"** ou **"Build Now"** manuellement.

---

## 🔍 Étape 3 : Configuration SonarQube

### 3.1 Vérifier SonarQube

1. Accédez à SonarQube : `http://10.0.2.15:9000` (ou votre IP)
2. Connectez-vous (admin/admin par défaut)
3. Vérifiez que le serveur fonctionne

### 3.2 Créer le Projet dans SonarQube

1. SonarQube → **Projects** → **Create Project**
2. **Project key** : `eventsProject` ⚠️ **EXACTEMENT ce nom** (comme dans Jenkinsfile)
3. **Display name** : `Events Project`
4. Cliquez sur **"Set Up"**
5. Choisissez **"With Jenkins"** ou **"Manually"**
6. **Token** : Utilisez le token créé pour Jenkins (ou créez-en un nouveau)

### 3.3 Vérifier la Configuration

Le fichier `sonar-project.properties` est déjà créé dans le projet avec :
- Project Key : `eventsProject`
- Sources : `src/main/java`
- Tests : `src/test/java`

---

## 📦 Étape 4 : Configuration Nexus (Optionnel mais dans le pipeline)

### 4.1 Installer et Configurer Nexus

1. **Démarrer Nexus** (si pas déjà fait)
2. Accéder à : `http://nexus-server:8081` (ou votre URL Nexus)
3. Se connecter (admin / admin123 par défaut)

### 4.2 Créer les Repositories

1. Nexus → **Settings** → **Repositories** → **Create repository**
2. Créer **maven-releases** (type: maven2 hosted, version policy: Release)
3. Créer **maven-snapshots** (type: maven2 hosted, version policy: Snapshot)

### 4.3 Configurer Maven Settings

1. Créer/modifier `~/.m2/settings.xml` :
```xml
<settings>
    <servers>
        <server>
            <id>nexus-releases</id>
            <username>admin</username>
            <password>admin123</password>
        </server>
        <server>
            <id>nexus-snapshots</id>
            <username>admin</username>
            <password>admin123</password>
        </server>
    </servers>
</settings>
```

2. **Mettre à jour le pom.xml** : Vérifier que l'URL Nexus est correcte dans `distributionManagement`

---

## 🐳 Étape 5 : Configuration Docker et DockerHub

### 5.1 Vérifier Docker

```bash
# Vérifier que Docker est installé
docker --version
docker-compose --version
```

### 5.2 Créer un Compte DockerHub

1. Allez sur https://hub.docker.com
2. Créez un compte (ou connectez-vous)
3. **Username** : `mariembouchaddakh` (ou le vôtre)

### 5.3 Générer un Token DockerHub

1. DockerHub → **Account Settings** → **Security**
2. **New Access Token**
3. Donnez un nom : `jenkins-dockerhub`
4. **Permissions** : Read & Write
5. **Générer** et **COPIER LE TOKEN**
6. Utilisez ce token dans les credentials Jenkins (`docker-hub-credentials`)

### 5.4 Vérifier les Fichiers Docker

✅ `Dockerfile` - Déjà créé
✅ `docker-compose.yml` - Déjà créé
✅ `.dockerignore` - Déjà créé

---

## 🚀 Étape 6 : Premier Lancement du Pipeline

### 6.1 Lancer le Pipeline

**Option A : Auto-trigger (Webhook)**
- Faites un simple `git push` vers GitHub
- Le pipeline se lance automatiquement

**Option B : Manuel**
1. Jenkins → Votre pipeline → **"Build Now"**

### 6.2 Suivre l'Exécution

1. Cliquez sur le build en cours
2. Cliquez sur **"Console Output"** pour voir les logs
3. Vérifiez chaque étape :
   - ✅ Récupération Git
   - ✅ Compilation
   - ✅ Tests unitaires
   - ✅ SonarQube
   - ✅ Build Docker
   - ✅ Push DockerHub
   - ✅ Docker Compose

### 6.3 Vérifier les Résultats

- **Tests** : Jenkins affiche les résultats JUnit
- **SonarQube** : Allez sur SonarQube pour voir l'analyse
- **DockerHub** : Vérifiez que l'image est poussée
- **Docker Compose** : Vérifiez que les conteneurs tournent

---

## 🔧 Étape 7 : Corriger les Problèmes SonarQube

### 7.1 Consulter les Problèmes

1. Allez sur SonarQube → Votre projet `eventsProject`
2. Section **"Issues"** pour voir les problèmes
3. Section **"Code Smells"**, **"Bugs"**, **"Vulnerabilities"**

### 7.2 Corriger les Problèmes

**Types de problèmes courants :**
- **Code smells** : Améliorer la qualité du code
- **Duplications** : Refactoriser le code dupliqué
- **Couverture de tests** : Ajouter plus de tests
- **Complexité** : Simplifier les méthodes complexes

### 7.3 Relancer l'Analyse

Après corrections :
```bash
git add .
git commit -m "Correction des problèmes SonarQube"
git push
```
Le pipeline se relance automatiquement (si webhook configuré).

---

## 🧪 Étape 8 : Tester avec Swagger/Postman

### 8.1 Démarrer l'Application

```bash
# Si pas déjà démarré via docker-compose
docker-compose up -d

# Vérifier que l'application tourne
curl http://localhost:8089/events/actuator/health
```

### 8.2 Accéder à Swagger

1. Ouvrez votre navigateur
2. Allez sur : **http://localhost:8089/events/swagger-ui.html**
3. Vous devriez voir toute l'API documentée

### 8.3 Test avec Swagger

**Test 1 : Ajouter un Participant**
1. Dans Swagger, trouvez `POST /event/addPart`
2. Cliquez sur **"Try it out"**
3. Modifiez le JSON :
```json
{
  "nom": "Doe",
  "prenom": "John",
  "tache": "INVITE"
}
```
4. Cliquez sur **"Execute"**
5. **Notez l'ID** retourné (ex: `idPart: 1`)

**Test 2 : Récupérer les Logistiques**
1. Trouvez `GET /event/getLogs/{d1}/{d2}`
2. Cliquez sur **"Try it out"**
3. Entrez les dates : `d1=2024-01-01`, `d2=2024-12-31`
4. Cliquez sur **"Execute"**
5. Vérifiez la réponse

### 8.4 Test avec Postman (Alternative)

**Collection Postman** :
1. Créer une nouvelle requête
2. **POST** `http://localhost:8089/events/event/addPart`
3. **Body** → **raw** → **JSON** :
```json
{
  "nom": "Test",
  "prenom": "User",
  "tache": "ORGANISATEUR"
}
```
4. **Send** → Notez l'ID retourné
5. **GET** `http://localhost:8089/events/event/getLogs/2024-01-01/2024-12-31`
6. **Send** → Vérifiez la réponse

---

## 📊 Étape 9 : Monitoring avec Prometheus et Grafana

### 9.1 Vérifier Prometheus

1. Accédez à : **http://localhost:9090**
2. Vérifiez que Prometheus collecte les métriques
3. Dans la barre de recherche, tapez : `up` → Execute
4. Vous devriez voir les targets (spring-boot-app, mysql, prometheus)

### 9.2 Vérifier les Métriques Spring Boot

1. Accédez à : **http://localhost:8089/events/actuator/prometheus**
2. Vous devriez voir toutes les métriques exposées

### 9.3 Configurer Grafana

1. Accédez à : **http://localhost:3000**
2. **Login** : `admin` / `admin` (changez le mot de passe si demandé)

### 9.4 Vérifier le Datasource

1. Grafana → **Configuration** → **Data Sources**
2. Vérifiez que **Prometheus** est configuré
3. **URL** : `http://prometheus:9090`
4. Cliquez sur **"Save & Test"** → Devrait afficher "Data source is working"

### 9.5 Importer le Dashboard

Le dashboard est déjà configuré via `grafana/dashboards/events-dashboard.json`

1. Grafana → **Dashboards** → **Browse**
2. Vous devriez voir **"Events Project Monitoring"**
3. Cliquez dessus pour voir les métriques

### 9.6 Vérifier les Métriques

Le dashboard devrait afficher :
- ✅ Application Health
- ✅ HTTP Requests Rate
- ✅ JVM Memory Usage
- ✅ Database Connections

---

## 👥 Étape 10 : Ajouter l'Enseignant au Repository GitHub

### 10.1 Inviter un Collaborateur

1. GitHub → Votre repo → **Settings** → **Collaborators**
2. Cliquez sur **"Add people"**
3. Entrez l'**email FST de l'enseignant** (ex: `enseignant@fst.tn`)
4. Sélectionnez le collaborateur
5. **Permission** : **Write** (ou **Admin**)
6. Cliquez sur **"Add [nom] to this repository"**

### 10.2 Vérification

L'enseignant recevra un email d'invitation et pourra accepter l'invitation.

---

## 🔄 Étape 11 : Relancer le Pipeline Complet

### 11.1 Via Push (Auto-trigger)

```bash
# Faire une petite modification
echo "# Test" >> README.md
git add README.md
git commit -m "Test pipeline auto-trigger"
git push
```

Le pipeline se lance automatiquement si le webhook est configuré.

### 11.2 Via Jenkins (Manuel)

1. Jenkins → Votre pipeline → **"Build Now"**

### 11.3 Via Script (Optionnel)

Créer un script `trigger-pipeline.sh` :
```bash
#!/bin/bash
curl -X POST http://localhost:8080/job/eventsProject-pipeline/build \
  --user admin:VOTRE_TOKEN_JENKINS
```

---

## ✅ Checklist Finale

Avant la présentation, vérifiez :

- [ ] ✅ Tests unitaires créés et passent (`mvn test`)
- [ ] ✅ Jenkins configuré avec tous les credentials
- [ ] ✅ Pipeline créé et fonctionne
- [ ] ✅ SonarQube configuré et analyse OK
- [ ] ✅ Nexus configuré (si utilisé)
- [ ] ✅ DockerHub : image poussée avec succès
- [ ] ✅ Docker Compose : tous les conteneurs tournent
- [ ] ✅ Swagger accessible et tests fonctionnent
- [ ] ✅ Prometheus collecte les métriques
- [ ] ✅ Grafana affiche le dashboard
- [ ] ✅ Enseignant ajouté au repo GitHub
- [ ] ✅ Pipeline se lance automatiquement au push

---

## 🆘 Résolution de Problèmes Courants

### Pipeline échoue à l'étape SonarQube
- Vérifier que SonarQube est accessible depuis Jenkins
- Vérifier le token SonarQube
- Vérifier l'URL SonarQube dans le Jenkinsfile

### Docker push échoue
- Vérifier les credentials DockerHub dans Jenkins
- Vérifier que vous êtes connecté à DockerHub

### Tests échouent
- Exécuter `mvn test` localement pour voir les erreurs
- Vérifier que tous les mocks sont corrects

### Docker Compose ne démarre pas
- Vérifier les ports (3306, 8089, 9090, 3000) ne sont pas utilisés
- Vérifier les logs : `docker-compose logs`

---

## 📝 Notes Importantes

1. **Gardez les IDs exacts** : `docker-hub-credentials`, `git1`, `sonar-token`, `SonarQube`
2. **Project Key SonarQube** : `eventsProject` (dans Jenkinsfile et sonar-project.properties)
3. **Docker Repository** : `mariembouchaddakh/events-project` (à adapter)
4. **Webhook** : Configurez-le pour l'auto-trigger (solution idéale)

---

**Bon courage pour la mise en place ! 🚀**

