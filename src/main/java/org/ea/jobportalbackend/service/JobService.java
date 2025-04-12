package org.ea.jobportalbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.ea.jobportalbackend.dto.*;
import org.ea.jobportalbackend.mapper.JobMapper;
import org.ea.jobportalbackend.model.Job;
import org.ea.jobportalbackend.model.enums.EducationLevel;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;
import org.ea.jobportalbackend.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Slf4j
@Service
public class JobService {
    private final JobRepository repository;

    private final TagService tagService;

    private final JobMapper jobMapper;

    public JobService(JobRepository repository, TagService tagService, JobMapper jobMapper) {
        this.repository = repository;
        this.tagService = tagService;
        this.jobMapper = jobMapper;
    }

    public Job create(JobDTO dto, MultipartFile logo) throws IOException, URISyntaxException {
        Job job = jobMapper.toEntity(dto);

        String defaultLogoURl =
                "https://ui-avatars.com/api/?name=User&size=100&background=cccccc&color=ffffff";

        if (logo != null && !logo.isEmpty())
            job.setCompanyLogo(addLogo(logo));
        else job.setCompanyLogo(downloadAndSaveLogoFromUrl(defaultLogoURl));
        repository.save(job);

        return repository.getJobByJobTitle(dto.jobTitle()).orElseThrow(() -> {
            log.error("Cannot find a job with this id.");
            return new NoSuchElementException("Cannot find a job with this id.");
        });
    }

    public void create(List<LoadJobDTO> loadJobDTOS) {
        List<String> logoNames = new ArrayList<>();
        loadJobDTOS.stream().map(LoadJobDTO::logoUrl).forEach(url -> {
            try {
                logoNames.add(downloadAndSaveLogoFromUrl(url));
            } catch (IOException | URISyntaxException e) {
                log.error("Something happen and logo cannot download: " + e);
                logoNames.add("");
                throw new RuntimeException(e);
            }
        });

        List<Job> jobs = new ArrayList<>();

        for (int i = 0; i < loadJobDTOS.size(); i++) {
            Job job = jobMapper.toEntity(loadJobDTOS.get(i).dto());
            job.setPostedDate(job.getApplicationDeadline().minusMonths(1));
            job.setCompanyLogo(logoNames.get(i));

            jobs.add(job);
        }

        repository.saveAll(jobs);
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

    public Job update(Long id, JobDTO dto, MultipartFile logo) throws IOException {
        Job oldJob = findById(id);

        if (logo == null || isSameContent(logo, oldJob.getCompanyLogo())) {
            jobMapper.updateJobFromDto(dto, oldJob);
            repository.save(oldJob);
        } else if (!logo.isEmpty()) {
            jobMapper.updateJobFromDto(dto, oldJob);
            oldJob.setCompanyLogo(addLogo(logo));

            repository.save(oldJob);
        } else {
            Path filePath = Paths.get("uploads", oldJob.getCompanyLogo());
            Files.deleteIfExists(filePath);

            jobMapper.updateJobFromDto(dto, oldJob);
            oldJob.setCompanyLogo(addLogo(logo));

            repository.save(oldJob);
        }
        return findById(id);
    }

    public void delete(Long id) {
        log.warn("Someone try to delete job!!!");
        repository.delete(findById(id));
    }

    public Map<String, Object> getFormData() {
        return Map.of("JobType", JobType.values(),
                "ExperienceLevel", ExperienceLevel.values(),
                "EducationLevel", EducationLevel.values());
    }

    public FilterOptions getAllFilterRequirements() {
        return new FilterOptions(
                getDistinctLocations(),
                List.of(JobType.values()),
                List.of(ExperienceLevel.values()),
                getDistinctIndustries(),
                tagService.findAll());
    }

    public List<String> getDistinctLocations() {
        return repository.getDistinctLocations();
    }

    public List<String> getDistinctIndustries() {
        return repository.getDistinctIndustries();
    }

    public JobResponseDTO getAllOrFilteredData(List<String> locations,
                                               List<JobType> jobTypes,
                                               List<ExperienceLevel> experienceLevels,
                                               List<String> industries,
                                               List<Long> tagIds,
                                               boolean sortByPostDate,
                                               boolean sortByMinSalary,
                                               boolean sortByMaxSalary,
                                               boolean sortInDescendingOrder) {
        List<Job> jobsWithFiltersAndSort = getJobsWithFiltersAndSort(
                locations, jobTypes,
                experienceLevels, industries, tagIds,
                sortByPostDate, sortByMinSalary, sortByMaxSalary,
                sortInDescendingOrder);

        SelectedFilters selectedFilters = new SelectedFilters(locations,
                jobTypes, experienceLevels, industries,
                tagIds, sortByPostDate, sortByMinSalary,
                sortByMaxSalary, sortInDescendingOrder);

        return new JobResponseDTO(jobsWithFiltersAndSort,
                getAllFilterRequirements(), selectedFilters);
    }

    public List<Job> getJobsWithFiltersAndSort(List<String> locations,
                                               List<JobType> jobTypes,
                                               List<ExperienceLevel> experienceLevels,
                                               List<String> industries,
                                               List<Long> tagIds,
                                               boolean sortByPostDate,
                                               boolean sortByMinSalary,
                                               boolean sortByMaxSalary,
                                               boolean sortInDescendingOrder) {
        if ((locations == null || locations.isEmpty()) &&
            (jobTypes == null || jobTypes.isEmpty()) &&
            (experienceLevels == null || experienceLevels.isEmpty()) &&
            (industries == null || industries.isEmpty()) &&
            (tagIds == null || tagIds.isEmpty())) {

            return sortListOfJob(findAll(),
                    sortByPostDate,
                    sortByMinSalary,
                    sortByMaxSalary,
                    sortInDescendingOrder);
        }

        if (locations == null || locations.isEmpty()) locations = getDistinctLocations();

        if (jobTypes == null || jobTypes.isEmpty()) jobTypes = List.of(JobType.values());

        if (experienceLevels == null || experienceLevels.isEmpty())
            experienceLevels = List.of(ExperienceLevel.values());

        if (industries == null || industries.isEmpty()) industries = getDistinctIndustries();

        if (tagIds == null || tagIds.isEmpty()) {
            List<Job> jobsFilteredWithoutTags =
                    repository.getJobsFilteredWithoutTags(locations, jobTypes,
                            experienceLevels, industries);

            return sortListOfJob(jobsFilteredWithoutTags,
                    sortByPostDate,
                    sortByMinSalary,
                    sortByMaxSalary,
                    sortInDescendingOrder);
        }

        List<Job> jobsFiltered = repository.
                getJobsFiltered(locations, jobTypes, experienceLevels, industries, tagIds);

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

        Comparator<Job> comparator = Comparator.comparing(Job::getId);

        if (sortByPostDate) comparator = Comparator.comparing(Job::getPostedDate);
        if (sortByMinSalary) comparator = Comparator.comparing(Job::getMinSalary);
        if (sortByMaxSalary) comparator = Comparator.comparing(Job::getMaxSalary);

        listOfJob.sort(comparator);

        if (sortInDescendingOrder) return listOfJob.reversed();

        return listOfJob;
    }

    public String addLogo(MultipartFile logo) throws IOException {
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String fileName = UUID.randomUUID() + "_" + logo.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);

        Files.copy(logo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public String downloadAndSaveLogoFromUrl(String imageUrl) throws IOException, URISyntaxException {
        URI uri = new URI(imageUrl);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (InputStream in = connection.getInputStream()) {
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fileExtension = imageUrl.substring(imageUrl.lastIndexOf('.'));
            if (fileExtension.contains("?") ||
                fileExtension.contains("$") ||
                fileExtension.contains("=")) {
                fileExtension = "Default_Name.png";
            }
            String fileName = UUID.randomUUID() + "_" + fileExtension;
            Path filePath = uploadDir.resolve(fileName);

            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } finally {
            connection.disconnect();
        }
    }

    public boolean isSameContent(MultipartFile multipartFile, String existingFileName) throws IOException {
        byte[] uploadedBytes = multipartFile.getBytes();

        Path existingFilePath = Paths.get("uploads", existingFileName);
        byte[] existingBytes = Files.readAllBytes(existingFilePath);

        return Arrays.equals(uploadedBytes, existingBytes);
    }
}
