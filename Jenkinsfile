pipeline {
    agent any

    environment {
        NAME = "spring-app"
        VERSION = "${env.BUILD_ID}"
        GIT_COMMIT = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
        IMAGE_REPO = "yugandhar99"
        GIT_REPO_NAME = "jenkins-argocd-vault-gitops"
        GIT_USER_NAME = "yugandhar99"
       
    }

    tools { 
        maven 'maven-3.8.6' 
    }
    stages {
        stage('Checkout git') {
            steps {
              git branch: 'main', url:'https://github.com/yugandhar99/jenkins-argocd-vault-devsecops.git'
            }
        }
        
        stage ('Build & JUnit Test') {
            steps {
                sh 'mvn clean install' 
            }
        }

        stage('SonarQube Analysis'){
            steps{
                withSonarQubeEnv('SonarQube-server') {
                        sh '''mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=gitops-with-argocd \
                        -Dsonar.projectName='gitops-with-argocd' \
                        -Dsonar.host.url=$sonarurl \
                        -Dsonar.login=$sonarlogin'''
                }
            }
        }

        stage("Quality Gate") {
            steps {
              timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate abortPipeline: true
              }
            }
        }
        
        stage('Deploy to Artifactory') {
            environment {
                // Define the target repository in Artifactory
                TARGET_REPO = 'my-local-repo'
            }
            
            steps {
                script {
                    try {
                        def server = Artifactory.newServer url: 'http://ARTIFACTORY_HOST:8082/artifactory', credentialsId: 'jfrog-cred'
                        def uploadSpec = """{
                            "files": [
                                {
                                    "pattern": "target/*.jar",
                                    "target": "${TARGET_REPO}/"
                                }
                            ]
                        }"""
                        
                        server.upload(uploadSpec)
                    } catch (Exception e) {
                        error("Failed to deploy artifacts to Artifactory: ${e.message}")
                    }
                }
            }
        }
        
        stage('Docker  Build') {
            steps {
               
      	         sh 'docker build -t ${IMAGE_REPO}/${NAME}:${VERSION}-${GIT_COMMIT} .'
                
            }
        }

        stage('Docker Image Scan') {
            steps {
      	        sh ' trivy image --format template --template "@/usr/local/share/trivy/templates/html.tpl" -o report.html ${IMAGE_REPO}/${NAME}:${VERSION}-${GIT_COMMIT} '
            }
        }    
        
        stage('Upload Scan report to AWS S3') {
              steps {
                  
                //  sh 'aws configure set aws_access_key_id "$AWS_ACCESS_KEY_ID"  && aws configure set aws_secret_access_key "$AWS_ACCESS_KEY_SECRET"  && aws configure set region ap-south-1  && aws configure set output "json"' 
                  sh 'aws s3 cp report.html s3://YOUR-SCAN-REPORT-BUCKET/'
              }
        }
        
        stage ('Docker Image Push') {
            steps {
                withVault(configuration: [skipSslVerification: true, timeout: 60, vaultCredentialId: 'vault-token', vaultUrl: 'http://VAULT_HOST:8200'], vaultSecrets: [[path: 'secrets/creds/docker', secretValues: [[vaultKey: 'username'], [vaultKey: 'password']]]]) {
                    
                    sh 'echo "${password}" | docker login -u "${username}" --password-stdin'
                    sh 'docker push ${IMAGE_REPO}/${NAME}:${VERSION}-${GIT_COMMIT}'
                    sh 'docker rmi  ${IMAGE_REPO}/${NAME}:${VERSION}-${GIT_COMMIT}'
                    
                }
            }
        }
        
        stage('Clone/Pull k8s deployment Repo') {
            steps {
                script {
                    if (fileExists('gitops-manifests-workspace')) {

                        echo 'Cloned repo already exists - Pulling latest changes'

                        dir("gitops-manifests-workspace") {
                          sh 'git pull'
                        }

                    } else {
                        echo 'Repo does not exists - Cloning the repo'
                        sh 'git clone -b feature https://github.com/yugandhar99/jenkins-argocd-vault-gitops.git'
                    }
                }
            }
        }
        
        stage('Update deployment Manifest') {
            steps {
                dir("gitops-manifests-workspace/yamls") {
                    sh 'sed -i "s#yugandhar99.*#${IMAGE_REPO}/${NAME}:${VERSION}-${GIT_COMMIT}#g" deployment.yaml'
                    sh 'cat deployment.yaml'
                }
            }
        }
        
        stage('Commit & Push changes to feature branch') {
            steps {
                withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]) {
                    dir("gitops-manifests-workspace/yamls") {
                        sh "git config --global user.email 'yugandharethamukkala1999@gmail.com'"
                        sh 'git remote set-url origin https://x-access-token:${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME}.git'
                        sh 'git checkout feature'
                        sh 'git add deployment.yaml'
                        sh "git commit -am 'Updated image version for Build- ${VERSION}-${GIT_COMMIT}'"
                        sh 'git push origin feature'
                    }
                }    
            }
        }
        
        stage('Raise PR') {
            steps {
                withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]) {
                    dir("gitops-manifests-workspace/yamls") {
                        sh '''
                            set +x
                            echo "$GITHUB_TOKEN" | gh auth login --with-token
                        '''
                        sh 'git branch'
                        sh 'git checkout feature'
                        sh "gh pr create -t 'image tag updated' -b 'check and merge it'"
                    }
                }    
            }
        } 
    }

    post{
        always{
            sendSlackNotifcation()
            }
        }
}

def sendSlackNotifcation()
{
    if ( currentBuild.currentResult == "SUCCESS" ) {
        buildSummary = "Job_name: ${env.JOB_NAME}\n Build_id: ${env.BUILD_ID} \n Status: *SUCCESS*\n Build_url: ${BUILD_URL}\n Job_url: ${JOB_URL} \n"
        slackSend(channel: '#devops', tokenCredentialId: 'jenkins-slack-credential', color: 'good', message: "${buildSummary}")
    }
    else {
        buildSummary = "Job_name: ${env.JOB_NAME}\n Build_id: ${env.BUILD_ID} \n Status: *FAILURE*\n Build_url: ${BUILD_URL}\n Job_url: ${JOB_URL}\n  \n "
        slackSend(channel: '#devops', tokenCredentialId: 'jenkins-slack-credential', color: 'danger', message: "${buildSummary}")
    }
}



