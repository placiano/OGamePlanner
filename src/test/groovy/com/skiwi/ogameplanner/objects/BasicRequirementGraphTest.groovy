package com.skiwi.ogameplanner.objects

import spock.lang.Specification

/**
 * @author Frank van Heeswijk
 */
class BasicRequirementGraphTest extends Specification {
    void "test constructor"() {
        expect:
        new BasicRequirementGraph(Reqs.A)
        new BasicRequirementGraph(Reqs.B)
        new BasicRequirementGraph(Reqs.C)
        new BasicRequirementGraph(Reqs.D)
    }

    void "test get topologically sorted requirements"() {
        expect:
        new BasicRequirementGraph(Reqs.A).topologicallySortedRequirements == []
        new BasicRequirementGraph(Reqs.B).topologicallySortedRequirements == [new BasicRequirement(Reqs.A, 3)]
        new BasicRequirementGraph(Reqs.C).topologicallySortedRequirements == [new BasicRequirement(Reqs.A, 2)]
        new BasicRequirementGraph(Reqs.D).topologicallySortedRequirements in [
            [new BasicRequirement(Reqs.A, 2), new BasicRequirement(Reqs.C, 1), new BasicRequirement(Reqs.A, 3), new BasicRequirement(Reqs.B, 5)],
            [new BasicRequirement(Reqs.A, 2), new BasicRequirement(Reqs.A, 3), new BasicRequirement(Reqs.C, 1), new BasicRequirement(Reqs.B, 5)],
            [new BasicRequirement(Reqs.A, 2), new BasicRequirement(Reqs.A, 3), new BasicRequirement(Reqs.B, 5), new BasicRequirement(Reqs.C, 1)]
        ]
    }

    enum Reqs implements GameObject {
        A {
            @Override
            Requirement[] getRequirements() {
                return new Requirement[0]
            }
        },
        B {
            @Override
            Requirement[] getRequirements() {
                return Arrays.asList(new BasicRequirement(A, 3)) as Requirement[]
            }
        },
        C {
            @Override
            Requirement[] getRequirements() {
                return Arrays.asList(new BasicRequirement(A, 2)) as Requirement[]
            }
        },
        D {
            @Override
            Requirement[] getRequirements() {
                return Arrays.asList(new BasicRequirement(B, 5), new BasicRequirement(C, 1)) as Requirement[]
            }
        };

        @Override
        Requirement[] getRequirements() {
            throw new UnsupportedOperationException()
        }
    }
}
