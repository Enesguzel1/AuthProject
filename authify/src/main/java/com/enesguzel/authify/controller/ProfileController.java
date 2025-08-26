package com.enesguzel.authify.controller;

import com.enesguzel.authify.io.ProfileRequest;
import com.enesguzel.authify.io.ProfileResponse;
import com.enesguzel.authify.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse createProfile(@Valid @RequestBody ProfileRequest request){
        return profileService.createProfile(request);
        //TODO: send to user welcome mail
    }

}
