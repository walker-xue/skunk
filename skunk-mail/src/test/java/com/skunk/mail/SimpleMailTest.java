package com.skunk.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleMailTest {
 
	@Autowired
	private MailService mailService;
 
	@Test
	public void sendMail(){
 
		mailService.sendSimpleMail("测试Springboot发送邮件", "发送邮件...");
	}
}