package org.ea.jobportalbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.ea.jobportalbackend.dto.JobUpdateDTO;
import org.ea.jobportalbackend.mapper.JobMapper;
import org.ea.jobportalbackend.model.Job;
import org.ea.jobportalbackend.model.Tag;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;
import org.ea.jobportalbackend.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class JobService {
    private final JobRepository repository;

    private final TagService tagService;

    private final JobMapper jobMapper;

    private final Comparator<Job> compareJobsByPostedDate = Comparator.comparing(Job::getPostedDate);

    private final Comparator<Job> compareJobsByMinSalary = Comparator.comparing(Job::getMinSalary);

    private final Comparator<Job> compareJobsByMaxSalary = Comparator.comparing(Job::getMaxSalary);

    public JobService(JobRepository repository, TagService tagService, JobMapper jobMapper) {
        this.repository = repository;
        this.tagService = tagService;
        this.jobMapper = jobMapper;
    }

    public void create(Job job) {
        repository.save(job);
    }

    public List<Job> findAll() {
        return repository.findAll();
    }

    public Job findById(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Cannot find a job with this id.");
            return new NoSuchElementException("Cannot find a job with this id.");
        });
    }

    public void update(Long id, JobUpdateDTO dto) {
        Job oldJob = findById(id);

        jobMapper.updateJobFromDto(dto, oldJob);

        if (dto.getTagIds() != null) {
            List<Tag> tags = tagService.findAllById(dto.getTagIds());
            oldJob.setTags(tags);
        }

        repository.save(oldJob);
    }

    public void delete(Job job){
        log.warn("Someone try to delete job!!!");
        repository.delete(job);
    }


    public List<String> getDistinctLocations() {
        return repository.getDistinctLocations();
    }

    public List<String> getDistinctIndustries() {
        return repository.getDistinctIndustries();
    }

    public List<Job> getJobsWithFiltersAndSort(List<String> locations,
                                               List<JobType> jobTypes,
                                               List<ExperienceLevel> experienceLevels,
                                               List<String> industries,
                                               List<Tag> tags,
                                               boolean sortByPostDate,
                                               boolean sortByMinSalary,
                                               boolean sortByMaxSalary,
                                               boolean sortInDescendingOrder) {
        if (locations.isEmpty() &&
            jobTypes.isEmpty() &&
            experienceLevels.isEmpty() &&
            industries.isEmpty() &&
            tags.isEmpty()) {
            return sortListOfJob(findAll(),
                    sortByPostDate,
                    sortByMinSalary,
                    sortByMaxSalary,
                    sortInDescendingOrder);
        }

        if (locations.isEmpty()) locations = getDistinctLocations();

        if (jobTypes.isEmpty()) jobTypes = List.of(JobType.values());

        if (experienceLevels.isEmpty()) experienceLevels = List.of(ExperienceLevel.values());

        if (industries.isEmpty()) industries = getDistinctIndustries();

        if (tags.isEmpty()) tags = tagService.findAll();

        List<Job> jobsFiltered = repository.
                getJobsFiltered(locations, jobTypes, experienceLevels, industries, tags);

        return sortListOfJob(jobsFiltered,
                sortByPostDate,
                sortByMinSalary,
                sortByMaxSalary,
                sortInDescendingOrder);
    }

    private List<Job> sortListOfJob(List<Job> listOfJob,
                                    boolean sortByPostDate,
                                    boolean sortByMinSalary,
                                    boolean sortByMaxSalary,
                                    boolean sortInDescendingOrder) {

        if (sortByPostDate) listOfJob.sort(compareJobsByPostedDate);

        if (sortByMinSalary) listOfJob.sort(compareJobsByMinSalary);

        if (sortByMaxSalary) listOfJob.sort(compareJobsByMaxSalary);

        if (sortInDescendingOrder) return listOfJob.reversed();

        return listOfJob;
    }
}
