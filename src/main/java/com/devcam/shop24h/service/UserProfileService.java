package com.devcam.shop24h.service;

import com.devcam.shop24h.entity.UserProfile;
import com.devcam.shop24h.repository.UserProfileRepository;
import com.devcam.shop24h.security.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public UserProfile getUserProfileByUserId(Long id) {
        return userProfileRepository.findByUserId(id);
    }

    public UserProfile setUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            UserProfile userProfile = new UserProfile();
            if (principal instanceof UserPrincipal) {
                // lấy thông tin tài khoản theo UserId 
                UserPrincipal userPrincipal = (UserPrincipal) principal;
                Long userId = userPrincipal.getUserId();
                UserProfile currentUserProfile = userProfileRepository.findByUserId(userId);
                if (currentUserProfile != null) {
                    return currentUserProfile;
                }
            } else if (principal instanceof DefaultOidcUser) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
                Map<String, Object> attributes = oidcUser.getAttributes();
                
                userProfile.setEmail((String) attributes.getOrDefault("email", null));
                userProfile.setAvatar((String) attributes.getOrDefault("picture", null));
                userProfile.setFullName((String) attributes.getOrDefault("name", null));
                userProfile.setUsername((String) attributes.getOrDefault("preferred_username", null));
                userProfile.setSocialcode((String) attributes.getOrDefault("sub", null));
                userProfile.setPhone((String) attributes.getOrDefault("phone_number", null));
                userProfile.setAddress((String) attributes.getOrDefault("address", null));
                userProfile.setDescription((String) attributes.getOrDefault("description", null));
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) principal;
                Map<String, Object> attributes = oauth2User.getAttributes();
                // String firtName = (String) attributes.getOrDefault("family_name", null);
                // String lastName = (String) attributes.getOrDefault("given_name", null); 
                userProfile.setEmail((String) attributes.getOrDefault("email", null));
                Object avatar = attributes.get("picture");
                if (avatar instanceof String) {
                    userProfile.setAvatar((String) avatar);
                } else if(avatar instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> avatarMap = (Map<String, Object>) avatar;
                    if (avatarMap.containsKey("data")) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> dataMap = (Map<String, Object>) avatarMap.get("data");
                        userProfile.setAvatar((String) dataMap.get("url"));
                    }
                }
                userProfile.setFullName((String) attributes.getOrDefault("name", null));
                String socialcode = (String) attributes.getOrDefault("sub", attributes.get("id"));
                userProfile.setSocialcode(socialcode);
                userProfile.setSocialcode((String) attributes.getOrDefault("sub", null));
                userProfile.setPhone((String) attributes.getOrDefault("phone_number", null));
                userProfile.setAddress((String) attributes.getOrDefault("address", null));
                userProfile.setDescription((String) attributes.getOrDefault("description", null));
            }
            return userProfile;
        }
        return null;
    }

    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile getUserProfileBySocialcode(String code) {
        return userProfileRepository.findBySocialcode(code);
    }

    public ResponseEntity<Object> createUserProfile(UserProfile userProfile) {
        try {
            UserProfile nProfile = new UserProfile();
            nProfile.setUserId(userProfile.getUserId());
            nProfile.setSocialcode(userProfile.getSocialcode());
            nProfile.setEmail(userProfile.getEmail());
            nProfile.setPhone(userProfile.getPhone());
            nProfile.setAddress(userProfile.getAddress());
            nProfile.setAvatar(userProfile.getAvatar());
            nProfile.setDescription(userProfile.getDescription());
            nProfile.setFullName(userProfile.getFullName());
            
            UserProfile savedProfile = userProfileRepository.save(nProfile);
            return new ResponseEntity<>(savedProfile, HttpStatus.CREATED);
        } catch (Exception e) {
            // In ra thông báo lỗi
            e.printStackTrace();
            
            // Trả về ResponseEntity với thông báo lỗi
            return new ResponseEntity<>("An error occurred while creating the user profile: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateUserProfile(Long id, UserProfile userProfileDetails) {
        Optional<UserProfile> fProfile = userProfileRepository.findById(id);
        if(fProfile.isPresent()){
            UserProfile nProfile = fProfile.get();
            nProfile.setUsername(userProfileDetails.getUsername());
            nProfile.setFullName(userProfileDetails.getFullName());
            nProfile.setEmail(userProfileDetails.getEmail());
            nProfile.setAvatar(userProfileDetails.getAvatar());
            nProfile.setPhone(userProfileDetails.getPhone());
            nProfile.setAddress(userProfileDetails.getAddress());
            nProfile.setDescription(userProfileDetails.getDescription());
            try{
                return new ResponseEntity<Object>(userProfileRepository.save(nProfile), HttpStatus.OK);
            }catch (Exception e) {
                return ResponseEntity.unprocessableEntity()
                        .body("Failed to Update specified Customer:" + e.getCause().getCause().getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Failed to get specified Customer: " + id + "  for update.");
        }
    }

    public void deleteUserProfile(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        userProfileRepository.delete(userProfile);
    }
}
