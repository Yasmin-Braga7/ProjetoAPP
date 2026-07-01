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
                // TODO: troque pelo token NOVO gerado no Infisical (o antigo foi revogado por ter sido exposto)
                bat 'npx -y @infisical/cli export --env="prod" --path="/receitix" --token="st.d8a9d665-770a-45a4-8077-4c7cf7d6ded3.d6f48d4527c29d89e8665f180f229a23.fe1fdd7848cbfde93e309935d7eba271" > .env'
                // O infisical export envolve os valores em aspas (ex: DB_URL="jdbc:mysql://...").
                // O "env_file:" do docker-compose NAO remove essas aspas, entao a variavel
                // chega ao Spring com a aspa dentro do valor (por isso o erro
                // "'url' must start with jdbc"). Este passo remove as aspas do .env.
                powershell '(Get-Content .env) -replace \'^([^=]+)="(.*)"$\', \'$1=$2\' | Set-Content .env'
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
