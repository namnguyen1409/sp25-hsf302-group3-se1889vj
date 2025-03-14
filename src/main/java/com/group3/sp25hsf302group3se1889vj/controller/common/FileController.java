package com.group3.sp25hsf302group3se1889vj.controller.common;

import com.group3.sp25hsf302group3se1889vj.service.StorageService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final StorageService storageService;

    @Value("${storage.max-size}")
    private int maxSize;

    private Map<String, String> putResponse(String status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        return response;
    }

    private Map<String, String> putResponse(String status, String message, String url) {
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        response.put("url", url);
        return response;
    }


    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(
            @RequestParam("file")MultipartFile file
            ) {
        int maxFileSize = 1024 * 1024 * maxSize;
        if(!storageService.isValidSize(file, maxFileSize)) {
            return ResponseEntity.badRequest().body(putResponse("error", "File size is too large"));
        }
        if(!storageService.isImage(file)) {
            return ResponseEntity.badRequest().body(putResponse("error", "File is not an image"));
        }
        else {
            String url = storageService.store(file);
            return ResponseEntity.ok(putResponse("success", "File uploaded successfully", url));
        }
    }

    @GetMapping("/roots/{filename:.+}")
    public ResponseEntity<Resource> getRootFile(@PathVariable String filename) throws IOException {
        var file = storageService.loadAsResource(filename);

        if (file == null || !file.exists() || !file.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }


    @GetMapping("/temps/{filename:.+}")
    public ResponseEntity<Resource> getTempFile(@PathVariable String filename) throws IOException {
        var file = storageService.loadTempAsResource(filename);

        if (file == null || !file.exists() || !file.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }

}
