package com.ktsnwt.project.team9.service.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import com.ktsnwt.project.team9.constants.FileServiceConstants;
import com.ktsnwt.project.team9.helper.implementations.FileService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class FileServiceUnitTest {

	@Autowired
	FileService fileService;

	@Test
	public void testSaveImage_WithValidParameters_ShouldReturnPath() throws IOException {
		byte[] content = Files.readAllBytes(Paths.get(FileServiceConstants.SAVE_NEW_IMAGE));
		MockMultipartFile file = new MockMultipartFile("file", FileServiceConstants.IMAGE_NAME, MediaType.IMAGE_JPEG_VALUE, content);

		String path = fileService.saveImage(file, "first");

		assertEquals(FileServiceConstants.NAME_OF_NEW_IMAGE, path);
	}

	@Test(expected = IOException.class)
	public void testUploadImageAsBase64_WithNonExistingPath_ShouldThrowIOException() throws IOException {
		fileService.uploadImageAsBase64(FileServiceConstants.NONEXISTING_IMAGE_NAME);
	}

	@Test
	public void testUploadImageAsBase64_WithExististingPath_ShouldReturnBase64String() throws IOException {
		byte[] array = Files.readAllBytes(Paths.get(FileServiceConstants.SAVE_NEW_IMAGE));
		String expectedString = "data:image/jpeg;base64," + Base64Utils.encodeToString(array);

		String base64String = fileService.uploadImageAsBase64(FileServiceConstants.SAVE_NEW_IMAGE);

		assertEquals(expectedString, base64String);
	}

	@Test
	public void testDeleteImageFromFile_WithExistingImageName_ShouldDeleteImageFromDisk() throws IOException {
		File file = new File(FileServiceConstants.IMAGE_FOR_DELETE);
		byte[] array = Files.readAllBytes(Paths.get(FileServiceConstants.IMAGE_FOR_DELETE));

		fileService.deleteImageFromFile(FileServiceConstants.IMAGE_FOR_DELETE);

		assertFalse(file.exists());

		OutputStream outputStream = new FileOutputStream(file);
		outputStream.write(array);
		outputStream.close();
	}

	@Test(expected = NoSuchFileException.class)
	public void testDeleteImageFromFile_WithNonExistingImageName_ShouldThrowIOException() throws IOException {
		fileService.deleteImageFromFile(FileServiceConstants.NONEXISTING_IMAGE_NAME);
	}

	@Test
	public void testUploadNewImage_WithValidParameters_ShouldReplaceNewImageWithOld() throws IOException {
		byte[] oldImage = Files.readAllBytes(Paths.get(FileServiceConstants.OLD_IMAGE_UPLOAD));
		byte[] newImage = Files.readAllBytes(Paths.get(FileServiceConstants.NEW_IMAGE_UPLOAD));
		MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, newImage);

		fileService.uploadNewImage(file, FileServiceConstants.OLD_IMAGE_UPLOAD);

		byte[] oldImageUpdated = Files.readAllBytes(Paths.get(FileServiceConstants.OLD_IMAGE_UPLOAD));
		assertTrue(Arrays.equals(newImage, oldImageUpdated));

		OutputStream outputStream = new FileOutputStream(FileServiceConstants.OLD_IMAGE_UPLOAD);
		outputStream.write(oldImage);
		outputStream.close();
	}
}
