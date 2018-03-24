package com.dolap.dolap.controller;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dolap.dolap.entities.Category;
import com.dolap.dolap.entities.Image;
import com.dolap.dolap.entities.Product;
import com.dolap.dolap.repo.CategoryRepository;
import com.dolap.dolap.repo.ImageRepository;
import com.dolap.dolap.repo.ProductRepository;
import com.dolap.dolap.service.AuthService;


@RestController 
@RequestMapping(path="/category/") 
public class CategoryController {
	
	@Autowired 
	private ProductRepository productRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	

	
	//GET category/id/   -- returns products of that category
	@GetMapping(path="/{cID}") 
	public @ResponseBody ResponseEntity<String> productGet(@PathVariable(value="cID") String id) {
		try {

			Category category = categoryRepository.findById(Integer.parseInt(id));
			
			//Find sub categories and append it subCategories list
			Queue<Category> queue = new LinkedBlockingQueue<>();
			List <Category> subCategories = new ArrayList<>();

			queue.add(category);
			subCategories.add(category);
			
			while(!queue.isEmpty()) {
				List <Category> subs = categoryRepository.findByParentId(queue.poll().getId());
				
				if (!subs.isEmpty()) {
					for (Category sub : subs) {
						queue.add(sub);
						subCategories.add(sub);
					}
				}
			}
			List <Product> products   = new ArrayList<>();

			//Add products from all sub categories
			for(Category subCategory : subCategories) {
				List <Product> productsByCategory = productRepository.findByCID(subCategory.getId());
	
				if (!productsByCategory.isEmpty()) {
					products.addAll(productsByCategory);
				}
			}
			
			List<JSONObject> productsList = new ArrayList<JSONObject>(); 

			for (Product p: products) {
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
				
				productsList.add(jsonProduct);
			}

			
			//Result json
			JSONObject jsonResult = new JSONObject();
			jsonResult.put("categoryID"  , category.getId());
			jsonResult.put("categoryName", category.getName());
			jsonResult.put("parentID", category.getParentId());
			jsonResult.put("products", productsList);
						
			return ResponseEntity.ok(jsonResult.toString());
		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	//POST /category/add/ new category, 
	@PostMapping(path="/add/")
	public @ResponseBody ResponseEntity<String> newProduct (@RequestBody CategoryData categoryRequestData) {
		
		try {
			if (AuthService.isRegistered(categoryRequestData.getToken())){

				Category c = new Category(categoryRequestData.getId());
				c.setId(categoryRequestData.getId());
				c.setName(categoryRequestData.getName());
				c.setParentId(categoryRequestData.getParentID());

				categoryRepository.save(c);
				return ResponseEntity.ok("Created");
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");

		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	//Delete /category/id   -- deletes category
	@DeleteMapping(path="/delete/{cID}", consumes="application/json")
	public @ResponseBody ResponseEntity<String> categoryDelete(@PathVariable(value="cID") String id,  @RequestBody TokenData tokenRequestData) {
		try {
			Category c = categoryRepository.findById(Integer.parseInt(id));
	
			if(AuthService.isRegistered(tokenRequestData.getToken())){
				categoryRepository.delete(c);
				
				return ResponseEntity.ok("Delete completed");
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access");
		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request" + e.getMessage());
		}
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Category> getAllProducts() {
		return categoryRepository.findAll();
	}
	
}

class CategoryData {
	private Integer id;
	private String name;
	private Integer parentID;
	private String token;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentID() {
		return parentID;
	}
	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}


