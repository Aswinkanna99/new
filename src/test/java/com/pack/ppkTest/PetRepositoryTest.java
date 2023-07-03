package com.pack.ppkTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pack.ppkTest.Model.Pet;
import com.pack.ppkTest.Repository.PetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PetRepositoryTest {

	@Autowired
	PetRepository petRepository;

	@Test
	public void TestInsertPet() {
		Pet p = new Pet(106, "naruto", 3, LocalDate.of(2023, 6, 6), "Navalpur");
		Pet p1 = petRepository.save(p);
		Optional<Pet> opt = petRepository.findById(p1.getId());
		Pet p2 = (Pet) opt.get();
		assertEquals(p1.getId(), p2.getId());
	}

	@Test
	public void testGetAllPets() {
		List<Pet> list = petRepository.findAll();
		List<Pet> booklist = new ArrayList<>();
		for (Pet b : list)
			booklist.add(b);
		assertThat(booklist.size()).isEqualTo(list.size());
	}

	@Test
	public void testGetPetById() {
		Pet p = petRepository.findById(102).get();
		assertEquals(p.getName(), "Sasori");
	}

	@Test
	public void testUpdatePet() {

		Pet p = petRepository.findById(101).get();
		p.setName("One Piece");
		p.setPlace("ichiraku shop");
		Pet UpdatedPet = petRepository.save(p);
		assertThat(UpdatedPet.getName()).isEqualTo("One Piece");
	}

	@Test
	public void testDeleteBook() {
		Pet p = new Pet(111, "boruto", 3, LocalDate.of(2023, 6, 6), "Navalpur");
		petRepository.save(p);
		petRepository.deleteById(106);
		Optional<Pet> deletedBook = petRepository.findById(106);
		assertFalse(deletedBook.isPresent());
	}

}
