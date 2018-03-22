package com.dolap.dolap.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.xml.ws.http.HTTPException;

import com.dolap.dolap.entities.User;
import com.dolap.dolap.entities.Token;
import com.dolap.dolap.repo.TokenRepository;
import com.dolap.dolap.repo.UserRepository;


@RestController   
@RequestMapping(path="/user") 
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;

	@GetMapping(path="/test") // Map ONLY GET Requests
	public @ResponseBody String test () {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request
		return "Works";
	}

	@PostMapping(path="/signup",consumes="application/json") 
	public @ResponseBody ResponseEntity<String> signup ( @RequestBody UserData userdata ) {
		try {
			//TODO: hashing
			//byte[] bytesOfpassword = userdata.getPassword().getBytes("UTF-8");
	
			User n = new User();
			n.setEmail(userdata.getEmail());
			n.setPassword(userdata.getPassword());
			userRepository.save(n);

			return ResponseEntity.ok("Created");
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping(path="/login", consumes="application/json")
	public @ResponseBody ResponseEntity<String> login(@RequestBody UserData userdata) {
		try {
			User user = userRepository.findByEmailAddress(userdata.getEmail());
			//TODO: hashing
			//byte[] bytesOfpassword = userdata.getPassword().getBytes("UTF-8");
			if(user.getPassword().equals(userdata.getPassword())){
				
				//no token found
				Token token = tokenRepository.findByUser(user);
				
				if(token == null ){
				    token = new Token();
					token.setUser(user);
					token.setToken(UUID.randomUUID().toString());
					tokenRepository.save(token);
				}
				JSONObject jsonString = new JSONObject("{'token': " + token.getToken() + "}");

				return ResponseEntity.ok(jsonString.toString());
			}
			//Wrong id or password
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong id or password");
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request" + e.getMessage());
		}
	}
	
	@PostMapping(path="/logout",consumes="application/json")
	public @ResponseBody ResponseEntity<String> logout(@RequestParam String token) {
		try {
			Token t = tokenRepository.findByToken(token);
			tokenRepository.delete(t);

			return ResponseEntity.ok("success");
		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request" + e.getMessage());
		}
	}

	/* 
	 *  These are for testing purposes 
	 */
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping(path="/all2")
	public @ResponseBody Iterable<Token> getAllTokens() {
		return tokenRepository.findAll();
	}
}

//JSON Request data
class UserData{
    private String email;
    private String password;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}