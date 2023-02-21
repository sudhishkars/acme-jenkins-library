import groovy.transform.Field

def getDeploymentConfigs (deployParams, portfolio, env, projectName ) {
    def props = readJSON file: '$portfolio/$projectName.json'
    def defaultProps = readJSON file: '$portfolio/defaultConfigs.json'
    def defaultEnvProps = defaultProps[env]
    def envProps = props[env]

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

    echo "Env: $env"
}