package com.hechi.niumall.service;

import javax.mail.MessagingException;

public interface MessageService {
    void sentMessage(String phonenumber) throws Exception;
    void sentMailCode(String to) throws MessagingException;
}