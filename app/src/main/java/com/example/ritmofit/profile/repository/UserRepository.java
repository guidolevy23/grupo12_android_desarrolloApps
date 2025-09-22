package com.example.ritmofit.profile.repository;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.profile.model.User;

public interface UserRepository {
    void currentUser(DomainCallback<User> callback);
}
