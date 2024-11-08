pipeline {
    agent any

    environment {
        // Defina o nome da imagem e a tag
        DOCKER_IMAGE = "menoci/https://github.com/GuilhermeMenoci/sistema-despesas:latest"
    }

    stages {
        stage('Build') {
            steps {
                // Comando para buildar o JAR (ajuste conforme necessário)
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build da imagem Docker
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                // Autenticação no Docker Hub
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        sh "docker push ${DOCKER_IMAGE}"
                    }
                }
            }
        }
    }

    post {
        always {
            // Remover a imagem localmente após o push, para economizar espaço
            sh "docker rmi ${DOCKER_IMAGE}"
        }
    }
}
