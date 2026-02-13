package com.project.back_end.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.logging.Logger;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AdminRepositoryTest{
    @Autowired
    private AdminRepository adminRepository;
    Logger logger = Logger.getLogger(AdminRepositoryTest.class.getName());
    @Test
    void testFindByUsername() {

    }

    @Test
    void findByUsername() {
        var test=adminRepository.findByUsername("admin");
        logger.info("Admin found: " + test);
    }
}
