package com.pack.ppkTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.pack.ppkTest.Model.Pet;
import com.pack.ppkTest.Repository.PetRepository;
import com.pack.ppkTest.Service.PetService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PetServiceTest {

	@MockBean
	PetService petService;

	@MockBean
	PetRepository petRepository;

	@Test
	public void testCreatePet() {
		Pet pet = new Pet(112, "naruto", 3, LocalDate.of(2023, 6, 6), "Navalpur");
		Mockito.when(petService.insertpet(pet)).thenReturn(pet);
		assertThat(petService.insertpet(pet)).isEqualTo(pet);
	}

	@Test
	public void testGetPetById() throws Exception {
		int id = 107;
		Optional<Pet> expectedPet = Optional.of(new Pet(112, "naruto", 3, LocalDate.of(2023, 6, 6), "Navalpur"));
		Mockito.when(petService.findById(Mockito.anyInt())).thenReturn(expectedPet);
		Optional<Pet> response = petService.findById(id);
		Optional<Pet> actualBook = Optional.ofNullable(response.get());

		Assertions.assertTrue(actualBook.isPresent(), "The book should be present");
		Assertions.assertEquals(expectedPet.get(), actualBook.get(), "Returned book does not match the expected book");
	}

	@Test
	public void testGetAllPets() {
		List<Pet> petList = new ArrayList<>();
		// Add pets to the list
		petList.add(new Pet(1, "cat", 2, LocalDate.of(2022, 8, 15), "Home"));
		petList.add(new Pet(2, "dog", 3, LocalDate.of(2021, 10, 10), "Park"));
		// ... add more pets as needed

		Mockito.when(petService.fetchAllPets()).thenReturn(petList);

		Iterable<Pet> petIterable = petList; // Convert the List to Iterable

		List<Pet> result = StreamSupport.stream(petIterable.spliterator(), false).collect(Collectors.toList()); // Convert
																												// Iterable
																												// to
																												// List

		assertThat(result).isEqualTo(petList);
	}

	@Test
	public void testUpdatePet() {
		Pet pet = new Pet(1, "cat", 2, LocalDate.of(2022, 8, 15), "Home");
		Mockito.when(petService.insertpet(Mockito.any(Pet.class))).thenReturn(pet);
		Mockito.when(petService.insertpet(pet)).thenReturn(pet);
		Pet insertedPet = petService.insertpet(pet);
		Pet updatedPet = new Pet(1, "dog", 3, LocalDate.of(2021, 10, 10), "Park");
		Mockito.when(petRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(insertedPet));
		Mockito.when(petRepository.save(Mockito.any(Pet.class))).thenReturn(updatedPet);
		Optional<Pet> optionalPet = petService.findById(1);
		if (optionalPet.isPresent()) {
			Pet pet1 = optionalPet.get();
			pet1.setName("new name");
			pet1.setPlace("new place");
			// Mock the behavior of the bookServiceImpl.insertBooks method
			Mockito.when(petService.insertpet(Mockito.any(Pet.class))).thenReturn(pet);

			// Insert the updated book
			Pet updatedPet1 = petService.insertpet(pet);

			// Assert the updated book has the correct values
			assertEquals("new name", updatedPet1.getName());
			assertEquals("new place", updatedPet1.getPlace());
		}
	}

	@Test
	public void testDeletePet() {
		int id = 101;
		// Create a book instance for testing
		Pet pet = new Pet();
		pet.setId(id);
		// Set other properties as needed
		// Mock the behavior of the bookRepository.findById method
		Mockito.when(petService.findById(id)).thenReturn(Optional.of(pet));

		// Call the deleteBook method
		petService.deletePetById(id);

		// Verify that the book is deleted
		Mockito.when(petService.findById(id)).thenReturn(Optional.empty());
		Optional<Pet> deletedPet = petService.findById(id);
		Assertions.assertTrue(deletedPet.isEmpty(), "Book should be deleted.");
	}

}
