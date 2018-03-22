package functional

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.grails.io.support.ClassPathResource
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class FullDialogSpec extends Specification {
    @Shared RestBuilder rest = new RestBuilder()

    void 'full dialog'(){
        when:'init session'
            RestResponse response = rest.post("http://localhost:${serverPort}/"){
                json(readResource('init-request'))
            }
        then:'session is initialized and user has tips'
            response.statusCode.is2xxSuccessful()
        when:'remember it'
            response = rest.post("http://localhost:${serverPort}/"){
                json(readResource('remember-request'))
            }
        then:'remember'
            response.statusCode.is2xxSuccessful()
        when:'recall'
            response = rest.post("http://localhost:${serverPort}/"){
                json(readResource('recall-request'))
            }
        then:'recall'
            response.statusCode.is2xxSuccessful()
    }

    private static String readResource(String name) {
        return new ClassPathResource('/functional/' + name + '.json').inputStream.text
    }
}
