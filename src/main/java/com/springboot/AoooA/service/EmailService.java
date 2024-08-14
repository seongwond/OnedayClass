package com.springboot.AoooA.service;

public interface EmailService {
    void sendTemporaryPassword(String userEmail, String temporaryPassword);
}