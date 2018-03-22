package com.dolap.dolap.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.xml.ws.http.HTTPException;

import com.dolap.dolap.entities.User;
import com.dolap.dolap.entities.Token;
import com.dolap.dolap.repo.TokenRepository;
import com.dolap.dolap.repo.UserRepository;


@Controller   
@RequestMapping(path="/media") 
public class MediaController {
	

	
	//To show image
	@GetMapping(value = "/{<mediaName>}", produces= MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<InputStreamResource> getImage(@PathVariable(value="<mediaName>") String name) {
		String filePath =  System.getProperty("user.dir") + File.separator + "media" +  File.separator + name;
		
	    try {
	    	File image = new File(filePath);
			InputStream imageStream = new FileInputStream(image);
	    	return ResponseEntity
			        .ok()
			        .body(new InputStreamResource(imageStream));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
