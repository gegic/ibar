package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.GeolocationConstants;
import com.ktsnwt.project.team9.model.Geolocation;
import com.ktsnwt.project.team9.services.implementations.GeolocationService;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class GeolocationServiceIntegrationTest {

	@Autowired
	GeolocationService geolocationService;

	@Test
	public void testGetAll_ShouldReturnAllGeolocations() {
		List<Geolocation> iterable = (List<Geolocation>) geolocationService.getAll();

		assertEquals(21, iterable.size());
	}

	@Test
	public void testGetAll_WithFirstPageable_ShouldReturnFirst10Geolocations() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<Geolocation> iterable = geolocationService.getAll(pageable);

		assertEquals(10, iterable.getNumberOfElements());
	}

	@Test
	public void testGetAll_WithSecondPageable_ShouldReturnSecond10Geolocations() {
		Pageable pageable = PageRequest.of(1, 10);

		Page<Geolocation> iterable = geolocationService.getAll(pageable);

		assertEquals(10, iterable.getNumberOfElements());
	}

	@Test
	public void testGetAll_WithThirdPageable_ShouldReturnOneGeolocation() {
		Pageable pageable = PageRequest.of(2, 10);

		Page<Geolocation> iterable = geolocationService.getAll(pageable);

		assertEquals(1, iterable.getNumberOfElements());
	}

	@Test
	public void testGetAll_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(3, 10);

		Page<Geolocation> iterable = geolocationService.getAll(pageable);

		assertEquals(0, iterable.getNumberOfElements());
	}

	@Test
	public void testGetById_WithExistingId_ShouldReturnGeolocationWithThatId() {
		Geolocation geolocation = geolocationService.getById(2L);

		assertEquals(GeolocationConstants.LOCATION2, geolocation.getLocation());
	}

	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		Geolocation geolocation = geolocationService.getById(55L);

		assertNull(geolocation);
	}

	@Test
	public void testFindByLatAndLon_WithExistingLatAndLon_ShouldReturnGeolocationWithThatLatAndLon() {
		Geolocation geolocation = geolocationService.findByLatAndLon(GeolocationConstants.LAT,
				GeolocationConstants.LON);

		assertEquals(GeolocationConstants.LOCATION2, geolocation.getLocation());
	}

	@Test
	public void testFindByLatAndLon_WithNonExistingLatAndLon_ShouldReturnNull() {
		Geolocation geolocation = geolocationService.findByLatAndLon(GeolocationConstants.NON_EXISTING_LAT,
				GeolocationConstants.NON_EXISTING_LON);

		assertNull(geolocation);
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithNonExistingLAtAndLon_ShouldCreateNewGeolocation() {
		int length = ((List<Geolocation>) geolocationService.getAll()).size();

		Geolocation geolocation = geolocationService.create(GeolocationConstants.GEOLOCATIONNEW);

		assertEquals(GeolocationConstants.GEOLOCATIONNEW.getLocation(), geolocation.getLocation());
		assertEquals(length + 1, ((List<Geolocation>) geolocationService.getAll()).size());
	}

	@Test(expected = EntityExistsException.class)
	public void testCreate_WithExistingLAtAndLon_ShouldThrowsEntityExistsException() {
		geolocationService.create(GeolocationConstants.GEOLOCATION1);
	}

	@Test(expected = NotFoundException.class)
	public void testDelete_WithNonExistingId_ShouldThrowsNotFoundException() throws NotFoundException {
		geolocationService.delete(55L);
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingId_ShouldReturnTrue() throws NotFoundException {
		int length = ((List<Geolocation>) geolocationService.getAll()).size();

		Boolean state = geolocationService.delete(GeolocationConstants.IDFORDELETE);

		assertEquals(true, state);
		assertEquals(length - 1, ((List<Geolocation>) geolocationService.getAll()).size());
	}
}
