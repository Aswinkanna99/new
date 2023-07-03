package com.pack.ppkTest.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.ppkTest.Model.Pet;
@Repository
public interface PetRepository extends JpaRepository<Pet,Integer>{

	List<Pet> findByName(String name);

}
