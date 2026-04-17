package com.portfolio;

import com.portfolio.model.Portfolio;
import com.portfolio.repository.PortfolioRepository;
import com.portfolio.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PortfolioApplicationTests {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Test
    void contextLoads() {
        // Verifies Spring context loads correctly
    }

    @Test
    void shouldSaveAndRetrievePortfolio() {
        Portfolio p = Portfolio.builder()
                .fullName("Test User")
                .email("test@example.com")
                .collegeName("Test College")
                .cgpa(8.5)
                .skills("Java, Spring Boot")
                .build();

        Portfolio saved = portfolioRepository.save(p);
        assertNotNull(saved.getId());
        assertEquals("Test User", saved.getFullName());
        assertEquals(8.5, saved.getCgpa());
    }

    @Test
    void shouldReturnLatestPortfolio() {
        portfolioRepository.deleteAll();

        Portfolio p1 = Portfolio.builder().fullName("User 1").email("u1@test.com").collegeName("College A").build();
        Portfolio p2 = Portfolio.builder().fullName("User 2").email("u2@test.com").collegeName("College B").build();
        portfolioRepository.save(p1);
        portfolioRepository.save(p2);

        Portfolio latest = portfolioService.getPortfolio();
        assertNotNull(latest);
        assertEquals("User 2", latest.getFullName());
    }
}
