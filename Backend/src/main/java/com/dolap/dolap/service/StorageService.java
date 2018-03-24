package com.dolap.dolap.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@Service
public class StorageService {

    public boolean store(MultipartFile file, String filePath) {
        if (!file.isEmpty()) {
            try {
                File dest = new File(filePath);
                file.transferTo(dest);
                return true;
            } catch (Exception e) {
            	e.printStackTrace();
			}
        }
        return false;
    }
    
    public boolean delete(String path) {
    	File file = new File(path);
        if(file.delete()){
            System.out.println("File deleted successfully");
            return true;
        }
        else {
            System.out.println("Failed to delete the file");
        }
        return false;
    }
}