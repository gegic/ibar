package com.sbnz.ibar.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.sbnz.ibar.model.Authority;
import com.sbnz.ibar.repositories.AuthorityRepository;
import com.sbnz.ibar.repositories.ReaderRepository;

@Service
@AllArgsConstructor
public class AuthorityService {


    private final AuthorityRepository authortyRepository;

    private final ReaderRepository regsteredUserRepository;

    public List<Authority> findById(long id) {
        Optional<Authority> auth = this.authortyRepository.findById(id);
        List<Authority> auths = new ArrayList<>();
        auth.ifPresent(auths::add);
        return auths;
    }

    public List<Authority> findByName(String name) {
        Authority auth = this.authortyRepository.findByName(name);
        List<Authority> auths = new ArrayList<>();
        if (auth != null) {
        	 auths.add(auth);
        }
        return auths;
    }
	
}
