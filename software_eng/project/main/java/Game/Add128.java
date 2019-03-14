// CS 1501 Summer 2017
// Interface for symmetric ciphers to be used with Assignment 4.  This must be
// implemented by both your Add128 and Substitute classes.
package Game;

import java.util.*;
import java.io.*;
import java.net.*;
import java.math.*;
import java.util.Random;

public class Add128 //implements SymCipher													//byte key should be used as some sort of Key storage here
{
	public byte[] Key;
	
	//the one with a parameter
	public Add128(byte [] byteKey){
		Key = byteKey;
	}
	//the one without the parameter
	public Add128(){					// i need to change the random here into the same byte
		int byteSize = 128;
		int shiftedByte = 10;
		Key = new byte[byteSize]; 			// 16 bytes = 128 bits
			for(int i = 0; i < byteSize; i++){	//shift 10 byte everytime
				Key[ i ] = (byte) shiftedByte;
			}
		
		
	}
	// Return an array of bytes that represent the key for the cipher
	public byte [] getKey(){
		return Key;
	}	
	
	// Encode the string using the key and return the result as an array of
	// bytes.  Note that you will need to convert the String to an array of bytes
	// prior to encrypting it.  Also note that String S could have an arbitrary
	// length, so your cipher may have to "wrap" when encrypting.
	
	//as long as i can remember, the encode part need to convert the string into byte[] then add the byte[] with Key?
	public byte [] encode(String S){
		byte[] value = S.getBytes();
		int i = 0;			//for counting the total int inside of the value byte[]
		int j= 0;			// for counting the toatl
		
			while( i < value.length){
				if(j >= 127){		//value is longer than key, we loop back and reuse the key
					j = 0;
				  }
				value[i] = (byte)(value[ i ] + Key[ j ]);
				j++;
				i++;
			}
		// so around here, we need to make sure the RSA is positive and check if the sum exceeds 128?	
		return value;
	}
	
	// Decrypt the array of bytes and generate and return the corresponding String.
	// decode will theoretically be the opposite of encode, which substract byte[] from the original bytes[] and convert them back into strings
	public String decode(byte [] bytes){
		int i = 0;
		int j = 0;
		
			while( i < bytes.length){
				if(j >= 127 ){
					j = 0;
				}
				bytes[ i ] =  (byte)(bytes[ i ] - Key[ j ]);
			j++;
			i++;
			}
		String temp = new String( bytes );
		
		return temp;
	}	
	
	public static void main(String [] args) throws IOException
    {
         Add128 cipher = new Add128();
		 byte[] sheetz = cipher.encode("sheetz");
		 String wawa = cipher.decode(sheetz);
		 
		 byte[] key = cipher.getKey();
		 Add128 newCipher = new Add128(key);
		 byte[] sheetz1 = cipher.encode("sheetz");
		 String wawa1 = cipher.decode(sheetz1);
		 
    }
}