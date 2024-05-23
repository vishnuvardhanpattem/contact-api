package com.spring.contactlist.contactapi.controller;

import static com.spring.contactlist.contactapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.contactlist.contactapi.domain.Contact;
import com.spring.contactlist.contactapi.service.ContactService;

@RestController
@RequestMapping("/contacts")
public class ContactController {

	@Autowired
	private ContactService contactService;

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable(value = "id") String id) {
		contactService.deleteContact(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
		// return ResponseEntity.ok().body(contactService.createContact(contact));
		return ResponseEntity.ok().body(contactService.createContact(contact));
//		return ResponseEntity.created(URI.create("/contacts/userID")).body(contactService.createContact(contact));
	}

	@GetMapping
	public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		return ResponseEntity.ok().body(contactService.getAllContacts(page, size));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id) {
		return ResponseEntity.ok().body(contactService.getContact(id));
	}

	@PutMapping("/photo")
	public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
		return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
	}

	@GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
	public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
		return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
	}
}
