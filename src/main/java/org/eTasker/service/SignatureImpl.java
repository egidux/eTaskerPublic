package org.eTasker.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eTasker.model.Signature;
import org.eTasker.model.Task;
import org.eTasker.repository.SignatureRepository;
import org.eTasker.repository.TaskRepository;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SignatureImpl implements SignatureService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SignatureService.class);
	private final Path LOCATION = Paths.get("images");
	
	@Autowired
	private SignatureRepository signatureRepository;
	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public List<Signature> findAll() {
		List<Signature> images = signatureRepository.findAll();
		if (images == null) {
			LOGGER.debug("Failed to retrieve all signatures");
		}
		LOGGER.info("Signature: " + images);
		return images;
	}
	
	@Override
	public Signature findOne(Long id) {
		Signature image = signatureRepository.findOne(id);
		if (image == null) {
			LOGGER.debug("Not found signature with id=" + id);
		}
		LOGGER.info("Found signature with id=" + id);
		return image;
	}

	@Override
	public Signature store(MultipartFile multFile, Signature image) {
		if (multFile.isEmpty()) {
			LOGGER.debug("Failed store file: " + multFile.getOriginalFilename());
            return null;
        }
        try {
        	Path filePath = LOCATION.resolve(multFile.getOriginalFilename());
        	Files.copy(multFile.getInputStream(), filePath);
        	image.setCreated(TimeStamp.get());
        	image.setName(multFile.getOriginalFilename());
        	image.setPath(filePath.toString());
        	signatureRepository.save(image);
        	LOGGER.info("File:" + multFile.getOriginalFilename() + " stored");
        	Task task = taskRepository.findOne(image.getTask());
        	if (task == null) {
        		LOGGER.debug("Failed retrieve task with id=" + image.getTask());
        	} else {
        		task.setSignature_exists(Boolean.TRUE);
        		taskRepository.save(task);
        		LOGGER.debug("Updated task=" + image.getTask() + " has signature true");
        	}
        	return image;
        } catch (IOException e) {
        	return null;
        }
    }

	@Override
	public Path load(Long id) {
		List<Signature> list = signatureRepository.findAll();
		for (Signature signature: list) {
			if (signature.getTask() == id) {
				LOGGER.info("Found file:" + JsonBuilder.build(signature));
				return Paths.get(signature.getPath());
			}
		}

		LOGGER.info("Not found file task id=" + id);
		return null;
	}
}
