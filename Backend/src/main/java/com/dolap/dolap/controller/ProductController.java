package com.dolap.dolap.controller;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dolap.dolap.entities.Category;
import com.dolap.dolap.entities.Image;
import com.dolap.dolap.entities.Product;
import com.dolap.dolap.repo.CategoryRepository;
import com.dolap.dolap.repo.ImageRepository;
import com.dolap.dolap.repo.ProductRepository;
import com.dolap.dolap.repo.TokenRepository;
import com.dolap.dolap.service.AuthService;
import com.dolap.dolap.service.ImageService;
import com.dolap.dolap.service.StorageService;


@RestController 
@RequestMapping(path="/product") 
public class ProductController {
	
	@Autowired 
	private ProductRepository productRepository;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private  StorageService storageService;

	//TODO:Add or delete images separately 
	
	@GetMapping(path="/all")
	public @ResponseBody ResponseEntity<String> getAllProducts() {
		List <Product> productListDB = productRepository.findAll(); 
		ArrayList <JSONObject> result = new ArrayList<>();

		try {
			for (Product p: productListDB) {
				result.add(generateProductJsonObject(p));
			}
		
			JSONObject jsonResult = new JSONObject();
			jsonResult.put("products", result);

			return ResponseEntity.ok(jsonResult.toString());		
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}
	
	//Only get 10 products starting with given number
	@GetMapping(path="/getTen")
	public @ResponseBody Iterable<Product> getTenProduct(@RequestParam Integer start ) {
		// This returns a JSON or XML with the users
		return productRepository.findAll().subList(start, start+10);
	}
	
	//GET product/id/   -- returns product
	@GetMapping(path="/{pID}") 
	public @ResponseBody ResponseEntity<String> productGet(@PathVariable(value="pID") String id) {
		try {
			Product p = productRepository.findByPID(Integer.parseInt(id));
			JSONObject productJson  = generateProductJsonObject(p);
			
			JSONObject jsonResult = new JSONObject();
			jsonResult.put("product", productJson);
			
			return ResponseEntity.ok(jsonResult.toString());
		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	//POST /product/ --- new Product, Request type is form-data/multipart
	@PostMapping(path="/")
	public @ResponseBody ResponseEntity<String> newProduct ( 
						@RequestParam String title,
						@RequestParam String token,
						@RequestParam String description,
						@RequestParam Integer price,
						@RequestParam Integer categoryID,
						@RequestParam MultipartFile... images) {
		
		try {
			if (AuthService.isRegistered(token)){
				Product p = new Product();
				p.setCreator(tokenRepository.findByToken(token).getUser());
				p.setDescription(description);
				p.setPrice(price);
				p.setTitle(title);
				
				//Handle Images
				int counter = 0;
				for (MultipartFile img:images) {
					Image image = new Image();
					String path =  System.getProperty("user.dir") +File.separator + "media" +  File.separator + p.getTitle() + ++counter;
					image.setPath(path);
					image.setProduct(p);
					storageService.store(img, path);
					imageRepository.save(image);
				}
				
				//Handle category
				Category category = categoryRepository.findById(categoryID);

				if (category == null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category does not exist");
				}
				p.setCategory(category);
				productRepository.save(p);
			
				return ResponseEntity.ok("Created");
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");

		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	//PUT /product/id   -- editing product
	@PutMapping(path="/{pID}", consumes="application/json")
	public @ResponseBody ResponseEntity<String> productUpdate(
								@PathVariable(value="pID") String id,
								@RequestParam(required=false) String title,
								@RequestParam(required=false) String token,
								@RequestParam(required=false) String description,
								@RequestParam(required=false) Integer price,
								@RequestParam(required=false) Integer categoryId,
								@RequestParam(required=false) MultipartFile... images) {
		try {
			Product p = productRepository.findByPID(Integer.parseInt(id));
	
			if(AuthService.isOwner(token, p)){
				if (description != null && !description.isEmpty()) {
					p.setDescription(description);
				}
				if (title != null && !title.isEmpty()) {
					p.setTitle(title);
				}
				if (price != null) {
					p.setPrice(price);
				}
			
				if (images != null && images.length != 0) {
					Iterable<Image> oldImages = imageRepository.findByProduct(p);

					for(Image oldImage : oldImages) {
						imageRepository.delete(oldImage);
					}
					//Add new Images
					int counter = 0;
					for (MultipartFile img:images) {
						Image image = new Image();
						String path =  System.getProperty("user.dir") +File.separator + "media" +  File.separator + p.getTitle() + ++counter;
						image.setPath(path);
						image.setProduct(p);
						storageService.store(img, path);
						imageRepository.save(image);
					}
				}
				if (categoryId != null) {
					Category category = categoryRepository.findById(categoryId);

					if (category == null) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category does not exist");
					}
					p.setCategory(category);
				}
				productRepository.save(p);
				
				return ResponseEntity.ok("Update completed");
			}
			
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access");
		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request" + e.getMessage());
		}
	}
	
	//Delete /product/id   -- deletes product
	@DeleteMapping(path="/{pID}", consumes="application/json")
	public @ResponseBody ResponseEntity<String> productDelete(@PathVariable(value="pID") String id,  @RequestBody TokenData tokenRequestData) {
		try {
			Product p = productRepository.findByPID(Integer.parseInt(id));
	
			if(AuthService.isOwner(tokenRequestData.getToken(), p)){
				productRepository.delete(p);
				
				return ResponseEntity.ok("Delete completed");
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access");
		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request" + e.getMessage());
		}
	}
	
	//Helper for product -> JSON
	public JSONObject generateProductJsonObject(Product p) throws JSONException, UnknownHostException {
		JSONObject jsonProduct = new JSONObject();
		
		jsonProduct.put("id"         , p.getId());
		jsonProduct.put("title"      , p.getTitle());
		jsonProduct.put("description", p.getDescription());
		jsonProduct.put("price"      , p.getPrice().toString());
		jsonProduct.put("category"   , p.getCategory().getName());
		
		//Image json
        String hostname = "localhost" + ":8080";
		String [] images = new String [imageRepository.findByProduct(p).size()]; 
	
		int counter = 0;
		for(Image img : imageRepository.findByProduct(p)) {
			String urlPath = hostname + "/media/" + img.getPath().substring(img.getPath().lastIndexOf("/"));
			images[counter++] = urlPath;
		} 
		jsonProduct.put("images"     , images);
		
		return jsonProduct;
	}
	
}


//Request with token, to delete a product
class TokenData{
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}