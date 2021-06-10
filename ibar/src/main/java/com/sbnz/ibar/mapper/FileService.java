package com.sbnz.ibar.mapper;

import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private static String path = "../photos";

    public String saveImage(MultipartFile file, String fileName) throws IOException {
        fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filepath = Paths.get(path, fileName);

        file.transferTo(filepath);

        return "/photos/" + fileName;
    }

    public String uploadImageAsBase64(String image) throws IOException {
        byte[] array = Files.readAllBytes(Paths.get(image));
        return "data:image/jpeg;base64," + Base64Utils.encodeToString(array);
    }

    public void deleteImageFromFile(String url) throws IOException {
        Path filePath = Paths.get(".." + url);
        Files.delete(filePath);
    }

    public void uploadNewImage(MultipartFile newImage, String url) throws IOException {
        Path filepath = Paths.get(".." + url);
        newImage.transferTo(filepath);
    }
}
