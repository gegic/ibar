package com.ktsnwt.project.team9.constants;

import com.ktsnwt.project.team9.dto.GeolocationDTO;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.Geolocation;
import com.ktsnwt.project.team9.model.Image;

public class CulturalOfferConstants {

	public static final Long ID1 = 1L;
	public static final Long ID2 = 2L;

	public static final String NAME1 = "Manastir 1";
	public static final String NAME2 = "Ponuda 2";
	public static final String NAME3 = "Ponuda 3";

	public static final String DESCRIPTION1 = "Description 1";
	public static final String DESCRIPTION2 = "Description 2";
	public static final String DESCRIPTION3 = "Description 3";

	public static final double LAT = 19.74067;
	public static final double LON = 159.73314;
	public static final double LAT2 = 62.19703;
	public static final double LON2 = 103.65479;

	public static final Geolocation GEOLOCATION1 = new Geolocation("ChIJd8BlQ2BZwokRAFUEcm_qrcA", "Neka lokacija 1",
			LAT, LON);
	public static final Geolocation GEOLOCATION2 = new Geolocation("ChIJd8BlQ2BZwokRAFUEcm_qrcA", "Neka lokacija 2",
			LAT2, LON2);

	public static final Category CATEGORY1 = new Category("Name1", "DESCRIPTION1", true);
	public static final Long CATEGORY2ID = 2L;
	public static final Category CATEGORY2 = new Category("Name2", "DESCRIPTION2", true);
	public static final Long CATEGORY3ID = 3L;
	public static final Category CATEGORY3 = new Category(CATEGORY3ID, "Name3", "DESCRIPTION3", true);

	public static final Admin ADMIN1 = new Admin(1L);

	public static final String IMAGEURL = "URL_DO_SLIKE";

	public static final Image IMAGE = new Image(IMAGEURL);

	public static final int PAGE = 0;
	public static final int PAGE_SIZE = 5;

	public static final Long NON_EXISTING_ID = 20L;

	public static final String SUBSTRING_NAME = "anast";

	public static final String NEW_OFFER_NAME = "New offer";
	public static final String NEW_OFFER_DESCRIPTION = "Some description";
	public static final Geolocation NEW_OFFER_GEOLOCATION = new Geolocation("someId", "Some location", 49, 18);
	public static final GeolocationDTO NEW_OFFER_GEOLOCATION_DTO = new GeolocationDTO("someId", "Some location", 49, 18);
	public static final Category NEW_OFFER_CATEGORY = new Category(5L);
	public static final Category NON_EXISTING_OFFER_CATEGORY = new Category(55L);
	public static final Admin NEW_OFFER_ADMIN = new Admin(2L);
	public static final CulturalOffer EXISTING_CULTURAL_OFFER = new CulturalOffer("Manastir 1", NEW_OFFER_DESCRIPTION,
			NEW_OFFER_GEOLOCATION, NEW_OFFER_CATEGORY, new Admin(1L));
	public static final GeolocationDTO GEOLOCATION_EXIST_DTO = new GeolocationDTO("ChIJd8BlQ2BZwokRAFUEcm_qrcA",
			"Neka lokacija 1", LAT, LON);

}
