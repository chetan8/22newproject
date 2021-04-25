pipeline {
    agent any
    stages {
        stage("This is for git") {
            steps {
                git 'https://github.com/chetan8/nature.git'
            }
        }
        stage("This is for maven") {
            steps {
                sh 'mvn compile test '
            }
        }
        stage("This is for sonarqube") {
            steps {
                withSonarQubeEnv('sonar_server_jenkins') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage("This is nexus") {
            steps {
                nexusPublisher nexusInstanceId: 'localnexus3', nexusRepositoryId: 'newdeploy', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'target/flowersfruits.war']], mavenCoordinate: [artifactId: 'flowersfruits', groupId: 'nature', packaging: 'war', version: '3.4']]]
            }
        }
        stage("This is for tomcat") {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'Tomcat _Credentials', path: '', url: 'http://192.168.1.2:8090')], contextPath: null, war: '**/*.war'
            }
        }
    }
}
