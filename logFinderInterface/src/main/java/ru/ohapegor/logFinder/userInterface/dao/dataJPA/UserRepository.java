package ru.ohapegor.logFinder.userInterface.dao.dataJPA;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import java.util.List;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User,String> {

    @Override
    List<User> findAll();

    @Override
    @Query("SELECT u FROM User u WHERE u.userName=:userName")
    User getOne(@Param("userName") String userName);

    @Override
    @Transactional
    @Query("DELETE FROM User u WHERE u.userName=:userName")
    void delete(String userName);

    @Override
    @Transactional
    User save(User user);
}
