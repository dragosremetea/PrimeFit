package com.primefit.tool.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.primefit.tool.model.Training;
import com.primefit.tool.service.trainingservice.TrainingService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/trainings")
@AllArgsConstructor
public class TrainingController {

    private TrainingService trainingService;

    @GetMapping
    public List<Training> getTrainingList() {
        return trainingService.findAll();
    }

    public String uploadPdfAsUrl(MultipartFile storedPdf, String bucketName, Training training) {

        File pdf;
        try {
            pdf = trainingService.convertMultiPartToFile(storedPdf);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unable to convert input stream to file");
        }

        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion("eu-central-1").build();
        TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3client).build();
        String fileName = "Training-pdf-for-" + training.getName() + "-" + new Timestamp(System.currentTimeMillis()) + ".pdf";
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

    @PostMapping
    public ResponseEntity<Training> createTraining(@RequestPart("training") Training training, @RequestParam("currentFile") MultipartFile currentFile) {

        if (training.getId() != null) {
            Optional<Training> optionalItem = trainingService.findById(training.getId());
            if (optionalItem.isPresent()) {
                throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "Item already exists");
            }
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Item id should be null");
        }

        String bucketName = "hardwear-pad-jmk";

        if (currentFile != null) {
            String pdfAsUrl = uploadPdfAsUrl(currentFile, bucketName, training);
            training.setPdfUrl(pdfAsUrl);
        }

        Training savedTraining = this.trainingService.save(training);
        return new ResponseEntity<>(savedTraining, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Training> deleteTraining(@PathVariable Integer id) {
        trainingService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Training> updateTraining(@RequestBody Training newTraining, @PathVariable Integer id) {
        trainingService.update(newTraining, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sendTrainingPlan/{trainingId}/{userId}")
    public void sendTrainingViaEmail(@PathVariable Integer trainingId, @PathVariable Integer userId) {
        trainingService.sendEmailWithTrainingPlan(trainingId, userId);
    }
}
