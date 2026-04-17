package com.portfolio.service;

import com.portfolio.model.Portfolio;
import com.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public Portfolio getPortfolio() {
        return portfolioRepository.findTopByOrderByIdDesc();
    }

    public Portfolio savePortfolio(Portfolio portfolio,
                                   MultipartFile photo,
                                   MultipartFile resume) throws IOException {

        // Create uploads directory if not exists
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Handle photo upload
        if (photo != null && !photo.isEmpty()) {
            String photoFilename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            Path photoPath = Paths.get(UPLOAD_DIR + photoFilename);
            Files.copy(photo.getInputStream(), photoPath, StandardCopyOption.REPLACE_EXISTING);
            portfolio.setPhotoPath("/uploads/" + photoFilename);
        }

        // Handle resume upload
        if (resume != null && !resume.isEmpty()) {
            String resumeFilename = UUID.randomUUID() + "_" + resume.getOriginalFilename();
            Path resumePath = Paths.get(UPLOAD_DIR + resumeFilename);
            Files.copy(resume.getInputStream(), resumePath, StandardCopyOption.REPLACE_EXISTING);
            portfolio.setResumePath("/uploads/" + resumeFilename);
        }

        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(Long id) {
        portfolioRepository.deleteById(id);
    }
}
