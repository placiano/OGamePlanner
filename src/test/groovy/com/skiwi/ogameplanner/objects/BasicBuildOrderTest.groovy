package com.skiwi.ogameplanner.objects

import spock.lang.Specification

/**
 * @author Frank van Heeswijk
 */
class BasicBuildOrderTest extends Specification {
    void "test constructor"() {
        expect:
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.A))
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.B))
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.C))
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.D))
    }

    void "test iterator"() {
        expect:
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.A)).asList() == []
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.B)).asList() == [Reqs.A, Reqs.A, Reqs.A]
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.C)).asList() == [Reqs.A, Reqs.A]
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.D)).asList() == [Reqs.A, Reqs.A, Reqs.A, Reqs.C, Reqs.B, Reqs.B, Reqs.B, Reqs.B, Reqs.B]
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
