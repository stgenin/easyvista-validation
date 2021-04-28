node() {

    def repoURL = 'https://github.com/stgenin/easyvista-validation.git'

    stage("Prepare Workspace") {
        cleanWs()
        env.WORKSPACE_LOCAL = sh(returnStdout: true, script: 'pwd').trim()
        env.BUILD_TIME = sh(returnStdout: true, script: 'date +%F-%T').trim()
        echo "Workspace set to:" + env.WORKSPACE_LOCAL
        echo "Build time:" + env.BUILD_TIME
    }

    stage('Checkout Self') {
        git branch: 'master', credentialsId: '', url: repoURL
    }
    stage ('Import Scenarios from XRay') {
        step([$class: 'XrayExportBuilder', credentialId: '', filePath: 'src/test/resources', issues: 'MXP-52', serverInstance: 'SERVER-0ee72acd-5079-404f-8b3e-c3eb0827002a'])
    }
    stage('Cucumber Tests') {
            withMaven(maven: 'maven35') {
            sh """
			cd ${env.WORKSPACE_LOCAL}
			mvn clean test
		"""
        }
    }

    stage('Expose report') {
        archiveArtifacts artifacts: "**/cucumber.json"
        cucumber '**/cucumber.json'
    }

    stage('Import results to Xray') {

   		def description = "[BUILD_URL|${env.BUILD_URL}]"
    	def labels = '["regression","automated_regression"]'
    	def environment = "DEV"
  		def testExecutionFieldId = 10007
   		def testEnvironmentFieldName = "customfield_10131"
    	def projectKey = "MXP"
    	def xrayConnectorId = '0ee72acd-5079-404f-8b3e-c3eb0827002a'
   		def info = '''{
   				"fields": {
    			"project": {
    			"key": "''' + projectKey + '''"
    		},
    			"labels":''' + labels + ''',
    			"description":"''' + description + '''",
    			"summary": "Automated Regression Execution @ ''' + env.BUILD_TIME + ' ' + environment + ''' " ,
    			"issuetype": {
    			"id": "''' + testExecutionFieldId + '''"
    			},
    			"''' + testEnvironmentFieldName + '''" : [
    			"''' + environment + '''"
    		    ]
    		}
    	}'''

    	echo info

    	step([$class: 'XrayImportBuilder', endpointName: '/cucumber/multipart', importFilePath: 'target/cucumber.json', importInfo: info, inputInfoSwitcher: 'fileContent', serverInstance: xrayConnectorId])
    }
}