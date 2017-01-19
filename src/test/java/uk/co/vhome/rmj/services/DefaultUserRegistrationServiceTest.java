package uk.co.vhome.rmj.services;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Should actually test something!
 */
public class DefaultUserRegistrationServiceTest
{
	@Test
	public void generateRandomPassword() throws Exception
	{
		char chars[] = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
		                          'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
		                          '0','1','2','3','4','5','6','7','8','9','!','@','£','$','%','^','&','*','(',')','-','+','=','#','_','?'};

		StringBuilder stringBuilder = new StringBuilder(10);

		for (int i = 0; i < 10; ++i )
		{
			char c = chars[(int)Math.round(Math.random() * chars.length)];
			stringBuilder.append(c);
		}

		String generatedPassword = stringBuilder.toString();
		System.out.println(generatedPassword);

		String hashpw = BCrypt.hashpw(generatedPassword, BCrypt.gensalt());

		assert BCrypt.checkpw(generatedPassword, hashpw);

	}
}