package com.primefit.tool.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.primefit.tool.model.Diet;
import com.primefit.tool.service.dietservice.DietService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diets")
@AllArgsConstructor
public class DietController {

    private DietService dietService;

    public String uploadPdfAsUrl(MultipartFile storedPdf, String bucketName, Diet diet) {

        File pdf;
        try {
            pdf = dietService.convertMultiPartToFile(storedPdf);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unable to convert input stream to file");
        }

        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion("eu-central-1").build();
        TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3client).build();
        String fileName = "Training-pdf-for-" + diet.getName() + "-" + new Timestamp(System.currentTimeMillis()) + ".pdf";
        fileName = fileName.replaceAll(" ", "-");
        try {
            Upload xfer = xfer_mgr.upload(new PutObjectRequest(bucketName, fileName, pdf)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            xfer.waitForCompletion();
        } catch (AmazonServiceException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "AWS Error or Upload was interrupted");
        }

        xfer_mgr.shutdownNow();

        return "https://" + bucketName + ".s3.eu-central-1.amazonaws.com/" + fileName;
    }

    @PostMapping("/items")
    public ResponseEntity<Diet> createDiet(@RequestBody @NotNull Diet diet, @RequestParam("currentFile") MultipartFile currentFile) {

        if (diet.getId() != null) {
            Optional<Diet> optionalItem = dietService.findById(diet.getId());
            if (optionalItem.isPresent()) {
                throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "Diet already exists");
            }
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Diet id should be null");
        }

        String bucketName = "primefittool";

        if (currentFile != null) {
            String pdfAsUrl = uploadPdfAsUrl(currentFile, bucketName, diet);
            diet.setPdfUrl(pdfAsUrl);
        }

        Diet saveDiet = this.dietService.save(diet);
        return new ResponseEntity<>(saveDiet, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Diet> getDietList() {
        return dietService.findAll();
    }
}
