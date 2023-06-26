package com.example.ChatBot.controller;

import com.example.ChatBot.dto.ChatRequest;
import com.example.ChatBot.dto.ChatResponse;
import com.example.ChatBot.dto.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
public class ChatBotController {
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String url;
    @Value("${openai.api.key}")
    private String apiKey;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/chat",produces = MediaType.APPLICATION_JSON_VALUE)
    public String chat(@RequestBody String prompt) throws JsonProcessingException {
        ChatRequest chatRequest = new ChatRequest(model, List.of(new Message("user", prompt)));
        ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);


        if (chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            throw new RuntimeException("Unable to respond, please try again");
        }
         String responseMessage = chatResponse.getChoices().get(0).getMessage().getContent();
        if (responseMessage == null || responseMessage.isEmpty()) {
            throw new RuntimeException("No response message received");
        }


        return responseMessage;
    }





}
