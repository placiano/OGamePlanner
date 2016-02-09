package com.skiwi.ogameplanner.objects

import spock.lang.Specification

/**
 * @author Frank van Heeswijk
 */
class BasicBuildOrderTest extends Specification {
    void "test constructor"() {
        expect:
        new BasicBuildOrder()
    }

    void "test constructor from list"() {
        expect:
        new BasicBuildOrder([Reqs.A, Reqs.B, Reqs.C, Reqs.D]).asList() == [Reqs.A, Reqs.B, Reqs.C, Reqs.D]
    }

    void "test constructor from requirement graph"() {
        expect:
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.A))
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.B))
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.C))
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.D))
    }

    void "test save and restore state without modifications"() {
        given:
        def buildOrder = new BasicBuildOrder([Reqs.A, Reqs.B])

        when: "state is saved and restored"
        buildOrder.saveState()
        buildOrder.restoreState()

        then: "build order should be the same"
        buildOrder.asList() == [Reqs.A, Reqs.B]
    }

    void "test restore state without saving"() {
        given:
        def buildOrder = new BasicBuildOrder([Reqs.A, Reqs.B])

        when: "state is restored without saving"
        buildOrder.restoreState()

        then: "an exception should be thrown"
        thrown(IllegalStateException)
    }

    void "test restore state after adding"() {
        given:
        def buildOrder = new BasicBuildOrder([Reqs.A, Reqs.B])
        buildOrder.saveState()
        buildOrder.add(Reqs.C)

        when: "state is restored"
        buildOrder.restoreState()

        then: "build order should be reset to before save state"
        buildOrder.asList() == [Reqs.A, Reqs.B]
    }

    void "test restore state after moving"() {
        given:
        def buildOrder = new BasicBuildOrder([Reqs.A, Reqs.B, Reqs.C, Reqs.D])
        buildOrder.saveState()
        buildOrder.moveBy(0, 2)

        when: "state is restored"
        buildOrder.restoreState()

        then: "build order should be reset to before save state"
        buildOrder.asList() == [Reqs.A, Reqs.B, Reqs.C, Reqs.D]
    }

    void "test restore state after adding and moving"() {
        given:
        def buildOrder = new BasicBuildOrder([Reqs.A, Reqs.B])
        buildOrder.saveState()
        buildOrder.add(Reqs.C)
        buildOrder.moveBy(0, 2)

        when: "state is restored"
        buildOrder.restoreState()

        then: "build order should be reset to before save state"
        buildOrder.asList() == [Reqs.A, Reqs.B]
    }

    void "test add"() {
        given:
        def buildOrder = new BasicBuildOrder()

        when: "adding first element"
        buildOrder.add(Reqs.A)

        then: "element should be added at the end"
        buildOrder.asList() == [Reqs.A]

        when: "adding second element"
        buildOrder.add(Reqs.B)

        then: "element should be added at the end"
        buildOrder.asList() == [Reqs.A, Reqs.B]
    }

    void "test add with index"() {
        given:
        def buildOrder = new BasicBuildOrder()

        when: "adding first element at the end"
        buildOrder.add(0, Reqs.A)

        then: "element should be added at the end"
        buildOrder.asList() == [Reqs.A]

        when: "adding second element at the start"
        buildOrder.add(0, Reqs.B)

        then: "element should be added at the start"
        buildOrder.asList() == [Reqs.B, Reqs.A]

        when: "adding third element at the end"
        buildOrder.add(2, Reqs.C)

        then: "element should be added at the end"
        buildOrder.asList() == [Reqs.B, Reqs.A, Reqs.C]

        when: "adding fourth element at index 1"
        buildOrder.add(1, Reqs.D)

        then: "element should be added at index 1"
        buildOrder.asList() == [Reqs.B, Reqs.D, Reqs.A, Reqs.C]
    }

    void "test move by"() {
        given:
        def buildOrder = new BasicBuildOrder([Reqs.A, Reqs.B, Reqs.C, Reqs.D])

        when: "moving an item by zero positions"
        buildOrder.moveBy(0, 0)

        then: "build order should not be modified"
        buildOrder.asList() == [Reqs.A, Reqs.B, Reqs.C, Reqs.D]

        when: "moving second item by two positions forward"
        buildOrder.moveBy(1, 2)

        then: "build order should be modified such that second item is moved two positions forwards"
        buildOrder.asList() == [Reqs.A, Reqs.C, Reqs.D, Reqs.B]

        when: "moving third item by one position backward"
        buildOrder.moveBy(2, -1)

        then: "build order should be modified such that third item is moved one position backwards"
        buildOrder.asList() == [Reqs.A, Reqs.D, Reqs.C, Reqs.B]
    }

    void "test get"() {
        given:
        def buildOrder = new BasicBuildOrder([Reqs.A, Reqs.B, Reqs.C, Reqs.D])

        expect:
        buildOrder.get(0) == Reqs.A
        buildOrder.get(1) == Reqs.B
        buildOrder.get(2) == Reqs.C
        buildOrder.get(3) == Reqs.D
    }

    void "test size"() {
        expect:
        new BasicBuildOrder([Reqs.A, Reqs.B, Reqs.C]).size() == 3
    }

    void "test iterator"() {
        expect:
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.A)).asList() == []
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.B)).asList() == [Reqs.A, Reqs.A, Reqs.A]
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.C)).asList() == [Reqs.A, Reqs.A]
        new BasicBuildOrder(new BasicRequirementGraph(Reqs.D)).asList() in [
            [Reqs.A, Reqs.A, Reqs.A, Reqs.C, Reqs.B, Reqs.B, Reqs.B, Reqs.B, Reqs.B],
            [Reqs.A, Reqs.A, Reqs.C, Reqs.A, Reqs.B, Reqs.B, Reqs.B, Reqs.B, Reqs.B],
            [Reqs.A, Reqs.A, Reqs.A, Reqs.B, Reqs.B, Reqs.B, Reqs.B, Reqs.B, Reqs.C],
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
