package com.enesguzel.authify.controller;

import com.enesguzel.authify.entity.UserEntity;
import com.enesguzel.authify.io.AuthRequest;
import com.enesguzel.authify.io.AuthResponse;
import com.enesguzel.authify.service.impl.AppUserDetailService;
import com.enesguzel.authify.util.JwtUtil;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailService appUserDetailService;
    private final JwtUtil jwtUtil;

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

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
