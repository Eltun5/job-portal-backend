package org.ea.jobportalbackend.loader;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.ea.jobportalbackend.dto.JobDTO;
import org.ea.jobportalbackend.dto.LoadJobDTO;
import org.ea.jobportalbackend.model.enums.EducationLevel;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class DjinniJobScraper {

    public List<LoadJobDTO> scrapeJobs() {
        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();


        List<LoadJobDTO> loadJobDTOS = new ArrayList<>();

        try {
            driver.get("https://djinni.co/jobs/?employment=remote&region=other&country=AZE");

            List<WebElement> jobElements = driver.findElements(By.cssSelector("li.mb-4"));

            for (WebElement jobElement : jobElements) {
                try {
                    String title = jobElement.findElement(By.cssSelector("a.job-item__title-link")).getText();
                    String company = jobElement.findElement(By.cssSelector("a[data-analytics='company_page']")).getText();
                    String location = jobElement.findElement(By.cssSelector(".location-text")).getText();
                    String jobType = jobElement.findElement(By.cssSelector("div.fw-medium span.text-nowrap")).getText();
                    String jobLink = jobElement.findElement(By.cssSelector("a.job-item__title-link")).getAttribute("href");
                    String description = jobElement.findElement(By.cssSelector(".js-truncated-text")).getText();

                    WebElement timeElement = jobElement.findElement(By.cssSelector(".text-nowrap[title][data-original-title]"));
                    String postedAt = timeElement.getAttribute("data-original-title");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
                    LocalDate postdate = null;

                    if (postedAt != null) {
                        postdate = LocalDate.parse(postedAt, formatter);
                    }

                    String logoUrl = "https://ui-avatars.com/api/?name=User&size=100&background=cccccc&color=ffffff";
                    List<WebElement> logoElements = jobElement.findElements(By.cssSelector("img.userpic-image"));
                    if (!logoElements.isEmpty()) {
                        String src = logoElements.getFirst().getAttribute("src");
                        if (src != null && !src.trim().isEmpty()) {
                            logoUrl = src;
                        }
                    }

                    log.info("✅ [{}] {} at {} | Location: {} | Link: {} | Deadline: {}",
                            jobType,
                            title,
                            company,
                            location,
                            jobLink,
                            postdate != null ? postdate.plusMonths(1) : "N/A"
                    );

                    JobDTO dto = new JobDTO(
                            title,
                            company,
                            location,
                            "Full Remote".equals(jobType) ? JobType.REMOTE : JobType.FULL_TIME,
                            new BigDecimal("0"),
                            new BigDecimal("0"),
                            description,
                            List.of(),
                            ExperienceLevel.MID,
                            EducationLevel.BACHELORS,
                            "Technology",
                            postdate != null ? postdate.plusMonths(1) : LocalDate.now().plusMonths(1),
                            jobLink,
                            List.of(),
                            List.of(),
                            jobLink
                    );
                    loadJobDTOS.add(new LoadJobDTO(dto, logoUrl));

                } catch (NoSuchElementException e) {
                    System.err.println("Incomplete job card, skipping...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return loadJobDTOS;
    }
}