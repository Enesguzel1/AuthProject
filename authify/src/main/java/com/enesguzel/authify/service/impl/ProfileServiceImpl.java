package com.enesguzel.authify.service.impl;

import com.enesguzel.authify.entity.UserEntity;
import com.enesguzel.authify.io.ProfileRequest;
import com.enesguzel.authify.io.ProfileResponse;
import com.enesguzel.authify.repository.UserRepository;
import com.enesguzel.authify.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity newProfile = convertToUserEntity(request);
        if(!userRepository.existsByEmail(newProfile.getEmail())){
            newProfile = userRepository.save(newProfile);
            return convertToResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");

    }

    private ProfileResponse convertToResponse(UserEntity newProfile) {
        return  ProfileResponse.builder()
                .userId(newProfile.getUserId())
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .createdAt(newProfile.getCreatedAt())
                .updatedAt(newProfile.getUpdatedAt())
                .build();
    }

    private UserEntity convertToUserEntity(ProfileRequest request) {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();
    }
}
