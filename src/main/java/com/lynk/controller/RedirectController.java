package com.lynk.controller;

import com.lynk.models.UrlMapping;
import com.lynk.service.UrlMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RedirectController
{
    private UrlMappingService urlMappingService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl)
    {
        UrlMapping originalUrlMapping = urlMappingService.getOriginalUrl(shortUrl);
        if (originalUrlMapping != null)
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Location", originalUrlMapping.getOriginalUrl());
            return ResponseEntity.status(302).headers(httpHeaders).build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }


}
