package com.pack.ppkTest.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pack.ppkTest.Model.Pet;
import com.pack.ppkTest.Repository.PetRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PetServiceImpl implements PetService{

	@Autowired 
	PetRepository petRepository;
	
	@Override
	public Pet insertpet(Pet pet) {
		log.info("Inside insertpet method");
		return petRepository.save(pet);
	}

	@Override
	public Optional<Pet> findById(Integer id) {
		log.info("Inside findById method");
		return petRepository.findById(id);
	}

	@Override
	public Iterable<Pet> fetchAllPets() {
		log.info("Inside fetchAllPets method");
		return petRepository.findAll();
	}

	@Override
	public void deletePetById(Integer id) {
		log.info("Inside deletePetById method");
		petRepository.deleteById(id);
		
	}

	@Override
	public List<Pet> findByName(String name) {
		log.info("Inside findByName method");
		return petRepository.findByName(name);
	}

}
