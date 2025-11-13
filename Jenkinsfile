pipeline {
    agent any
    stages {
        stage('Verificar Repositório') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], useRemoteConfigs: [[url: 'https://github.com/Yasmin-Braga7/ProjetoAPP']]])
            }
        }

        stage('Instalar Dependências') {
            steps {
                script {
                    // Atualiza o PATH se necessário
                    env.PATH = "/usr/bin:$PATH"
                    // Instalar as dependências Maven antes de compilar o projeto
                    bat 'mvn clean install'  // Instala as dependências do Maven

                    //dir('subpasta') {
                    //  bat "docker build -t ${imageTag} ."
                    //}

                }
            }
        }

        stage('Construir Imagem Docker') {
            steps {
                script {
                    def appName = 'receitix'
                    def imageTag = "${appName}:${env.BUILD_ID}"

                    // Construir a imagem Docker
                    bat "docker build -t ${imageTag} ."

                    //dir('subpasta') {
                    //  bat "docker build -t ${imageTag} ."
                    //}
                }
            }
        }

        stage('Fazer Deploy') {
            steps {
                script {
                    def appName = 'receitix'
                    def imageTag = "${appName}:${env.BUILD_ID}"

                    // Parar e remover o container existente, se houver
            		bat "docker stop ${appName} || exit 0"
            		bat "docker rm -v ${appName} || exit 0"  // Remover o container e os volumes associados

                    // Executar o novo container
                    bat "docker-compose up -d --build"
                }
            }
        }
    }
    post {
        success {
            echo 'Deploy realizado com sucesso!'
        }
        failure {
            echo 'Houve um erro durante o deploy.'
        }
    }
}