pipeline {
    agent any

    // 1. Définir l'environnement pour les étapes Docker
    environment {
        // Remplacez 'mariembouchaddakh' par votre ID DockerHub si nécessaire
        DOCKER_REPO = 'mariembouchaddakh/events-project'
        
        // 'docker-hub-credentials' est l'ID de l'identifiant DockerHub créé dans Jenkins
        DOCKER_CREDENTIAL_ID = 'docker-hub-credentials' 
        
        // Récupérer la version du projet directement depuis le pom.xml
        APP_VERSION = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout').trim()
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

        // 2. Compilation et création du JAR
        stage('Compilation & Package Maven') {
            steps {
                echo "Building code with Maven and creating JAR"
                sh 'mvn clean package -DskipTests' // '-DskipTests' pour éviter de relancer les tests ici
            }
        }

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

        // 3. Exécuter les tests unitaires
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

        stage('Nexus') {
            steps {
                echo "Déploiement vers Nexus"
                // Déploiement vers Nexus
                sh 'mvn deploy -DskipTests' 
            }
        }
        
        // --- ETAPES CI/CD (DOCKER) ---

        // 4. Construire l'image Docker
        stage('Build Docker Image') {
            steps {
                echo "Construction de l'image Docker : ${DOCKER_REPO}:${APP_VERSION}"
                script {
                    // La commande 'docker build' est encapsulée dans la syntaxe Jenkins Docker
                    docker.build "${DOCKER_REPO}:${APP_VERSION}", "."
                }
            }
        }

        // 5. Push vers Docker Hub
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

        // 6. Déploiement avec Docker Compose
        stage('Deploy with Docker Compose') {
            steps {
                echo 'Démarrage de l\'application et de la base de données via Docker Compose'
                // Arrêter les anciens conteneurs et démarrer les nouveaux en mode detached (-d)
                sh 'docker compose up -d --build' 
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
