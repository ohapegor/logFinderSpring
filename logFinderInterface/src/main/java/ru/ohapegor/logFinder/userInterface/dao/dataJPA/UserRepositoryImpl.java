package ru.ohapegor.logFinder.userInterface.dao.dataJPA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.ohapegor.logFinder.userInterface.dao.UserDAO;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import java.util.List;

@Repository("userDaoDataJPA")
public class UserRepositoryImpl implements UserDAO {


    private UserRepository repository;

    @Autowired
    public UserRepositoryImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public void updateUser(User user) {
        repository.save(user);
    }

    @Override
    public User getUserByName(String username) {
        return repository.getOne(username);
    }

    @Override
    public void saveUser(User user) {
        repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void deleteUser(User user) {
        repository.delete(user.getUserName());
    }
}
