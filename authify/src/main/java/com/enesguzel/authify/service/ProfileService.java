package com.enesguzel.authify.service;

import com.enesguzel.authify.io.ProfileRequest;
import com.enesguzel.authify.io.ProfileResponse;

import java.util.Optional;

public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getProfile(String email);
}
