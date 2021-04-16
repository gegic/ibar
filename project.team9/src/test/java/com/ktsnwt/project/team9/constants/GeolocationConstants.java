package com.ktsnwt.project.team9.constants;

import com.ktsnwt.project.team9.model.Geolocation;

public class GeolocationConstants {

	public static final Long ID1 = 1L;
	public static final Long ID2 = 2L;
	public static final Long ID3 = 3L;
	public static final Long IDFORDELETE = 21L;

	public static final String LOCATION1 = "Neka lokacija 1";
	public static final String LOCATION2 = "Neka lokacija 2";
	public static final String LOCATION3 = "Location 3";

	public static final double LAT = 19.74067;
	public static final double LON = 159.73314;
	public static final double LAT2 = 62.19703;
	public static final double LON2 = 103.65479;
	public static final double LAT3 = 52.03717;
	public static final double LON3 = 21.03142;
	
	public static final double NON_EXISTING_LAT = 1;
	public static final double NON_EXISTING_LON = 1;

	public static final Geolocation GEOLOCATION1 = new Geolocation("ChIJd8BlQ2BZwokRAFUEcm_qrcA", LOCATION1, LAT, LON);
	public static final Geolocation GEOLOCATION2 = new Geolocation("ChIJd8BlQ2BZwokRAFUEcm_qrcA", LOCATION2, LAT2,
			LON2);
	public static final Geolocation GEOLOCATION3 = new Geolocation("ChIJd8BlQ2BZwokRAFUEcm_qrcA", LOCATION3, LAT3,
			LON3);
	public static final Geolocation GEOLOCATIONNEW = new Geolocation("ChIJd8BlQ2BZwokRAFUEcm_qrcA", LOCATION3, NON_EXISTING_LAT,
			NON_EXISTING_LON);
	public static final Geolocation GEOLOCATIONDELETE = new Geolocation("ChIJd8BlQ2BZwokRAFUEcm_qrcA", "Neka lokacija", 45, 45);
}
