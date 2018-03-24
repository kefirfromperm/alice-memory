package functional

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.grails.io.support.ClassPathResource
import org.springframework.http.converter.StringHttpMessageConverter
import spock.lang.Shared
import spock.lang.Specification

import java.nio.charset.StandardCharsets

@Integration
@Rollback
class FullDialogSpec extends Specification {
    @Shared RestBuilder rest = new RestBuilder()

    void setup(){
        rest.restTemplate.setMessageConverters([new StringHttpMessageConverter(StandardCharsets.UTF_8)])
    }

    void 'full dialog'(){
        when:'init session'
            RestResponse response = rest.post("http://localhost:${serverPort}/"){
                json(readResource('init-request'))
            }
        then:'session is initialized and user has tips'
            response.statusCode.is2xxSuccessful()

            response.json.response.text == 'Я могу запомнить и напомнить.'
            response.json.response.end_session == false

            response.json.session.session_id == '2eac4854-fce721f3-b845abba-20d60'
            response.json.session.user_id == 'AC9WC3DF6FCE052E45A4566A48E6B7193774B84814CE49A922E163B8B29881DC'
            response.json.session.message_id == 4

            response.json.version == '1.0'
        when:'remember it'
            response = rest.post("http://localhost:${serverPort}/"){
                json(readResource('remember-request'))
            }
        then:'remember'
            response.statusCode.is2xxSuccessful()

            response.json.response.text == 'Запомнила.'
            response.json.response.end_session == false

            response.json.session.session_id == '2eac4854-fce721f3-b845abba-20d60'
            response.json.session.user_id == 'AC9WC3DF6FCE052E45A4566A48E6B7193774B84814CE49A922E163B8B29881DC'
            response.json.session.message_id == 5

            response.json.version == '1.0'
        when:'recall'
            response = rest.post("http://localhost:${serverPort}/"){
                json(readResource('remind-request'))
            }
        then:'recall'
            response.statusCode.is2xxSuccessful()

            response.json.response.text == 'Завтра мне к врачу'
            response.json.response.end_session == false

            response.json.session.session_id == '2eac4854-fce721f3-b845abba-20d60'
            response.json.session.user_id == 'AC9WC3DF6FCE052E45A4566A48E6B7193774B84814CE49A922E163B8B29881DC'
            response.json.session.message_id == 6

            response.json.version == '1.0'
    }

    private static String readResource(String name) {
        return new ClassPathResource('/functional/' + name + '.json').inputStream.text
    }
}
