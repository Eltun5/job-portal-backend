package org.ea.jobportalbackend.repository;

import org.ea.jobportalbackend.model.Job;
import org.ea.jobportalbackend.model.Tag;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("select distinct j.location from jobs j")
    List<String> getDistinctLocation();

    @Query("select distinct j.industry from jobs j")
    List<String> getDistinctIndustry();

    @Query("""
           from jobs where
           lower(location) in :locations and
           lower(jobType) in :jobTypes and
           lower(experienceLevel) in :experienceLevels and
           lower(industry) in :industries and
           lower(tags) in :tags
           """)
    List<Job> getJobsByLocationAndJobTypeAndExperienceLevelAndIndustryAndTags(
            List<String> locations, List<JobType> jobTypes,
            List<ExperienceLevel> experienceLevels, List<String> industries,
            List<Tag> tags);


}
