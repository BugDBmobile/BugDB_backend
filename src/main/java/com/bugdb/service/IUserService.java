package com.bugdb.service;

import com.bugdb.domain.User;


public interface IUserService {

    Iterable<User> findAll();

    User findByUserName(String name);

}
