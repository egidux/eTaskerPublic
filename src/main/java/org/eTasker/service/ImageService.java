package org.eTasker.service;

import java.nio.file.Path;
import java.util.List;

import org.eTasker.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Image store(MultipartFile multFile, Image file);

    Path load(Long taskID, Long imageID);
    
    List<Image> findAll(Long taskID);
    
    Image findOne(Long id);
}
