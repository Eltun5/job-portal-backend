package org.ea.jobportalbackend.dto;

import org.ea.jobportalbackend.model.Tag;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;

import java.util.List;

public record SelectedFilters(List<String> locations,
                              List<JobType> jobTypes,
                              List<ExperienceLevel> experienceLevels,
                              List<String> industries,
                              List<Long> tags,
                              boolean sortByPostDate,
                              boolean sortByMinSalary,
                              boolean sortByMaxSalary,
                              boolean sortInDescendingOrder) {
}
