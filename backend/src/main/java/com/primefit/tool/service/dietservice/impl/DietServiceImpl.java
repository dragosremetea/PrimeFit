package com.primefit.tool.service.dietservice.impl;

import com.primefit.tool.model.Diet;
import com.primefit.tool.repository.DietRepository;
import com.primefit.tool.service.dietservice.DietService;
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

@Service
@AllArgsConstructor
public class DietServiceImpl implements DietService {

    private DietRepository dietRepository;

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
    public File convertMultiPartToFile(@NotNull MultipartFile file) throws IOException {
        File convFile = new File("FileName");
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
