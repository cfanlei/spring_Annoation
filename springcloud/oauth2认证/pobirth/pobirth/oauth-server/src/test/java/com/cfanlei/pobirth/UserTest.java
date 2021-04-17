package com.cfanlei.pobirth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    @Autowired
    private BCryptPasswordEncoder encoder;
    @Test
    public void code(){
        System.out.println(encoder.encode("123456"));
    }
}
