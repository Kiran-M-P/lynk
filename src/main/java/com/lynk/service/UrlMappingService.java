package com.lynk.service;

import com.lynk.dtos.ClickEventDTO;
import com.lynk.dtos.UrlMappingDTO;
import com.lynk.models.ClickEvent;
import com.lynk.models.UrlMapping;
import com.lynk.models.User;
import com.lynk.repository.ClickEventRepository;
import com.lynk.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService
{
    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;

    public UrlMappingDTO createShortUrl(String originalUrl, User user)
    {
        String shortUrl = generateShortUrl(originalUrl);

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDTO(savedUrlMapping);
    }


    public List<UrlMappingDTO> getUrlsByUser(User user)
    {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .toList();
    }


    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end)
    {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        if (urlMapping != null)
        {
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
                    .collect(Collectors.groupingBy(clickEvent -> clickEvent.getClickDate().toLocalDate(), Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> new ClickEventDTO(entry.getKey(), entry.getValue()))
                    .toList();
        }

        return null;
    }



    private UrlMappingDTO convertToDTO(UrlMapping urlMapping)
    {
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setClickCount(urlMappingDTO.getClickCount());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
        return urlMappingDTO;
    }

    private String generateShortUrl(String originalUrl)
    {
        // TODO : add uniqueness check based on DB
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        StringBuilder shortUrl = new StringBuilder(8);
        for (int i = 0; i < 8; i++)
        {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }


        return shortUrl.toString();
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end)
    {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        return clickEvents.stream()
                .collect(Collectors.groupingBy(clickEvent -> clickEvent.getClickDate().toLocalDate(), Collectors.counting()));
    }

    public UrlMapping getOriginalUrl(String shortUrl)
    {
        UrlMapping originalUrlMapping =  urlMappingRepository.findByShortUrl(shortUrl);
        if (originalUrlMapping != null)
        {
            originalUrlMapping.setClickCount(originalUrlMapping.getClickCount() + 1);
            urlMappingRepository.save(originalUrlMapping);

            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(originalUrlMapping);
            clickEventRepository.save(clickEvent);
        }

        return originalUrlMapping;
    }
}
