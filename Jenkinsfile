pipeline {
    agent any

    // Déclencheur automatique lors d'un push sur GitHub
    triggers {
        // Poll SCM toutes les minutes pour détecter les changements
        pollSCM('H/1 * * * *')
    }

    // 1. Définir l'environnement pour les étapes Docker
    environment {
        // Remplacez mariembouchaddakh par votre ID DockerHub si necessaire
        DOCKER_REPO = 'mariembouchaddakh/events-project'
        
        // docker-hub-credentials est l ID de l identifiant DockerHub cree dans Jenkins
        DOCKER_CREDENTIAL_ID = 'docker-hub-credentials'
    }

    stages {
        stage('Récupération du code') {
            steps {
                echo "Pulling from Git"
                // Adaptez l'URL du dépôt selon votre repository
                git branch: 'main', url: 'https://github.com/mariembouchaddakh/eventsProject.git', 
                credentialsId: 'git1'
            }
        }

        // 2. Récupérer la version du projet
        stage('Get Version') {
            steps {
                script {
                    // Utiliser le settings.xml de Jenkins s'il existe, sinon ignorer le global
                    def settingsFile = "${env.HOME}/.m2/settings.xml"
                    def mvnCmd = "mvn help:evaluate -Dexpression=project.version -q -DforceStdout"
                    if (fileExists(settingsFile)) {
                        mvnCmd = "mvn help:evaluate -Dexpression=project.version -q -DforceStdout -s ${settingsFile}"
                    }
                    env.APP_VERSION = sh(returnStdout: true, script: mvnCmd).trim()
                    echo "Version du projet: ${env.APP_VERSION}"
                }
            }
        }

        // 3. Compilation et création du JAR
        stage('Compilation & Package Maven') {
            steps {
                echo "Building code with Maven and creating JAR"
                sh 'mvn clean package' // Les tests sont exécutés automatiquement
            }
        }

        // 4. Analyse SonarQube
        stage('Analyse SonarQube') {
            steps {
                echo "Analyse de la Qualité du Code"
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=eventsProject \
                        -Dsonar.host.url=http://10.0.2.15:9000 \
                        -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            } 
        } 

        // 5. Exécuter les tests unitaires
        stage('Test') {
            steps {
                echo "Lancement des tests unitaires JUnit"
                sh 'mvn test' 
            }
        }

        stage('Publish Test Results') {
            steps {
                echo "Publication des résultats des tests"
                junit '**/target/surefire-reports/*.xml' 
            }
        }

        // 6. Déploiement vers Nexus
        stage('Nexus') {
            steps {
                echo "Déploiement vers Nexus"
                // Les credentials sont dans /var/lib/jenkins/.m2/settings.xml
                sh 'mvn deploy -DskipTests'
            }
        }
        
        // --- ETAPES CI/CD (DOCKER) ---

        // 7. Construire l'image Docker
        stage('Build Docker Image') {
            steps {
                echo "Construction de l'image Docker : ${DOCKER_REPO}:${APP_VERSION}"
                script {
                    // La commande 'docker build' est encapsulée dans la syntaxe Jenkins Docker
                    docker.build "${DOCKER_REPO}:${APP_VERSION}", "."
                }
            }
        }

        // 8. Push vers Docker Hub
        stage('Push to DockerHub') {
            steps {
                echo "Push vers DockerHub"
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIAL_ID) {
                        // Utilisation du tag dynamique
                        docker.image("${DOCKER_REPO}:${APP_VERSION}").push()
                    }
                }
            }
        }

        // 9. Déploiement avec Docker Compose
        stage('Deploy with Docker Compose') {
            steps {
                echo 'Arrêt et suppression des conteneurs de l application uniquement'
                // Arrêter uniquement les conteneurs events-app et mysql, sans toucher à Prometheus et Grafana
                sh 'docker stop events-spring-app events-mysql || true'
                sh 'docker rm events-spring-app events-mysql || true'
                echo 'Démarrage de l application et de la base de données via Docker Compose'
                // Démarrer uniquement les services nécessaires (events-app et mysql)
                // Utiliser --no-recreate pour ne pas recréer les conteneurs existants (Prometheus, Grafana)
                sh 'docker compose up -d --build --no-recreate events-app mysql' 
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline réussi !'
        }
        failure {
            echo 'Pipeline échoué !'
        }
        always {
            cleanWs()
        }
    }
}
