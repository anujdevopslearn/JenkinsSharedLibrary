def call(Map config = [:]){
    def gitURL = config.gitURL
    def productType = config.productType
    def pomfileName = config.pomfileName
    pipeline {
        agent any
        environment {
            gitBranch = "${env.BRANCH_NAME}"
        }
        stages{
            stage("Code Checkout"){
                steps{
                    git changelog: false, poll: false, url: gitURL, branch: gitBranch
                }
            }
            stage("Code Build"){
                steps{
                    script {
                        sh """
                            ls -lart
                            mvn clean install -f ${pomfileName}
                        """
                    }
                }
            }
            stage("Docker Image Build"){
                when {
                    expression {
                        productType == "docker"
                    }
                }
                steps{
                    script {
                        sh """
                            echo "Docker Image Build"
                        """
                    }
                }
            }
        }
    }
}