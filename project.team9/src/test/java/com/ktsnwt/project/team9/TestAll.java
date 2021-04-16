package com.ktsnwt.project.team9;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.ktsnwt.project.team9.controller.integration.AdminControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.AuthenticationControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.CategoryControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.CommentControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.CulturalOfferControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.GeolocationControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.ImageControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.MarkControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.NewsControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.RegisteredUserControllerIntegrationTest;
import com.ktsnwt.project.team9.controller.integration.UserControllerIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.AdminRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.AuthorityRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.CategoryRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.CommentRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.CulturalOfferRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.GeolocationRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.MarkRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.NewsRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.RegisteredUserIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.UserRepositoryIntegrationTest;
import com.ktsnwt.project.team9.repository.integration.VerificationTokenRepositoryIntegrationTest;
import com.ktsnwt.project.team9.service.integration.AdminServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.AuthorityServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.CategoryServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.CommentServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.CulturalOfferServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.GenerateRandomPasswordServiceIntegrityTest;
import com.ktsnwt.project.team9.service.integration.GeolocationServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.ImageServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.MarkServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.NewsServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.RegisteredUserServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.UserServiceIntegrationTest;
import com.ktsnwt.project.team9.service.integration.VerificationTokenServiceIntegrationTest;
import com.ktsnwt.project.team9.service.unit.AdminServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.AuthorityServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.CommentServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.CulturalOfferServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.FileServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.GenerateRandomPasswordServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.GeolocationServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.MailServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.MarkServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.NewsServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.RegisteredUserServiceUnitTest;
import com.ktsnwt.project.team9.service.unit.UserServiceUnitTest;

@RunWith(Suite.class)
@SuiteClasses({ 
		CulturalOfferControllerIntegrationTest.class, GeolocationControllerIntegrationTest.class,
		CulturalOfferServiceIntegrationTest.class, GeolocationServiceIntegrationTest.class,
		GeolocationServiceUnitTest.class, CulturalOfferServiceUnitTest.class, FileServiceUnitTest.class,
		CulturalOfferRepositoryIntegrationTest.class, GeolocationRepositoryIntegrationTest.class,
		
		NewsRepositoryIntegrationTest.class, NewsControllerIntegrationTest.class, 
		NewsServiceIntegrationTest.class, NewsServiceUnitTest.class,
		CategoryControllerIntegrationTest.class, CategoryServiceIntegrationTest.class,  CategoryRepositoryIntegrationTest.class,
		ImageControllerIntegrationTest.class, ImageServiceIntegrationTest.class,
		
		AdminControllerIntegrationTest.class, CommentControllerIntegrationTest.class, RegisteredUserControllerIntegrationTest.class, 
		AdminServiceIntegrationTest.class, CommentServiceIntegrationTest.class, RegisteredUserServiceIntegrationTest.class, VerificationTokenServiceIntegrationTest.class,
		AdminServiceUnitTest.class, CommentServiceUnitTest.class, MailServiceUnitTest.class, RegisteredUserServiceUnitTest.class, 
		AdminRepositoryIntegrationTest.class, VerificationTokenRepositoryIntegrationTest.class, CommentRepositoryIntegrationTest.class, RegisteredUserIntegrationTest.class,
		UserRepositoryIntegrationTest.class,
		
		AuthenticationControllerIntegrationTest.class, MarkControllerIntegrationTest.class, UserControllerIntegrationTest.class,
		AuthorityServiceIntegrationTest.class, GenerateRandomPasswordServiceIntegrityTest.class, MarkServiceIntegrationTest.class, UserServiceIntegrationTest.class,
		AuthorityServiceUnitTest.class, GenerateRandomPasswordServiceUnitTest.class, MarkServiceUnitTest.class, UserServiceUnitTest.class,
		AuthorityRepositoryIntegrationTest.class, MarkRepositoryIntegrationTest.class,

})
@ContextConfiguration
@TestPropertySource("classpath:test.properties")
public class TestAll {

}
