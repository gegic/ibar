package com.ktsnwt.project.team9.constants;

import com.ktsnwt.project.team9.dto.CategoryDTO;
import com.ktsnwt.project.team9.model.Category;

public class CategoryConstants {

	public static final int PAGE_NO = 0;
	public static final int NON_EXISTING_PAGE_NO = 50;
	public static final int PAGE_SIZE = 5;
	public static final String EXISTING_VALUE = "Manastiri";

	public static final int TOTAL_NO = 12;

	public static final Long EXISTING_CATEGORY_ID= 11L;
	public static final Long NON_EXISTING_CATEGORY_ID = 300L;
	
	public static final Category CATEGORY_NO_1 = new Category("Manastiri","Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vestibulum dapibus nisl sit amet laoreet. Etiam congue convallis egestas. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere.",true);
	
	public static final String EXISTING_DESC = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vestibulum dapibus nisl sit amet laoreet. Etiam congue convallis egestas. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere.";
	public static final String EXISTING_NAME = "Manastiri";
	
	public static final CategoryDTO CATEGORY_FOR_CREATE = new CategoryDTO(null, "Category New", "Good category", true);
	
	public static final CategoryDTO CATEGORY_FOR_UPDATE = new CategoryDTO(null, "Category Update", "Updated good category", true);
}
