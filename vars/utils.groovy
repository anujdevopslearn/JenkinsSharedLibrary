def dockerImageBuild(containerName, tag){
    sh "docker image prune -f"
    sh "docker build -t $containerName:$tag --pull --no-cache ."
    withCredentials([usernamePassword(credentialsId: 'DockerHubCreds', passwordVariable: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKERHUB_USERNAME')]) {
        sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
        sh "docker tag $containerName:$tag $DOCKERHUB_USERNAME/$containerName:$tag"
        sh "docker push $DOCKERHUB_USERNAME/$containerName:$tag"
    }
    echo "Image build complete"
}

def kubernetesDeploy(){
    withCredentials([file(credentialsId: 'SERVICEACCOUNT', variable: 'SERVICEACCOUNT')]) {
        sh """
            echo $SERVICEACCOUNT
            gcloud auth activate-service-account --key-file=$SERVICEACCOUNT
            gcloud auth list
            gcloud config set project project-7-418106
            gcloud container clusters get-credentials jenkins-cd --zone asia-south1-c --project project-7-418106
            kubectl get node
        """
    }   
}
