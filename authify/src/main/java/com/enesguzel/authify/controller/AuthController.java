package com.enesguzel.authify.controller;

import com.enesguzel.authify.entity.UserEntity;
import com.enesguzel.authify.io.AuthRequest;
import com.enesguzel.authify.io.AuthResponse;
import com.enesguzel.authify.io.ProfileRequest;
import com.enesguzel.authify.io.ResetPasswordRequest;
import com.enesguzel.authify.service.ProfileService;
import com.enesguzel.authify.service.impl.AppUserDetailService;
import com.enesguzel.authify.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailService appUserDetailService;
    private final JwtUtil jwtUtil;
    private final ProfileService profileService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        try {

            authenticate(authRequest.getEmail(),authRequest.getPassword());
            final UserDetails userDetails = appUserDetailService.loadUserByUsername(authRequest.getEmail());
            final String token = jwtUtil.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("jwt",token)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse(userDetails.getUsername(), token));
        }catch (BadCredentialsException e){
            Map<String,Object> mapError = new HashMap<>();
            mapError.put("error",true);
            mapError.put("message","Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapError);
        }catch (DisabledException e){
            Map<String,Object> mapError = new HashMap<>();
            mapError.put("error",true);
            mapError.put("message","Account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapError);
        }catch (Exception e){
            Map<String,Object> mapError = new HashMap<>();
            mapError.put("error",true);
            mapError.put("message","Authentication Failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapError);
        }

    }
    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return ResponseEntity.ok(email!=null);
    }

    @PostMapping("/send-reset-otp")
    public void sendResetOtp(@RequestParam String email){
        try {
            profileService.sendResetOtp(email);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Unable to send reset OTP");
        }
    }

    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        profileService.resetPassword(request.getEmail(), request.getNewPassword(), request.getOtp());
    }
    @PostMapping("/send-otp")
    public void sendOtp(@CurrentSecurityContext(expression = "authentication?.name")String email) {
        profileService.sendOtp(email);
    }
    @PostMapping("/verify-email")
    public void verifyEmail(@RequestBody Map<String, Object> request,@CurrentSecurityContext(expression = "authentication?.name")String email) {
        if (request.get("otp").toString() ==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing OTP");
        }
        profileService.verifyOtp(email,request.get("otp").toString());
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
