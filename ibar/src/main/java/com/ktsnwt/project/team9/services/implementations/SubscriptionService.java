package com.ktsnwt.project.team9.services.implementations;

import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Subscription;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.repositories.ITitleRepository;
import com.ktsnwt.project.team9.services.interfaces.ISubscriptionService;

import lombok.AllArgsConstructor;

@Service
public class SubscriptionService implements ISubscriptionService {

	@Override
	public Iterable<Subscription> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subscription getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subscription create(Subscription entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Subscription update(Long id, Subscription entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
