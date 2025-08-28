package com.enesguzel.authify.service.impl;

import com.enesguzel.authify.entity.UserEntity;
import com.enesguzel.authify.io.ProfileRequest;
import com.enesguzel.authify.io.ProfileResponse;
import com.enesguzel.authify.repository.UserRepository;
import com.enesguzel.authify.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity newProfile = convertToUserEntity(request);
        if(!userRepository.existsByEmail(newProfile.getEmail())){
            newProfile = userRepository.save(newProfile);
            return convertToResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");

    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity existingProfile = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return convertToResponse(existingProfile);

    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        long expiryTime = System.currentTimeMillis() + (60*15*1000); //15 dakika
        existingUser.setResetOtp(otp);
        existingUser.setResetOtpExpireAt(expiryTime);
        userRepository.save(existingUser);
        try{
            //mail gönder
            emailService.sendResetOtpMail(existingUser.getEmail(), otp);
        }catch(Exception e){
            throw new RuntimeException("Unable to send reset OTP");
        }
    }

    @Override
    public void resetPassword(String email, String newPassword, String otp) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if(otp==null || !otp.equals(existingUser.getResetOtp())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Reset OTP is not valid");
        }
        if(existingUser.getResetOtpExpireAt()<System.currentTimeMillis()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Reset OTP expired");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpireAt(0L);

        userRepository.save(existingUser);

    }

    @Override
    public void sendOtp(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        if(existingUser.getIsAccountVerified()!= null && existingUser.getIsAccountVerified()){
            return;
        }
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        long expiryTime = System.currentTimeMillis() + (60*60*24*1000);

        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpireAt(expiryTime);
        userRepository.save(existingUser);
        try{
            emailService.sendVerifyOtpMail(existingUser.getEmail(), otp);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occurred while sending verify OTP mail");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity existingUser =  userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(otp==null || !otp.equals(existingUser.getVerifyOtp())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Verify OTP is not valid");
        }
        if(existingUser.getVerifyOtpExpireAt()<System.currentTimeMillis()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Verify OTP expired");
        }
        existingUser.setVerifyOtp(null);
        existingUser.setVerifyOtpExpireAt(0L);
        existingUser.setIsAccountVerified(true);
        userRepository.save(existingUser);

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
