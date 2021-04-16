package com.ktsnwt.project.team9.repository.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.GeolocationConstants;
import com.ktsnwt.project.team9.model.Geolocation;
import com.ktsnwt.project.team9.repositories.IGeolocationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class GeolocationRepositoryIntegrationTest {

	@Autowired
	IGeolocationRepository geolocationRepository;

	@Test
	public void testFindByLatAndLon_WithExistingLatAndLon_ShouldReturnOptionalWithGeolocation() {
		Optional<Geolocation> optional = geolocationRepository.findByLatAndLon(GeolocationConstants.LAT,
				GeolocationConstants.LON);

		assertEquals(GeolocationConstants.LOCATION2, optional.get().getLocation());
	}

	@Test
	public void testFindByLatAndLon_WithNonExistingLatAndLon_ShouldReturnEmptyOptional() {
		Optional<Geolocation> optional = geolocationRepository.findByLatAndLon(GeolocationConstants.NON_EXISTING_LAT,
				GeolocationConstants.NON_EXISTING_LON);

		assertTrue(!optional.isPresent());
	}
}
