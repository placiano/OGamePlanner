package com.skiwi.ogameplanner.objects;

import java.util.List;

/**
 * @author Frank van Heeswijk
 */
public interface RequirementGraph {
    List<Requirement> getTopologicallySortedRequirements();
}
