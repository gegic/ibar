package com.ktsnwt.project.team9.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ktsnwt.project.team9.helper.implementations.FileService;

@RestController
@RequestMapping(value = "/api/images")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*")
public class ImageController {

	@Autowired
	private FileService fileService;
	
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		String uniqueID = UUID.randomUUID().toString();
		String path = fileService.saveImage(file, uniqueID + "_" +file.getOriginalFilename());
		return new ResponseEntity<String>(path, HttpStatus.OK);
	}
	
	@PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> upload(@RequestParam("file") MultipartFile[] file) throws IllegalStateException, IOException {
		ArrayList<String> images = new ArrayList<String>();
		for (MultipartFile multipartFile : file) {
			String uniqueID = UUID.randomUUID().toString();			
			String path = fileService.saveImage(multipartFile, uniqueID + "_" +multipartFile.getOriginalFilename());
			images.add(path);
		}
		return new ResponseEntity<ArrayList<String>>(images, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/delete", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> deleteImage(@RequestBody String file) throws IllegalStateException, IOException {
		fileService.deleteImageFromFile("src/main/resources/uploadedImages/" + file);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	@PostMapping(value = "",  produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getImage(@RequestBody String file) throws IllegalStateException, IOException {
		try {
			return new ResponseEntity<String>(fileService.uploadImageAsBase64(file), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

}
