package com.ktsnwt.project.team9.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Authority;
import com.ktsnwt.project.team9.model.VerificationToken;
import com.ktsnwt.project.team9.repositories.IAuthorityRepository;
import com.ktsnwt.project.team9.repositories.IReaderRepository;

@Service
public class AuthorityService {

	@Autowired
    private IAuthorityRepository authorityRepository;
	
	@Autowired
    private VerificationTokenService verificationTokenService;
	
	@Autowired
    private IReaderRepository registeredUserRepository;

    public List<Authority> findById(Long id) {
        Optional<Authority> auth = this.authorityRepository.findById(id);
        List<Authority> auths = new ArrayList<>();
        auth.ifPresent(auths::add);
        return auths;
    }

    public List<Authority> findByName(String name) {
        Authority auth = this.authorityRepository.findByName(name);
        List<Authority> auths = new ArrayList<>();
        if (auth != null) {
        	 auths.add(auth);
        }
        return auths;
    }

	public void confirmRegistration(String token) {
		VerificationToken vt = verificationTokenService.findByToken(token);
		if (vt != null) {
			vt.getUser().setEnabled(true);
			registeredUserRepository.save(vt.getUser());
		} else {
			throw new NoSuchElementException("Token doesn't exist.");
		}
	}
	
}
