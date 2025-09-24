package com.example.ritmofit.profile.service;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.profile.model.UpdateUserRequest;
import com.example.ritmofit.profile.model.User;

public interface UserService {

    void fetchCurrentUser(DomainCallback<User> callback);

    void saveUser(Long id, UpdateUserRequest req, DomainCallback<User> cb);
}
