package com.group3.sp25hsf302group3se1889vj.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

        void sendMail(String to, String subject, String body);

        boolean sendHTMLMail(String to, String subject, String body);

}
