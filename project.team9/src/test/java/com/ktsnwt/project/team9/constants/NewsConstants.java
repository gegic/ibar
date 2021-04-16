package com.ktsnwt.project.team9.constants;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.ktsnwt.project.team9.dto.NewsDTO;

public class NewsConstants {

	public static final int PAGE_NO = 0;
	public static final int NON_EXISTING_PAGE_NO = 50;
	public static final int PAGE_SIZE = 5;
	
	public static final int TOTAL_NO = 16;
	
	
	public static final Long EXISTING_CULTURAL_OFFER_ID = 1L;
	public static final Long NON_EXISTING_CULTURAL_OFFER_ID = 300L;
	public static final Long CULTURAL_OFFER_ID_NOT_SUBSCRIBED = 5L;
	
	public static final Long EXISTING_CATEGORY_ID = 1L;
	public static final Long NON_EXISTING_CATEGORY_ID = 300L;
	
	public static final Long NEWS_ID = 1L;
	public static final Long NON_EXIST_NEWS_ID = 200L;
	
	
	public static final Long EXISTING_REGISTERED_USER_ID = 8L;
	public static final Long NON_EXISTING_REGISTERED_USER_ID = 200L;
	
	public static final long FIRST_NEWS_DATE = 1606750229093L;
	
	public static final Set<String> IMAGES = new HashSet<String>(Arrays.asList("random_slika1.jpg", "random_slika2.jpg", "random_slika3.jpg"));
	public static final NewsDTO NEWS_FOR_CREATE = new NewsDTO(null, "RNG", FIRST_NEWS_DATE, "Title 1", false, IMAGES, 1L);
	public static final NewsDTO NEWS_FOR_CREATE_NON_EXIST_ID = new NewsDTO(null, "RNG", FIRST_NEWS_DATE, "Title 1", false, IMAGES, 100L);
	
	
	
	public static final NewsDTO NEWS_FOR_UPDATE = new NewsDTO(null, "RNG_UPDATE", FIRST_NEWS_DATE, "Title 1", false, IMAGES, 1L);
	public static final NewsDTO NEWS_NO_1 = new NewsDTO(null, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur congue justo nec purus aliquam, id ultrices orci facilisis. Nullam egestas quis sapien et consectetur. Etiam quis orci at sem hendrerit tristique. Donec ante lorem, tincidunt quis molestie sit amet, pellentesque finibus felis. Phasellus pulvinar sem et elit euismod, vel mollis tortor vehicula. Duis sed scelerisque enim. In dui ipsum, placerat quis ex quis, molestie vestibulum est. Mauris lacus magna, faucibus at lorem in, fringilla luctus orci. Sed cursus posuere odio. Donec est justo, varius ac cursus eget, luctus non orci. Praesent condimentum sagittis commodo.Maecenas viverra diam ut nisl scelerisque, at hendrerit tellus porta. Nam luctus lorem ligula. Nunc ullamcorper a odio tempor vulputate. Morbi id ex pulvinar nunc lacinia interdum. Quisque faucibus ex at dolor tempor, eget aliquam leo convallis. Maecenas sit amet orci in orci suscipit convallis at ut quam. Pellentesque volutpat pretium luctus. Phasellus at odio ligula. Integer bibendum purus a nunc lacinia blandit. Nunc fringilla est odio, in varius nulla tempus ut.Sed pellentesque blandit nisl nec elementum. Duis vel metus ut nunc dapibus semper ac non tellus. Etiam suscipit ipsum id turpis interdum interdum. Cras in ullamcorper felis. Sed tincidunt, enim sed aliquet euismod, magna eros convallis nisl, suscipit pharetra ante magna eu elit. Etiam nisi enim, faucibus eget vulputate ac, lobortis vel purus. Vivamus varius sem at lectus facilisis, ut pellentesque velit aliquam. Nam finibus ullamcorper purus nec mollis. Suspendisse semper eu dolor et suscipit.Nulla fringilla lorem in libero placerat, vel consequat ligula ornare. Aliquam risus nisl, interdum in enim sit amet, suscipit dignissim dolor. In vulputate arcu nulla, id aliquam erat tincidunt eget. Vivamus erat metus, tempus id sodales volutpat, finibus et augue. Pellentesque vitae tincidunt leo, et consectetur nibh. Mauris eget orci dignissim, laoreet orci eget, dignissim metus. Pellentesque eu vulputate metus. Ut viverra mauris aliquam porttitor tincidunt. Fusce luctus rhoncus fermentum. Nam consequat condimentum lacus. Aenean ut venenatis nunc. Sed condimentum tincidunt ligula ut vestibulum.Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Maecenas elit neque, congue vitae ipsum vitae, eleifend dapibus nibh. Aliquam erat volutpat. Nulla ut turpis vehicula, egestas nisi nec, luctus ex. Aliquam venenatis leo at eros tempus, quis laoreet elit viverra. Aliquam erat volutpat. Donec sodales pulvinar ligula id pulvinar. Sed maximus lectus nibh, nec vulputate augue posuere id.Nam ac risus vehicula erat euismod tempor. Cras sagittis massa id quam sollicitudin, vitae molestie ipsum finibus. Praesent ornare sodales ultrices. Nulla vestibulum libero in erat ullamcorper, eget faucibus diam consequat. Nam hendrerit, nibh id sagittis lobortis, quam tortor sollicitudin enim, sit amet semper ante est at enim. Mauris ac magna tortor. Phasellus sit amet eros iaculis sem lacinia tempus. Curabitur sit amet tincidunt tortor. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nulla in ultrices dui. Ut et ornare massa. Sed condimentum egestas metus. Quisque vel ipsum ac tellus convallis tincidunt. Vivamus sodales ut elit id aliquam. Donec faucibus, sem ac hendrerit ultricies, metus enim consequat elit, et feugiat magna nunc sed arcu.Donec auctor arcu in nulla aliquet tristique. Morbi viverra massa a lorem pellentesque lacinia. Suspendisse cursus finibus arcu vitae blandit. Maecenas bibendum magna magna, in rhoncus turpis efficitur nec. Morbi id dapibus elit. Vivamus scelerisque, massa at ullamcorper consectetur, mi ipsum tempor eros, nec egestas libero dui a velit. Nam a laoreet erat, vel elementum purus.Sed tristique urna consequat lacus porttitor, eget malesuada ante rhoncus. Praesent ornare semper convallis. Pellentesque quis sem fringilla, rutrum eros at, faucibus erat. Proin tincidunt tempus leo, nec rhoncus ex mollis a. Aliquam placerat odio a arcu dignissim consequat. Nam facilisis a tellus in aliquet. Duis laoreet feugiat cursus. Nulla varius ipsum vel est rhoncus, quis tristique enim iaculis. Fusce in ligula in libero dictum lobortis vel eget tellus. In ut dolor ipsum. Nunc eget metus condimentum, viverra elit iaculis, ultricies nisl.Suspendisse id ante laoreet, lacinia enim porta, interdum felis. Vestibulum laoreet ipsum nulla, a lacinia orci ornare non. Proin iaculis, risus vitae dapibus consectetur, eros dolor elementum nibh, vel aliquam dui mi quis est. Quisque non sem nulla. Mauris vel nulla vel mauris ullamcorper vehicula. In euismod eleifend turpis, eget molestie mauris consequat ut. Integer.", 1606750229093L, "Title 1", false, IMAGES, 1L);
	
	

}
