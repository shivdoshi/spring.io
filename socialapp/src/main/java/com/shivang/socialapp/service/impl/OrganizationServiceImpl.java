package com.shivang.socialapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shivang.socialapp.dao.OrganizationDAO;
import com.shivang.socialapp.model.Organization;
import com.shivang.socialapp.service.OrganizationService;

@Service
public class OrganizationServiceImpl implements OrganizationService{

	@Autowired
	OrganizationDAO organizationDAO;

	@Override
	public Organization create(Organization organization) {
		return organizationDAO.create(organization);
	}

	@Override
	public Organization read(long id) {
		return organizationDAO.read(id);
	}

	@Override
	public Organization update(Organization organization) {
		return organizationDAO.update(organization);
	}

	@Override
	public Organization delete(long id) {
		
		Organization organization = organizationDAO.read(id);
		if(organization==null)
			return null;
		return organizationDAO.delete(organization);	
	}
		
}