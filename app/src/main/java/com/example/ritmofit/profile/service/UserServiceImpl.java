package com.example.ritmofit.profile.service;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.profile.model.UpdateUserRequest;
import com.example.ritmofit.profile.model.User;
import com.example.ritmofit.profile.repository.UserRepository;

import javax.inject.Inject;

public class UserServiceImpl implements UserService {

    UserRepository repository;

    @Inject
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void fetchCurrentUser(DomainCallback<User> callback) {
        repository.currentUser(callback);
    }

    @Override
    public void saveUser(Long id, UpdateUserRequest req, DomainCallback<User> cb) {
        repository.saveUser(id, req, cb);
    }
}
