import groovy.transform.Field

def getDeploymentConfigs (deployParams, portfolio, env, projectName ) {
    println "portfolio: " + portfolio + ", env: " + env + ", project: " + projectName

    def props = readJSON file: "${portfolio}/${projectName}.json"
    println "Props: " + props

    def defaultProps = readJSON file: "${portfolio}/defaultConfigs.json"
    println "D Props: " + defaultProps

    def defaultEnvProps = defaultProps[env]
    println "D Env Props: " + defaultEnvProps

    def envProps = props[env]
    println "Env Props: " + envProps


    // Update with Portfolio defaults
    deployParams.each {key, value -> 
        if (defaultEnvProps[key] != null) {
            echo "Overriding parameter '$key' with Porfolio Default: $value"
            deployParams[key] = value
        }
    }

    // Update with Project values
    deployParams.each {key, value -> 
        if (envProps[key] != null) {
            echo "Overriding parameter '$key' with Project value: $value"
            deployParams[key] = value
        }
    }    

    deployParams << mwDefaults.deployConstants

    deployParams.each {key, value -> 
        env[key.toUpperCase()] = value
    }

    println "Env: ${env}"
}