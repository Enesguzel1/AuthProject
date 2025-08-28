package com.enesguzel.authify.service;

import com.enesguzel.authify.io.ProfileRequest;
import com.enesguzel.authify.io.ProfileResponse;

import java.util.Optional;

public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getProfile(String email);
    void sendResetOtp(String email);
    void resetPassword(String email, String newPassword,String otp);
    void sendOtp(String email);
    void verifyOtp(String email, String otp);
}
