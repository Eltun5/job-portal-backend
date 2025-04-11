package org.ea.jobportalbackend.repository;

import org.ea.jobportalbackend.model.Job;
import org.ea.jobportalbackend.model.Tag;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> getJobByJobTitle(String title);

    @Query("select distinct j.location from jobs j")
    List<String> getDistinctLocations();

    @Query("select distinct j.industry from jobs j")
    List<String> getDistinctIndustries();

    @Query("""
            from jobs j where
            j.location in :locations and
            j.jobType in :jobTypes and
            j.experienceLevel in :experienceLevels and
            j.industry in :industries and
            exists (select t from j.tags t where t.id in :tags)
            """)
    List<Job> getJobsFiltered(List<String> locations,
                              List<JobType> jobTypes,
                              List<ExperienceLevel> experienceLevels,
                              List<String> industries,
                              List<Long> tags);

    @Query("""
            from jobs j where
            j.location in :locations and
            j.jobType in :jobTypes and
            j.experienceLevel in :experienceLevels and
            j.industry in :industries
            """)
    List<Job> getJobsFilteredWithoutTags(List<String> locations,
                                         List<JobType> jobTypes,
                                         List<ExperienceLevel> experienceLevels,
                                         List<String> industries);

}
