grails {
    profile = 'rest-api'
    codegen.defaultPackage = 'alice.memory'

    // Whether to translate GORM events into Reactor events
    // Disabled by default for performance reasons
    gorm.reactor.events = false
}

info {
    app {
        name = '@info.app.name@'
        version = '@info.app.version@'
        grailsVersion = '@info.app.grailsVersion@'
    }
}
spring {
    main.'banner-mode' = "off"
    groovy.template.'check-template-location' = false
}

// Spring Actuator Endpoints are Disabled by Default
endpoints {
    enabled = false
    health.enabled = true
//    jmx.enabled = true
}

grails {
    mime {
        disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
        types {
            json = ['application/json', 'text/json']
            hal = ['application/hal+json', 'application/hal+xml']
            xml = ['text/xml', 'application/xml']
            atom = 'application/atom+xml'
            css = 'text/css'
            csv = 'text/csv'
            js = 'text/javascript'
            rss = 'application/rss+xml'
            text = 'text/plain'
            all = '*/*'
        }
    }
    urlmapping.cache.maxsize = 1000
    controllers.defaultScope = singleton
    converters.encoding = 'UTF-8'
}

hibernate {
    cache {
        queries = false
        use_second_level_cache = false
        use_query_cache = false
    }
    dialect = 'org.hibernate.dialect.PostgreSQLDialect'
}

dataSource {
    pooled = true
    jmxExport = true
    driverClassName = 'org.postgresql.Driver'
}

environments {
    development {
        dataSource {
            dbCreate = 'none'
            url = 'jdbc:postgresql://localhost:5432/alice-dev'
            username = 'postgres'
            password = 'postgres'
        }
    }
    test {
        dataSource {
            dbCreate = 'create-drop'
            url = 'jdbc:postgresql://localhost:5432/alice-test'
            username = 'postgres'
            password = 'postgres'
        }
    }
    production {
        dataSource {
            dbCreate = 'none'
            url = System.getenv('DATASOURCE_URL')
            username = System.getenv('DATASOURCE_USERNAME')
            password = System.getenv('DATASOURCE_PASSWORD')
        }
    }
}
