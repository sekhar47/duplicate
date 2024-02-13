package com.example.userprofile;


import com.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private UserProfileService userService;

    @Autowired
    public ProfileController(UserProfileService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        // Fetch logged-in user's profile and add it to the model
        String loggedInEmpId = principal.getName(); // Get the logged-in user's empid (username)
        User user = userService.getUserByEmpid(loggedInEmpId);

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            // Handle the case when user is not found
        }

        return "profile";
    }




    @GetMapping("/editAvailability")
    public String editAvailability(Model model, Principal principal) {
        String loggedInEmpId = principal.getName();
        User user = userService.getUserByEmpid(loggedInEmpId);
        model.addAttribute("user", user);
        return "editAvailability";
    }

    // Method to handle update profile request
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute User updatedUser, Principal principal) {
        String loggedInEmpId = principal.getName();
        User currentUser = userService.getUserByEmpid(loggedInEmpId);
        
        // Update only necessary fields
       
        currentUser.setEmpmobile(updatedUser.getEmpmobile());
        currentUser.setAvailability(updatedUser.getAvailability());
        currentUser.setDesignation(updatedUser.getDesignation()); // Update designation
        userService.updateUserProfile(currentUser);
        return "redirect:/profile";
    }
    
    
    @PostMapping("/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture, Principal principal) {
        try {
            String loggedInEmpId = principal.getName();
            byte[] pictureBytes = profilePicture.getBytes();
            userService.updateProfilePicture(pictureBytes, loggedInEmpId);
            return "redirect:/profile";
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error
            return "error";
        }
    }
    

}
