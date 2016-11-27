package org.eTasker.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eTasker.model.Image;
import org.eTasker.model.Task;
import org.eTasker.repository.ImageRepository;
import org.eTasker.repository.TaskRepository;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageImpl implements ImageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
	private final Path LOCATION = Paths.get("images");
	
	@Autowired
	private ImageRepository fileRepository;
	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public Image store(MultipartFile multFile, Image image) {
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
        	fileRepository.save(image);
        	LOGGER.info("File:" + multFile.getOriginalFilename() + " stored");
        	Task task = taskRepository.findOne(image.getTask());
        	if (task == null) {
        		LOGGER.debug("Failed retrieve task with id=" + image.getTask());
        	} else {
        		task.setFile_exists(Boolean.TRUE);
        		taskRepository.save(task);
        		LOGGER.debug("Updated task=" + image.getTask() + " has file true");
        	}
        	return image;
        } catch (IOException e) {
        	return null;
        }
    }

	@Override
	public Path load(Long id) {
		Image image = fileRepository.findOne(id);
		if (image== null) {
			LOGGER.debug("Not found file with id=" + id);
		}
		LOGGER.info("Found file:" + JsonBuilder.build(image));
		return Paths.get(image.getPath());
	}
}
