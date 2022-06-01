package com.primefit.tool.service.dietservice.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.primefit.tool.exceptions.ResourceNotFoundException;
import com.primefit.tool.model.Diet;
import com.primefit.tool.model.Training;
import com.primefit.tool.model.User;
import com.primefit.tool.repository.DietRepository;
import com.primefit.tool.service.dietservice.DietService;
import com.primefit.tool.service.emailsenderservice.EmailService;
import com.primefit.tool.service.userservice.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class used for managing diets.
 */
@Service
@AllArgsConstructor
public class DietServiceImpl implements DietService {

    private DietRepository dietRepository;

    private UserService userService;

    private EmailService emailService;

    @Override
    public List<Diet> findAll() {
        return new ArrayList<>(dietRepository.findAll());
    }

    @Override
    public Optional<Diet> findById(@NotNull Integer id) {
        return dietRepository.findById(id);
    }

    @Override
    public Diet save(Diet diet) {
        return dietRepository.save(diet);
    }

    @Override
    public Diet update(@NotNull Diet newDiet, Integer id) {
        Diet diet = dietRepository.findById(id).orElseThrow();

        diet.setName(newDiet.getName());
        diet.setPdfUrl(newDiet.getPdfUrl());
        diet.setDietCategory(newDiet.getDietCategory());

        return dietRepository.save(diet);
    }

    @Override
    public void deleteById(Integer id) {

        Optional<Diet> optionalDiet = findById(id);
        if (optionalDiet.isPresent()) {

            Diet diet = optionalDiet.get();

            String bucketName = "hardwear-pad-jmk";

            String filename = diet.getPdfUrl().replaceAll("https://" + bucketName + ".s3.eu-central-1.amazonaws.com/", "");

            AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion("eu-central-1").build();
            s3client.deleteObject(bucketName, filename);

            dietRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Diet", "id", id);
        }
    }

    @Override
    public void sendEmailWithDietPlan(Integer dietId, Integer userId) {
        Optional<Diet> optionalDiet = findById(dietId);

        if (optionalDiet.isPresent()) {
          //  String hello = "Hello "
            String info = "<a href='" + optionalDiet.get().getPdfUrl() + "' target=\"_blank\"> here </a><br>Great";
            System.out.println(optionalDiet.get().getPdfUrl());
            User user = userService.findById(userId);

            emailService.sendDiet(user.getEmail(), info);   //sending the url of the diet as body
        }
    }

    @Override
    public File convertMultiPartToFile(@NotNull MultipartFile file) throws IOException {
        File convFile = new File("FileName");
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
