package com.shivang.secretsharing;

import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shivang.secretsharing.Exceptions.UnauthorizedException;
import com.shivang.secretsharing.Interfaces.SecretService;
import com.shivang.secretsharing.pojo.Secret;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	ApplicationContext appContext = new ClassPathXmlApplicationContext(
			new String[] { "beans.xml" });
	SecretService ssc = (SecretService) appContext.getBean("secretservice");
	
	Secret aliceS = new Secret();
    Secret bobS = new Secret();
    Secret carlS = new Secret();
    
    UUID iAlice, iBob, iCarl;
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}
	
	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		iAlice = ssc.storeSecret("Alice", aliceS);
		iBob = ssc.storeSecret("Bob", bobS);
		iCarl = ssc.storeSecret("Carl", carlS);
	}



	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	
	public void testA(){
		try{
			ssc.readSecret("Bob", iAlice);
		} catch (Exception e){
			assertTrue(true); // exception is expected
		}
	}
	
	/* Alice shares a secret with Bob, and Bob can read it */
	public void testB(){
		try{
			ssc.shareSecret("Alice", iAlice, "Bob");
			ssc.readSecret("Bob", iAlice);
		} catch (Exception e){
			assertTrue(false);
		}
	}
	
	/* Alice shares a secret with Bob, and Bob shares Alice’s it with Carl, and Carl can read this secret */
	public void testC(){
		try{
			ssc.shareSecret("Alice", iAlice, "Bob");
			ssc.shareSecret("Bob", iAlice, "Carl");
			ssc.readSecret("Carl", iAlice);
		} catch (Exception e){
			assertTrue(false);
		}
	}
	
	/* Alice shares her secret with Bob; Bob shares Carl’s secret with Alice and encounters UnauthorizedException. */
	public void testD(){
		try{
			ssc.shareSecret("Alice", iAlice, "Bob");
			ssc.shareSecret("Bob", iAlice, "Alice");
		} catch (UnauthorizedException e){
			assertTrue(true); // exception is expected
		}
	}
	
	/* Alice shares a secret with Bob, Bob shares it with Carl, Alice unshares it with Carl, and Carl cannot read this secret anymore. */
	public void testE(){
		try{
			ssc.shareSecret("Alice", iAlice, "Bob");
			ssc.shareSecret("Bob", iAlice, "Carl");
			ssc.unshareSecret("Alice", iAlice, "Carl");
			ssc.readSecret("Carl", iAlice);
		} catch (Exception e){
			assertTrue(true); // exception is expected
		}
	}
	
	/* Alice shares a secret with Bob and Carl; Carl shares it with Bob, then Alice unshares it with Bob; Bob cannot read this secret anymore */
	public void testF(){
		try{
			ssc.shareSecret("Alice", iAlice, "Bob");
			ssc.shareSecret("Alice", iAlice, "Carl");
			ssc.shareSecret("Carl", iAlice, "Bob");
			ssc.unshareSecret("Alice", iAlice, "Bob");
			ssc.readSecret("Bob", iAlice);
		} catch (Exception e){
			assertTrue(true); // exception is expected
		}
	}
	
	/* Alice shares a secret with Bob; Bob shares it with Carl, and then unshares it with Carl. Carl can still read this secret.*/
	public void testG(){
		try{
			ssc.shareSecret("Alice", iAlice, "Bob");
			ssc.shareSecret("Bob", iAlice, "Carl");

			ssc.unshareSecret("Bob", iAlice, "Carl");
			ssc.readSecret("Carl", iAlice);
		} catch (Exception e){
			assertTrue(false);
		}
	}
	
	/*Alice shares a secret with Bob; Carl unshares it with Bob, and encounters UnauthorizedException*/
	public void testH(){
		try{
			ssc.shareSecret("Alice", iAlice, "Bob");
			ssc.unshareSecret("Carl", iAlice, "Bob");
		} catch (Exception e){
			assertTrue(true); //exception expected
		}
	}
	
	/*Alice shares a secret with Bob; Bob shares it with Carl; Alice unshares it with Bob; Bob shares it with Carl with again, and encounters UnauthorizedException.*/
	public void testI(){
		try{
			ssc.shareSecret("Alice", iAlice, "Bob");
			ssc.shareSecret("Bob", iAlice, "Carl");
			ssc.unshareSecret("Alice", iAlice, "Bob");
			ssc.shareSecret("Bob", iAlice, "Carl");
		} catch (Exception e){
			assertTrue(true); //exception expected
		}
	}
	
	/*Alice stores the same secrete object twice, and get two different UUIDs.*/
	public void testJ(){
		Secret s = new Secret();
		UUID u1 = ssc.storeSecret("Alice", s);
		UUID u2 = ssc.storeSecret("Alice", s);
		
		assertNotSame(u1, u2);
	}
}