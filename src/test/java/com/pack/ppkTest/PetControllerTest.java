package com.pack.ppkTest;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pack.ppkTest.Controller.PetController;
import com.pack.ppkTest.Model.Pet;
import com.pack.ppkTest.Repository.PetRepository;
import com.pack.ppkTest.Service.PetService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PetController.class)
public class PetControllerTest {

	@Autowired
	private MockMvc mockMvc; // test the controller

	@MockBean
	private PetService petService;

	private static ObjectMapper mapper = new ObjectMapper();

	@MockBean
	PetRepository petRepository;

	@Before
	public void setup() {
		mapper.registerModule(new JavaTimeModule());
	}

	@Test
	public void testCreatePet() throws Exception {
		// Create a Pet object with the required fields
		Pet pet = new Pet();
		pet.setName("Tom");
		pet.setAge(2);
		pet.setBirthDate(LocalDate.of(2021, 1, 1));
		pet.setPlace("Home");

		// Convert the Pet object to JSON string
		String petJson = mapper.writeValueAsString(pet);

		// Perform the POST request
		mockMvc.perform(
				MockMvcRequestBuilders.post("/pets/addPet").contentType(MediaType.APPLICATION_JSON).content(petJson))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	public void testGetAllPets() throws Exception {
		List<Pet> l = new ArrayList<>();
		l.add(new Pet(106, "naruto", 3, LocalDate.of(2023, 6, 6), "Navalpur"));
		l.add(new Pet(107, "sasuke", 1, LocalDate.of(2022, 2, 7), "Navalpur"));
		Mockito.when(petService.fetchAllPets()).thenReturn(l);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/allPets")).andReturn();
		String res = result.getResponse().getContentAsString();
		List<Pet> petlist = new ArrayList<>();
		petlist = Arrays.asList(mapper.readValue(res, Pet[].class));
		assertEquals(l.size(), petlist.size());
	}

	@Test
	public void testGetPetById() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		Pet p = new Pet(102, "naruto", 3, LocalDate.of(2023, 6, 6), "Navalpur");
		Optional<Pet> optionalPet = Optional.of(p); // Wrap the pet object in an Optional
		Mockito.when(petService.findById(102)).thenReturn(optionalPet); // Return the Optional<Pet>

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/getPetById/102")).andReturn();
		String res = result.getResponse().getContentAsString();

		Pet pet = objectMapper.readValue(res, Pet.class);

		assertEquals(p.getId(), pet.getId());
		assertEquals(p.getName(), pet.getName());
		assertEquals(p.getAge(), pet.getAge());
		assertEquals(p.getBirthDate(), pet.getBirthDate());
		assertEquals(p.getPlace(), pet.getPlace());
	}

	@Test
	public void testUpdatePet() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		int id = 102;
		Pet existingPet = new Pet(102, "naruto", 3, LocalDate.of(2023, 6, 6), "Navalpur");
		Pet updatedPet = new Pet(102, "updatedNaruto", 4, LocalDate.of(2022, 5, 5), "UpdatedPlace");

		Optional<Pet> optionalPet = Optional.of(existingPet);
		Mockito.when(petService.findById(id)).thenReturn(optionalPet);
		Mockito.when(petService.insertpet(Mockito.any(Pet.class))).thenReturn(updatedPet);

		String requestBody = objectMapper.writeValueAsString(updatedPet);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/pets/updatePet/{id}", id)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();

		int statusCode = result.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), statusCode);

		String responseString = result.getResponse().getContentAsString();
		Pet responsePet = objectMapper.readValue(responseString, Pet.class);

		assertEquals(updatedPet.getName(), responsePet.getName());
		assertEquals(updatedPet.getAge(), responsePet.getAge());
		assertEquals(updatedPet.getBirthDate(), responsePet.getBirthDate());
		assertEquals(updatedPet.getPlace(), responsePet.getPlace());
	}

	@Test
	public void testDeletePet() throws Exception {
		int id = 102;

		Mockito.doNothing().when(petService).deletePetById(id);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/pets/deletePet/{id}", id)).andReturn();

		int statusCode = result.getResponse().getStatus();
		assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
	}

	public MockMvc getMockMvc() {
		return mockMvc;
	}

	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	public PetService getPetService() {
		return petService;
	}

	public void setPetService(PetService petService) {
		this.petService = petService;
	}

	public static ObjectMapper getMapper() {
		return mapper;
	}

	public static void setMapper(ObjectMapper mapper) {
		PetControllerTest.mapper = mapper;
	}

	public PetRepository getPetRepository() {
		return petRepository;
	}

	public void setPetRepository(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

}
