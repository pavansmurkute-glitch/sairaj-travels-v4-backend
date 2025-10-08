package com.sairajtravels.site.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/files")
@CrossOrigin(origins = "*")
public class FileManagerController {

    @Value("${app.upload.dir:src/main/resources/static/images}")
    private String uploadDir;

    /**
     * Browse files and folders in a directory
     */
    @GetMapping("/browse")
    public ResponseEntity<Map<String, Object>> browseDirectory(@RequestParam(defaultValue = "") String path) {
        try {
            Path fullPath = Paths.get(uploadDir, path).normalize();
            File directory = fullPath.toFile();

            if (!directory.exists() || !directory.isDirectory()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Directory not found"));
            }

            List<Map<String, Object>> folders = new ArrayList<>();
            List<Map<String, Object>> files = new ArrayList<>();

            File[] listFiles = directory.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", file.getName());
                    item.put("createdAt", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

                    if (file.isDirectory()) {
                        item.put("type", "folder");
                        folders.add(item);
                    } else {
                        item.put("type", getFileType(file.getName()));
                        item.put("size", file.length());
                        files.add(item);
                    }
                }
            }

            // Sort folders and files alphabetically
            folders.sort((a, b) -> ((String) a.get("name")).compareToIgnoreCase((String) b.get("name")));
            files.sort((a, b) -> ((String) a.get("name")).compareToIgnoreCase((String) b.get("name")));

            Map<String, Object> response = new HashMap<>();
            response.put("folders", folders);
            response.put("files", files);
            response.put("currentPath", path);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to browse directory: " + e.getMessage()));
        }
    }

    /**
     * Create a new folder
     */
    @PostMapping("/create-folder")
    public ResponseEntity<Map<String, Object>> createFolder(@RequestBody Map<String, String> request) {
        try {
            String path = request.getOrDefault("path", "");
            String name = request.get("name");

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Folder name is required"));
            }

            // Sanitize folder name
            name = name.trim().replaceAll("[^a-zA-Z0-9._-]", "_");

            Path fullPath = Paths.get(uploadDir, path, name).normalize();
            File newFolder = fullPath.toFile();

            if (newFolder.exists()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Folder already exists"));
            }

            if (newFolder.mkdirs()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Folder created successfully",
                    "folderName", name
                ));
            } else {
                return ResponseEntity.internalServerError().body(Map.of("error", "Failed to create folder"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to create folder: " + e.getMessage()));
        }
    }

    /**
     * Upload multiple files
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(defaultValue = "") String path) {
        
        try {
            List<Map<String, Object>> uploadedFiles = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            Path targetPath = Paths.get(uploadDir, path).normalize();
            File targetDir = targetPath.toFile();

            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            for (MultipartFile file : files) {
                try {
                    if (file.isEmpty()) {
                        errors.add("File " + file.getOriginalFilename() + " is empty");
                        continue;
                    }

                    // Validate file type
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null || !isValidFileType(originalFilename)) {
                        errors.add("File type not supported: " + originalFilename);
                        continue;
                    }

                    // Sanitize filename
                    String sanitizedFilename = sanitizeFilename(originalFilename);
                    
                    // Handle duplicate filenames
                    String finalFilename = getUniqueFilename(targetPath, sanitizedFilename);

                    Path filePath = targetPath.resolve(finalFilename);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    Map<String, Object> uploadedFile = new HashMap<>();
                    uploadedFile.put("name", finalFilename);
                    uploadedFile.put("originalName", originalFilename);
                    uploadedFile.put("size", file.getSize());
                    uploadedFile.put("type", getFileType(finalFilename));
                    uploadedFiles.add(uploadedFile);

                } catch (IOException e) {
                    errors.add("Failed to upload " + file.getOriginalFilename() + ": " + e.getMessage());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("uploadedFiles", uploadedFiles);
            response.put("errors", errors);
            response.put("success", errors.isEmpty());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    /**
     * Delete a file or folder
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteItem(
            @RequestParam String path,
            @RequestParam String name) {
        
        try {
            Path fullPath = Paths.get(uploadDir, path, name).normalize();
            File item = fullPath.toFile();

            if (!item.exists()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Item not found"));
            }

            boolean deleted;
            if (item.isDirectory()) {
                deleted = deleteDirectory(item);
            } else {
                deleted = item.delete();
            }

            if (deleted) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Item deleted successfully"
                ));
            } else {
                return ResponseEntity.internalServerError().body(Map.of("error", "Failed to delete item"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Delete failed: " + e.getMessage()));
        }
    }

    /**
     * Download a file
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String path,
            @RequestParam String filename) {
        
        try {
            Path filePath = Paths.get(uploadDir, path, filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get file info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getFileInfo(
            @RequestParam String path,
            @RequestParam String filename) {
        
        try {
            Path filePath = Paths.get(uploadDir, path, filename).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> info = new HashMap<>();
            info.put("name", file.getName());
            info.put("size", file.length());
            info.put("type", getFileType(file.getName()));
            info.put("lastModified", new Date(file.lastModified()));
            info.put("path", path);

            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to get file info: " + e.getMessage()));
        }
    }

    // Helper methods

    private String getFileType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        
        if (extension.matches("jpg|jpeg|png|gif|webp|bmp")) {
            return "image";
        } else if (extension.matches("mp4|avi|mov|wmv|flv|mkv|webm")) {
            return "video";
        } else if (extension.matches("pdf|doc|docx|txt")) {
            return "document";
        } else {
            return "file";
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

    private boolean isValidFileType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        Set<String> allowedExtensions = Set.of(
            "jpg", "jpeg", "png", "gif", "webp", "bmp",  // Images
            "mp4", "avi", "mov", "wmv", "flv", "mkv", "webm",  // Videos
            "pdf", "doc", "docx", "txt"  // Documents
        );
        return allowedExtensions.contains(extension);
    }

    private String sanitizeFilename(String filename) {
        // Remove or replace invalid characters
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String getUniqueFilename(Path directory, String filename) {
        File file = directory.resolve(filename).toFile();
        if (!file.exists()) {
            return filename;
        }

        String name = filename;
        String extension = "";
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            name = filename.substring(0, lastDotIndex);
            extension = filename.substring(lastDotIndex);
        }

        int counter = 1;
        while (file.exists()) {
            String newFilename = name + "_" + counter + extension;
            file = directory.resolve(newFilename).toFile();
            if (!file.exists()) {
                return newFilename;
            }
            counter++;
        }
        
        return filename; // Fallback
    }

    private boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }
}
