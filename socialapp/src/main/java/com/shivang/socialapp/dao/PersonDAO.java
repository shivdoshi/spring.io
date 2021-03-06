package com.shivang.socialapp.dao;

import com.shivang.socialapp.model.Organization;
import com.shivang.socialapp.model.Person;

public interface PersonDAO {
	 
    /**
     * create a new person
     * @param person
     * @return
     */
	public Person create(Person person);
    
	/**
	 * get a person details
	 * @param person
	 * @return
	 */
    public Person read(long id);
    
    /**
     * update person's details
     * @param person
     * @return
     */
    public Person update(Person person);	
    
    /**
     * delete person entry
     * @param person
     * @return
     */
    public Person delete(Person person);
    
    /**
     * To check if any Person belongs to the Organization
     * @param org
     * @return
     */
    public Person checkPersonInOrganization(Organization org);
    
    /**
     * To remove friendship when a person is deleted/removed
     * @param person
     */
    public void removeAllFriendship(Person person);
    
}