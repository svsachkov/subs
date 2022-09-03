package subs.api.file;

import subs.api.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface FileRepository extends JpaRepository<File, Integer> {

    Optional<File> findByNameAndDateAndUser(String name, Date date, User user);
}
