package com.lynk.controller;

import com.lynk.dtos.UrlMappingDTO;
import com.lynk.models.User;
import com.lynk.service.UrlMappingService;
import com.lynk.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController
{
    private UrlMappingService urlMappingService;
    private UserService userService;


    // {"originalUrl" : "https://example.com"}
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request, Principal principal)
    {
        String originalUrl = request.get("originalUrl");
        User user = userService.findUserByUsername(principal.getName());
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return  ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal)
    {
        User user = userService.findUserByUsername(principal.getName());
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }

}
