package org.eTasker.service;

import java.nio.file.Path;

import org.eTasker.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Image store(MultipartFile multFile, Image file);

    Path load(Long id);
}
