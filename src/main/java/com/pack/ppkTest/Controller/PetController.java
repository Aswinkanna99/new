package com.pack.ppkTest.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pack.ppkTest.Model.Pet;
import com.pack.ppkTest.Repository.PetRepository;
import com.pack.ppkTest.Service.PetService;

import lombok.extern.log4j.Log4j2;



@RestController
@RequestMapping("/pets")
@Log4j2
public class PetController {

	@Autowired
	PetRepository petRepository;

	@Autowired
	PetService petService;

	/**
	 * This method is used to add new pet
	 * 
	 * @param pet
	 * @return ResponseEntity
	 */
	@PostMapping("/addPet")
	public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
		log.info("Inside createPet method");
		try {
			Pet pet1 = petService.insertpet(pet);
			return new ResponseEntity<>(pet1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to find pet by its id
	 * 
	 * @param id
	 * @return ResponseEntity
	 */
	@GetMapping("/getPetById/{id}")
	public ResponseEntity<Pet> getPetById(@PathVariable("id") Integer id) {
		log.info("Inside creatPet method");
		Optional<Pet> petData = petService.findById(id);
		if (petData.isPresent()) {
			return new ResponseEntity<>(petData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * This method is used to fetch all the pets in database
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/allPets")
	public ResponseEntity<List<Pet>> getAllPets() {
		log.info("Inside getAllPets method");
		try {
			List<Pet> pets = new ArrayList<Pet>();
			petService.fetchAllPets().forEach(pets::add);
			if (pets.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(pets, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to update the pet information
	 * 
	 * @param id
	 * @param pet
	 * @return ResponseEntity
	 */
	@PutMapping("/updatePet/{id}")
	public ResponseEntity<Pet> updatePet(@PathVariable("id") Integer id, @RequestBody Pet pet) {
		log.info("Inside updatePet method");
		Optional<Pet> petData = petService.findById(id);

		if (petData.isPresent()) {
			Pet pet1 = petData.get();
			pet1.setName(pet.getName());
			pet1.setAge(pet.getAge());
			pet1.setBirthDate(pet.getBirthDate());
			pet1.setPlace(pet.getPlace());
			return new ResponseEntity<>(petService.insertpet(pet1), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * This method is used to delete pet by id
	 * 
	 * @param id
	 * @return ResponseEntity
	 */
	@DeleteMapping("/deletePet/{id}")
	public ResponseEntity<HttpStatus> deletePet(@PathVariable("id") Integer id) {
		log.info("Inside deletePet method");
		try {
			petService.deletePetById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to find pet by name
	 * 
	 * @param name
	 * @return ResponseEntity
	 */
	@GetMapping("/deletePetByName/Name")
	public ResponseEntity<List<Pet>> getPetByName(@RequestParam("name") String name) {
		log.info("Inside getPetByName method");
		List<Pet> petData = petService.findByName(name);

		if (petData.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(petData, HttpStatus.OK);
	}

	public PetRepository getPetRepository() {
		return petRepository;
	}

	public void setPetRepository(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public PetService getPetService() {
		return petService;
	}

	public void setPetService(PetService petService) {
		this.petService = petService;
	}

}
