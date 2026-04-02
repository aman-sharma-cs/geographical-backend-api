package com.example.geographical.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Map;

@RestController
@RequestMapping("/api/translate")
@CrossOrigin
public class TranslationController {

    @PostMapping
    public ResponseEntity<?> translate(@RequestBody Map<String, String> body) {
        try {
            String text = body.get("text");
            String targetLang = body.get("targetLang");

            if (text == null || targetLang == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing text or language"));
            }

            // Encode text
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

            String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl="
                    + targetLang + "&dt=t&q=" + encodedText;

            System.out.println(urlStr);

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();


            String response = sb.toString();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            String translated = root.get(0).get(0).get(0).asText();
            String original = root.get(0).get(0).get(1).asText();
            String detectedLang = root.get(2).asText();

            System.out.println("Translated = " + translated);

            return ResponseEntity.ok(
                    Map.of(
                            "translated_text", translated,
                            "original_text", original,
                            "detected_language", detectedLang
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Translation failed", "details", e.getMessage()));
        }
    }}