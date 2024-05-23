package com.spring.contactlist.contactapi.service;

import static com.spring.contactlist.contactapi.constant.Constant.PHOTO_DIRECTORY;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.contactlist.contactapi.domain.Contact;
import com.spring.contactlist.contactapi.repo.ContactRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {

	@Autowired
	private final ContactRepo contactRepo;

	public org.springframework.data.domain.Page<Contact> getAllContacts(int page, int size) {
		return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
	}

//	public Page<Contact> getAllContacts(int page, int size) {
//		return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
//	}
//
	public Contact getContact(String id) {
		return contactRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
	}

	public Contact createContact(Contact contact) {
		return contactRepo.save(contact);
	}

	public void deleteContact(String id) {
//		String id = contact.getId();
		contactRepo.deleteById(id);
	}

	public String uploadPhoto(String id, MultipartFile file) {
		log.info("saving picture for user id: {}", id);
		Contact contact = getContact(id);
		String photoUrl = photoFunction.apply(id, file);
		contact.setPhotoUrl(photoUrl);
		contactRepo.save(contact);
		return photoUrl;
	}

//	private final Function<String, String> fileExtension = filename -> Optional.of(filename)
//			.filter(name -> name.contains(".")).map(name -> "." + name.substring(filename.lastIndexOf(".") + 1))
//			.orElse(".png");
//
//	private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
//		String filename = id + fileExtension.apply(image.getOriginalFilename());
//		try {
//			Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
//			if (!Files.exists(fileStorageLocation)) {
//				Files.createDirectories(fileStorageLocation);
//			}
//			Files.copy(image.getInputStream(), fileStorageLocation.resolve(id + ".png"), REPLACE_EXISTING);
//			return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contacts/image/" + filename)
//					.toUriString();
//		} catch (Exception e) {
//			throw new RuntimeException("Unable to save image");
//		}
//	};

	private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
		// Get the original filename of the uploaded image
		String originalFilename = image.getOriginalFilename();

		// Extract file extension from the original filename
		String fileExtension = getFileExtension(originalFilename);

		try {
			// Create the file storage location if it doesn't exist
			Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
			if (!Files.exists(fileStorageLocation)) {
				Files.createDirectories(fileStorageLocation);
			}

			// Generate a unique filename with the specified ID and file extension
			String filename = id + fileExtension;

			// Copy the uploaded image to the file storage location with the generated
			// filename
			Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);

			// Generate the image URL using ServletUriComponentsBuilder
			return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contacts/image/" + filename)
					.toUriString();
		} catch (IOException e) {
			throw new RuntimeException("Unable to save image", e);
		}
	};

	// Helper method to extract file extension from a filename
	private String getFileExtension(String filename) {
		if (filename == null || filename.isEmpty()) {
			throw new IllegalArgumentException("Invalid filename");
		}

		// Find the last occurrence of '.' in the filename
		int lastIndex = filename.lastIndexOf('.');
		if (lastIndex == -1) {
			throw new IllegalArgumentException("File has no extension");
		}

		// Extract the file extension (substring after the last '.')
		return filename.substring(lastIndex).toLowerCase(); // Convert to lowercase for consistency
	}

}
