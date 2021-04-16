package com.ktsnwt.project.team9.service.unit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.ktsnwt.project.team9.constants.GeolocationConstants;
import com.ktsnwt.project.team9.model.Geolocation;
import com.ktsnwt.project.team9.repositories.IGeolocationRepository;
import com.ktsnwt.project.team9.services.implementations.GeolocationService;

import javassist.NotFoundException;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class GeolocationServiceUnitTest {

	@Autowired
	GeolocationService geolocationService;

	@MockBean
	IGeolocationRepository geolocationRepository;

	@Before
	public void setup() {

		given(geolocationRepository.findById(GeolocationConstants.ID1))
				.willReturn(Optional.of(GeolocationConstants.GEOLOCATION1));
		given(geolocationRepository.findById(GeolocationConstants.ID3))
				.willReturn(Optional.of(GeolocationConstants.GEOLOCATION3));
		given(geolocationRepository.findById(GeolocationConstants.ID2)).willReturn(Optional.empty());
		given(geolocationRepository.findByLatAndLon(GeolocationConstants.LAT, GeolocationConstants.LON))
				.willReturn(Optional.of(GeolocationConstants.GEOLOCATION1));
		given(geolocationRepository.findByLatAndLon(GeolocationConstants.LAT2, GeolocationConstants.LON2))
				.willReturn(Optional.empty());
		given(geolocationRepository.findByLatAndLon(GeolocationConstants.LAT3, GeolocationConstants.LON3))
				.willReturn(Optional.empty());
		given(geolocationRepository.save(GeolocationConstants.GEOLOCATION2))
				.willReturn(GeolocationConstants.GEOLOCATION2);
		given(geolocationRepository.save(GeolocationConstants.GEOLOCATION3))
				.willReturn(GeolocationConstants.GEOLOCATION3);
		doNothing().when(geolocationRepository).deleteById(GeolocationConstants.ID1);
	}

	@Test
	public void testGetById_WithExistingId_ShouldReturnGeolocation() {
		Geolocation geolocation = geolocationService.getById(GeolocationConstants.ID1);

		verify(geolocationRepository, times(1)).findById(GeolocationConstants.ID1);
		assertEquals(GeolocationConstants.LOCATION1, geolocation.getLocation());
	}

	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		Geolocation geolocation = geolocationService.getById(GeolocationConstants.ID2);

		verify(geolocationRepository, times(1)).findById(GeolocationConstants.ID2);
		assertNull(geolocation);
	}

	@Test
	public void testGetByLatAndLon_WithExistingLatAndLon_ShouldReturnGeolocation() {
		Geolocation geolocation = geolocationService.findByLatAndLon(GeolocationConstants.LAT,
				GeolocationConstants.LON);

		verify(geolocationRepository, times(1)).findByLatAndLon(GeolocationConstants.LAT, GeolocationConstants.LON);
		assertEquals(GeolocationConstants.LOCATION1, geolocation.getLocation());
	}

	@Test
	public void testGetByLatAndLon_WithNonExistingLatAndLon_ShouldReturnNull() {
		Geolocation geolocation = geolocationService.findByLatAndLon(GeolocationConstants.LAT2,
				GeolocationConstants.LON2);

		verify(geolocationRepository, times(1)).findByLatAndLon(GeolocationConstants.LAT2, GeolocationConstants.LON2);
		assertNull(geolocation);
	}

	@Test
	public void testCreate_WithNonExistingLatAndLon_ShouldReturnCreatedGeolocation() {
		Geolocation geolocation = geolocationService.create(GeolocationConstants.GEOLOCATION2);

		verify(geolocationRepository, times(1)).save(GeolocationConstants.GEOLOCATION2);
		assertEquals(GeolocationConstants.LAT2, geolocation.getLat());
		assertEquals(GeolocationConstants.LON2, geolocation.getLon());
		assertEquals(GeolocationConstants.LOCATION2, geolocation.getLocation());
	}

	@Test(expected = EntityExistsException.class)
	public void testCreate_WithExistingLatAndLon_ShouldThrowsEntityExistsException() {
		geolocationService.create(GeolocationConstants.GEOLOCATION1);
	}

	@Test
	public void testDelete_WithExistingId_ShouldReturnTrue() throws NotFoundException {
		Boolean status = geolocationService.delete(GeolocationConstants.ID1);

		verify(geolocationRepository, times(1)).findById(GeolocationConstants.ID1);
		verify(geolocationRepository, times(1)).deleteById(GeolocationConstants.ID1);
		assertTrue(status);
	}

	@Test(expected = NotFoundException.class)
	public void testDelete_WithNonExistingId_ShouldThrowsNotFoundException() throws NotFoundException {
		geolocationService.delete(GeolocationConstants.ID2);
	}

}
