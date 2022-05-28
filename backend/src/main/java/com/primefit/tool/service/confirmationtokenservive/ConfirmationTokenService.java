package com.primefit.tool.service.confirmationtokenservive;

import com.primefit.tool.model.ConfirmationToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenService {

    /**
     * Set a token confirmation date
     *
     * @param token - the token we want to save
     * @return - an integer
     */
    int setConfirmedAt(String token);

    /**
     * ]
     * Save a token after it has been confirmed
     *
     * @param token -  the token we want to save
     * @return - the saved token
     */
    ConfirmationToken saveConfirmationToken(ConfirmationToken token);

    /**
     * Get a saved token from the DB
     *
     * @param token - the token we want to search
     * @return - the persisted searched token
     */
    Optional<ConfirmationToken> getToken(String token);

    /**
     * Delete a token based on his id
     *
     * @param id - the token id we want to delete
     */
    void delete(Integer id);
}
