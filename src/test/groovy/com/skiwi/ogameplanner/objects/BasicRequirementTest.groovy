package com.skiwi.ogameplanner.objects

import spock.lang.Specification

/**
 * @author Frank van Heeswijk
 */
class BasicRequirementTest extends Specification {
    void "test equals"() {
        expect:
        new BasicRequirement(Reqs.A, 1) == new BasicRequirement(Reqs.A, 1)
    }

    void "test hash code"() {
        expect:
        new BasicRequirement(Reqs.A, 1).hashCode() == new BasicRequirement(Reqs.A, 1).hashCode()
    }

    enum Reqs implements GameObject { A
        @Override
        Requirement[] getRequirements() {
            return new Requirement[0]
        }
    }
}
