pipeline {
    agent any
    stages {
        stage('Verificar Repositório') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], useRemoteConfigs: [[url: 'https://github.com/Yasmin-Braga7/ProjetoAPP']]])
            }
        }

        stage('Fetch Secrets') {
            steps {
                bat 'npx -y @infisical/cli export --env="prod" --path="/receitix" --token="st.65702f07-d5f1-4681-8dfd-3b1f5e5c47d9.c4b552bb9bd4f97d88d011636cd877bf.70bdf1b0c24fd4ad23c9818b619b2695" > .env'
            }
        }

        stage('Instalar Dependências') {
            steps {
                script {
                    // Atualiza o PATH se necessário
                    env.PATH = "/usr/bin:$PATH"
                    // Instalar as dependências Maven antes de compilar o projeto
                    bat 'mvn clean install'  // Instala as dependências do Maven
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

                    // Executar o novo container (o docker-compose.yml lê o .env gerado no stage Fetch Secrets)
                    bat "docker-compose up -d --build"
                }
            }
        }
    }
    post {
        always {
            // Remove o arquivo .env do workspace para não deixar segredos em texto puro salvos no disco do Jenkins
            bat 'del /f /q .env 2> nul || exit 0'
        }
        success {
            echo 'Deploy realizado com sucesso!'
        }
        failure {
            echo 'Houve um erro durante o deploy.'
        }
    }
}
