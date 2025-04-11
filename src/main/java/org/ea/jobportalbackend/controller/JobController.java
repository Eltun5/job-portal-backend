package org.ea.jobportalbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.ea.jobportalbackend.dto.JobDTO;
import org.ea.jobportalbackend.dto.JobResponseDTO;
import org.ea.jobportalbackend.model.Job;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;
import org.ea.jobportalbackend.service.JobService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/jobs")
public class JobController {
    private final JobService service;

    public JobController(JobService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Job> create(@ModelAttribute JobDTO dto,
                                      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.create(dto, file));
    }

    @GetMapping
    public ResponseEntity<JobResponseDTO> get(
            @RequestParam(required = false) List<String> locations,
            @RequestParam(required = false) List<JobType> jobTypes,
            @RequestParam(required = false) List<ExperienceLevel> experienceLevels,
            @RequestParam(required = false) List<String> industries,
            @RequestParam(required = false) List<Long> tags,
            @RequestParam(defaultValue = "false") boolean sortByPostDate,
            @RequestParam(defaultValue = "false") boolean sortByMinSalary,
            @RequestParam(defaultValue = "false") boolean sortByMaxSalary,
            @RequestParam(defaultValue = "false") boolean sortInDescendingOrder) {

        log.error("\nlocations " + locations + "\n" +
                  "jobTypes " + jobTypes + "\n" +
                  "experienceLevels " + experienceLevels + "\n" +
                  "industries " + industries + "\n" +
                  "tags " + tags + "\n" +
                  "sortByPostDate " + sortByPostDate + "\n" +
                  "sortByMinSalary " + sortByMinSalary + "\n" +
                  "sortByMaxSalary " + sortByMaxSalary + "\n" +
                  "sortInDescendingOrder " + sortInDescendingOrder
        );
        JobResponseDTO allOrFilteredData = service.getAllOrFilteredData(locations, jobTypes,
                experienceLevels, industries, tags, sortByPostDate,
                sortByMinSalary, sortByMaxSalary, sortInDescendingOrder);

        return ResponseEntity.ok(allOrFilteredData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/form-data")
    public ResponseEntity<Map<String, Object>> getJobFormData() {
        return ResponseEntity.ok(service.getFormData());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Job> update(@PathVariable Long id,
                                      @ModelAttribute JobDTO dto,
                                      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.update(id, dto, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
