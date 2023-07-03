package com.pack.ppkTest.Service;

import java.util.List;
import java.util.Optional;

import com.pack.ppkTest.Model.Pet;

public interface PetService {

	Pet insertpet(Pet pet);

	Optional<Pet> findById(Integer id);

	Iterable<Pet> fetchAllPets();

	void deletePetById(Integer id);

	List<Pet> findByName(String name);

}
