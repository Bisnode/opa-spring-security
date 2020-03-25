package com.bisnode.opa.spring.security.filter.decision.request

import com.bisnode.opa.client.query.OpaQueryApi
import com.bisnode.opa.client.query.QueryForDocumentRequest
import com.bisnode.opa.spring.security.filter.decision.AccessDecision
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class ByRequestDeciderSpec extends Specification {

    def 'should user given supplier to make request to OPA'() {
        given:
            OpaQueryApi opaQueryApi = Mock()
            OpaInput opaInput = OpaInput.builder().build()
            AccessDecision expectedAccessDecision = new AccessDecision()
            OpaRequestSupplier opaRequestSupplier = Mock()
            ByRequestDecider decider = new ByRequestDecider(opaQueryApi, SOME_DOCUMENT_PATH, opaRequestSupplier)

        when:
            AccessDecision actualAccessDecision = decider.decideBy(Mock(HttpServletRequest))

        then:
            1 * opaRequestSupplier.get(_ as HttpServletRequest) >> opaInput
            1 * opaQueryApi.queryForDocument(new QueryForDocumentRequest(opaInput, SOME_DOCUMENT_PATH), AccessDecision) >> expectedAccessDecision
            expectedAccessDecision == actualAccessDecision
    }

    static final String SOME_DOCUMENT_PATH = 'some/policy'

}
