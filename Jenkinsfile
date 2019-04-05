pipeline {
    agent { label 'docker' }
    environment {
        projectname = JOB_NAME.toLowerCase()
    }
    stages {
        stage ('Pipeline configuration') {
            agent none
            steps {
                script {
                    def server = Artifactory.server('artifactory')
                    def rtMaven = Artifactory.newMavenBuild()
                    def buildInfo
                    def version = "${env.BUILD_NUMBER}"
                    def port=9191
                    def dockerNetwork="devopscoc-demotoolchain_default"

                  stage('Java Build') { 
                    // resolver is used for downloading dependencies
                    // rtMaven.resolver server: server, releaseRepo: 'maven', snapshotRepo: 'maven'
                    rtMaven.deployer server: server, releaseRepo: 'maven', snapshotRepo: 'maven'
                    rtMaven.tool = "maven"

                    buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean package -Dmaven.test.skip=true'
                  }

                  stage('Publish to Artifactory') { 
                    server.publishBuildInfo buildInfo
                  } 

                  stage('JUnit') { 
                    try {
                        buildInfo = rtMaven.run pom: 'pom.xml', goals: 'test'
                    } finally {
                        junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                    }                   
                  } 

                  stage('QA') { 
                    withSonarQubeEnv('sonar') {
                      buildInfo = rtMaven.run pom: 'pom.xml', goals: 'sonar:sonar'
                    }
                  }

                  stage('Sonarqube Quality Gate') { 
                    sleep 20
                    def qg = waitForQualityGate()
                    if (qg.status != 'OK') {
                      error "Pipeline aborted due to quality gate failure: ${qg.status}"
                    }
                  }

                  stage('Build Dockerimage') { 
                    docker.withServer('tcp://socatdockersock:2375') {
                      def customImage = docker.build("${projectname}:${version}")
                    }
                  }

                  stage('Deploy') { 
                    docker.withServer('tcp://socatdockersock:2375') {
                      // stop old container, if no container is running, exit with true
                      sh 'docker stop "$(docker ps -qf name=deploy-${projectname})" || true'
                      // deploy fresh container
                      sh """docker run --net ${dockerNetwork} \
                      --name deploy-${projectname}${version} -d -p ${port}:${port} ${projectname}:${version}"""
                    }
                    echo "Application was deployed on http://localhost:${port}"
                  }
                }
            }
        }
    } 
}
