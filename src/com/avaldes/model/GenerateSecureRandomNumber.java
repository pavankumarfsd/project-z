package com.avaldes.model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class GenerateSecureRandomNumber {
 
  public static byte[] getSecureNumber() {
	byte[] seed = null;
    try {
	    
	// Create a secure random number generator using the SHA1PRNG algorithm
	SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
	
	// Get 128 random bytes
	byte[] randomBytes = new byte[128];
	secureRandomGenerator.nextBytes(randomBytes);

	// Create two secure number generators with the same seed
	int seedByteCount = 5;
	seed = secureRandomGenerator.generateSeed(seedByteCount);

	SecureRandom secureRandom1 = SecureRandom.getInstance("SHA1PRNG");
	secureRandom1.setSeed(seed);
	SecureRandom secureRandom2 = SecureRandom.getInstance("SHA1PRNG");
	secureRandom2.setSeed(seed);

    } catch (NoSuchAlgorithmException e) {
    }
 
    return seed;
  }
	
	

//	public static void main(String[] args) {
//		Random rand = new Random(123457);
//	
//		for (int j = 0; j<5; j++){
//			  int nextRandom = rand.nextInt(5);
//			  System.out.print(nextRandom+"");
//		 }
//		}
}
