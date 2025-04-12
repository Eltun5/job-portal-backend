package org.ea.jobportalbackend.loader;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.ea.jobportalbackend.dto.LoadJobDTO;
import org.ea.jobportalbackend.service.JobService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JobLoader {

    private final DjinniJobScraper scraper;
    private final JobService jobService;

    public JobLoader(DjinniJobScraper scraper, JobService jobService) {
        this.scraper = scraper;
        this.jobService = jobService;
    }

    @PostConstruct
    public void loadJobsOnStartup() {
        List<LoadJobDTO> loadJobDTOS = scraper.scrapeJobs();

        if(!(jobService.findAll().size()>1)) jobService.create(loadJobDTOS);
    }
}
