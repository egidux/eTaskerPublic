package org.eTasker.service;

import java.nio.file.Path;
import java.util.List;

import org.eTasker.model.Signature;
import org.springframework.web.multipart.MultipartFile;

public interface SignatureService {
	
    Signature store(MultipartFile multFile, Signature file);

    Path load(Long id);
    
    List<Signature> findAll();
    
    Signature findOne(Long id);

}
