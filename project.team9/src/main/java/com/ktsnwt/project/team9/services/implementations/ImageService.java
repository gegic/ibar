package com.ktsnwt.project.team9.services.implementations;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Image;
import com.ktsnwt.project.team9.repositories.IImageRepository;
import com.ktsnwt.project.team9.services.interfaces.IImageService;

@Service
public class ImageService implements IImageService {

	
	@Autowired
	private IImageRepository imageRepository;
	
	@Override
	public Collection<Image> getAll() {
		return imageRepository.findAll();
	}

	@Override
	public Image getById(Long id) {
		return imageRepository.findById(id).orElse(null);
	}

	@Override
	public Image create(Image entity) throws Exception {
		return imageRepository.save(entity);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		imageRepository.deleteById(id);
		return true;
	}

	@Override
	public Image update(Long id, Image entity) throws Exception {
		return null;
	}
}
