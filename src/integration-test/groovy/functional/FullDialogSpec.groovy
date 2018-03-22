package functional

import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class FullDialogSpec extends Specification {
    @Shared RestBuilder rest = new RestBuilder()
}
