package com.taskmanagement.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
	
	@Override
	public void sendTaskAssignmentEmail(String toEmail, String taskTitle) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("Task Management System: New Task Assigned!");

            String emailBody = "<html><body>" +
                    "<h3>A new task has been assigned to you by Admin. Task Name: " + taskTitle + "</h3>" +
                    "</body></html>";

            messageHelper.setText(emailBody, true);
        };

        javaMailSender.send(messagePreparator);
	}

	@Override
	public void sendCommentNotificationEmail(String toEmail, String taskTitle, String comment) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("Task Management System: New Comment on Task");

            String emailBody = "<html><body>" +
                    "<h3>A new comment has been added to your task by admin.<br/> Task: " + taskTitle + "</h3>" +
                    "<h4>" + comment + "</h4>" +
                    "</body></html>";

            messageHelper.setText(emailBody, true);
        };

        javaMailSender.send(messagePreparator);
	}
}
