package com.skiwi.ogameplanner.main;

import com.skiwi.ogameplanner.objects.BasicBuildOrder;
import com.skiwi.ogameplanner.objects.BasicRequirementGraph;
import com.skiwi.ogameplanner.objects.BuildOrder;
import com.skiwi.ogameplanner.objects.RequirementGraph;

import static com.skiwi.ogameplanner.ogame.Ship.SMALL_CARGO;

/**
 * @author Frank van Heeswijk
 */
public class OGamePlanner {
    private void run() {
        RequirementGraph requirementGraph = new BasicRequirementGraph(SMALL_CARGO);
        BuildOrder buildOrder = new BasicBuildOrder(requirementGraph);
        System.out.println("buildOrder = " + buildOrder);
    }

    public static void main(String[] args) {
        new OGamePlanner().run();
    }
}
