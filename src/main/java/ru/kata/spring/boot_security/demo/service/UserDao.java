package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;

@Repository
@Transactional
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public User findByUsername(String firstName){
        User user = (User) entityManager.createQuery("FROM User where firstName = '" + firstName + "'").getSingleResult();
        return user;
    }

}
