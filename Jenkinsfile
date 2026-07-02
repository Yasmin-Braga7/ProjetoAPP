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

                    // O "infisical run" injeta as secrets direto no processo do docker-compose,
                    // sem nunca escrever um .env em disco (evita tanto o vazamento de segredo
                    // quanto o bug das aspas que o "infisical export" gera).
                    withCredentials([string(credentialsId: 'infisical-token-prod', variable: 'INFISICAL_TOKEN')]) {
                        bat 'npx -y @infisical/cli run --env="prod" --path="/receitix" --token="st.2ab38cc2-e5df-43d6-8b9b-54b0c4f0c447.cbbf380bf00ef8a331a339d38eddf418.8708bdcbd9440a4138f72f048a68f473" -- docker-compose up -d --build'
                    }
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
