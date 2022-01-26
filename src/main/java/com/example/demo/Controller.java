package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/")
public class Controller {
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping(path="/login")
	public void userLogin(@RequestBody User user) {
		List<User> allUsers = userRepo.findAll();
		for(User i : allUsers) {
			String username = i.getUsername();
			String password = i.getPassword();
			if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
				throw new ResponseStatusException(HttpStatus.OK, "User Login Successful !");
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found !");
	}
	

	@GetMapping(path="/getallbooks" ,produces="application/json")
	public List<Book> getAllBook() {
		List<Book> allBooks = (List<Book>) bookRepo.findAll();
		return allBooks;
	}
	
	@GetMapping(path="/getbook/{id}",produces="application/json")
	public Book getBookById(@PathVariable("id") int id) {
		Optional<Book> book  = bookRepo.findById(id);
		if(book.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "book id not found !");
		}
		return book.get();
	}
	
	@PostMapping(path="/addbook" ,produces="application/json")
	public void addBook(@RequestBody Book book) {
		if(!bookRepo.existsById(book.getId())) {
			bookRepo.save(book);
		}
	}
	
	@PutMapping(path="/updatebook", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateBook(@RequestBody Book book) {
		bookRepo.save(book);
	}
	
	@DeleteMapping(path="/deletebook/{id}" ,produces="application/json")
	public void deleteBook(@PathVariable("id") int id) {
		bookRepo.deleteById(id);
	}
	
	@GetMapping(path="/getallauthors")
	public List<String> getAllAuthors() throws IOException {
		File file = new File("src/main/java/authors.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));

		List<String> listOfAuthors = new ArrayList<>();

		String temp = "";
	    while ((temp = br.readLine()) != null) {
	    	listOfAuthors.add(temp);	    
	    }
	    return listOfAuthors;
	}


}
