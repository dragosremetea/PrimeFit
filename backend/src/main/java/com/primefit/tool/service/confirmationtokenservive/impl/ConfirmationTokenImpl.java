package com.primefit.tool.service.confirmationtokenservive.impl;

import com.primefit.tool.exceptions.ResourceNotFoundException;
import com.primefit.tool.model.ConfirmationToken;
import com.primefit.tool.repository.ConfirmationTokenRepository;
import com.primefit.tool.service.confirmationtokenservive.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken saveConfirmationToken(ConfirmationToken token) {
        return confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    public void delete(Integer id) {
        confirmationTokenRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Token", "id", id));

        confirmationTokenRepository.deleteById(id);
    }
}
