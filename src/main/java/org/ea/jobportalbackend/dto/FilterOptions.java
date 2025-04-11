package org.ea.jobportalbackend.dto;

import org.ea.jobportalbackend.model.Tag;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;

import java.util.List;

public record FilterOptions(List<String> locations,
                            List<JobType> jobTypes,
                            List<ExperienceLevel> experienceLevels,
                            List<String> industries,
                            List<Tag> tags) {
}
