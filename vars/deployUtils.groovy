import groovy.transform.Field

def retrieveDeployConfigsFromFile (deployParams, filepath, env) {

    println "File Path: " + filepath
    try {
        def propsString = libraryResource filepath
        println "Props Str: " + propsString
        def props = readJSON text: propsString
        println "Props: " + props

        def envProps = props[env]
        println "Env Props: " + envProps

        // Update with Project values
        deployParams.each {key, value -> 
            if (envProps[key] != null) {
                echo "Overriding parameter '$key' with Project value: " + envProps[key]
                deployParams[key] = envProps[key]
            }
        } 
    } catch (Exception e) {
        echo "Error Occurred: " + e.toString();
    }

}

def getDeploymentProile (deployProfile, portfolio, projectName) {
    try {
        def propsString = libraryResource "${portfolio}/${projectName}1.json"
        def props = readJSON text: propsString
        if (props['deploy_profile'] != null) {
            deploy_profile = props['deploy_profile']
        }
    } catch (Exception e) {
        echo "Error Occurred: " + e.toString();
    }
}

def getDeploymentConfigs (deployParams, portfolio, env, projectName ) {
    println "portfolio: " + portfolio + ", env: " + env + ", project: " + projectName

    updateDeploymentConfigs (deployParams, "${portfolio}/defaultConfigs.json",  env)
    println "deployParams 1: " + deployParams

    updateDeploymentConfigs (deployParams, "${portfolio}/${projectName}1.json",  env)

    println "deployParams 2: " + deployParams 

    deployParams << mwDefaults.deployment_Params_Constants

    println "deployParams 3: " + deployParams
}