package com.devcam.shop24h.api;

import com.devcam.shop24h.entity.UserProfile;
import com.devcam.shop24h.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public List<UserProfile> getAllUserProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable(value = "id") Long id) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(id);
        return userProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> createUserProfile(@RequestBody UserProfile userProfile) {
        return userProfileService.createUserProfile(userProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEntity<Object>> updateUserProfile(@PathVariable(value = "id") Long id, @RequestBody UserProfile userProfileDetails) {
        return ResponseEntity.ok(userProfileService.updateUserProfile(id, userProfileDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable(value = "id") Long id) {
        userProfileService.deleteUserProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<UserProfile> profile(HttpServletRequest request, Model model) {
        UserProfile curentProfile =  (UserProfile) request.getSession().getAttribute("user");
        return ResponseEntity.ok(curentProfile);
    }
}
