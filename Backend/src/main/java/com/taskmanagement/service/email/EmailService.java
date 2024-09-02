package com.taskmanagement.service.email;

public interface EmailService {

	void sendTaskAssignmentEmail(String toEmail, String taskTitle);
    void sendCommentNotificationEmail(String toEmail, String taskTitle, String comment);
}
