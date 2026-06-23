package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;


@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<Profile> getProfile(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Oops! You must log in first.");
        }
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        Profile currentProfile = profileService.getProfileById(userId);

        return ResponseEntity.ok(currentProfile);
    }

    @PutMapping
    public ResponseEntity<Profile>  updateProfile(Principal principal, @RequestBody Profile profile){
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        Profile updatedProfile = profileService.updateProfile(userId, profile);
        return ResponseEntity.ok(updatedProfile);
    }

}
