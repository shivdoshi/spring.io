package com.shivang.socialapp.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shivang.socialapp.dao.PersonDAO;
import com.shivang.socialapp.model.Organization;
import com.shivang.socialapp.model.Person;

@Repository
public class PersonDAOImpl implements PersonDAO {
	
	@Autowired
    SessionFactory sessionFactory;

	/**
	 * DAO implementation of creating a Person
	 */
	public Person create(Person person) {

		Session session = sessionFactory.openSession();
		Transaction tx =  session.beginTransaction();
		try{
			session.save(person);
			tx.commit();
		} catch(HibernateException h) {
			tx.rollback();
		} finally {
			session.close();
		}
		return person;
	}

	/**
	 *  DAO implementation of get/reading a Person 
	 */
	public Person read(long id) {
		Session session = sessionFactory.openSession();
		Transaction tx =  session.beginTransaction();
		Person person = null;
		try{
			person = (Person)session.get(Person.class,id);
			tx.commit();
		} catch(HibernateException h){
			tx.rollback();
		} finally{
			session.close();
		}
		return person;
	}

	/**
	 * DAO implementation of updating a Person
	 */
	public Person update(Person person) {
		Session session = sessionFactory.openSession();
		Transaction tx =  session.beginTransaction();
		try{
			session.update(person);
			tx.commit();
		} catch(HibernateException h){
			tx.rollback();
			person = null;
		} finally{
			session.close();
		}
		return person;
	}

	/**
	 * DAO implementation of deleting/removing a Person
	 */
	public Person delete(Person person) {
		Session session = sessionFactory.openSession();
		Transaction tx =  session.beginTransaction();
		try{
			session.delete(person);
			tx.commit();
		} catch(HibernateException h){
			tx.rollback();
			person = null;
		} finally{
			session.close();
		}
		return person;
	}

	/**
	 * * DAO implementation to check if any Person belongs to the given Organization before deleting that Organization
	 */
	public Person checkPersonInOrganization(Organization org) {
		Session session = sessionFactory.openSession();
		Transaction tx =  session.beginTransaction();
		
		List<Person> persons;
		Person person=null;

		try{
			String hql = "FROM com.shivang.socialapp.model.Person as person WHERE person.org = :org";
			Query query = session.createQuery(hql);
			query.setEntity("org", org);
			query.setMaxResults(1);
			
			persons = query.list();
			
			if(persons.size()!=0)
				person = persons.get(0);			
			tx.commit();
		} catch(HibernateException h){
			tx.rollback();
		} finally{
			session.close();
		}
		return person;
	}

	/**
	 * Remove all friendship when a person is deleted
	 */
	public void removeAllFriendship(Person person) {
		Session session = sessionFactory.openSession();
		Transaction tx =  session.beginTransaction();
		
		try{
			String sql = "DELETE FROM friendship WHERE friend_id = :friend_id";
			Query query = session.createSQLQuery(sql);
			query.setParameter("friend_id", person.getId());
			
			query.executeUpdate();			
			tx.commit();
		} catch(HibernateException h){
			tx.rollback();
		} finally{
			session.close();
		}	
	}

}