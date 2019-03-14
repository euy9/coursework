package Game;

import java.util.*;
import java.io.*;

/** 
 * Usage sample serializing SomeClass instance 
 */
public class EncryptDecryptData {
	static Add128 cipher = new Add128();	
	
	public EncryptDecryptData(){		//constructors
		
	}
    public static void main( String [] args ){
		
	}
	/**
	* change the saveGame instance into a string and encode the string
	*/
	public static byte[] returnEncoded(SaveGame data) throws IOException,
                                                      ClassNotFoundException{
		String string = toString( data );	
		return cipher.encode(string);
		
	}
	
	/**
	* decode the byte array into a string, then convert the string back to a object
	* basically the base64 is a way to convert string back to the serializable object
	*/
	public static SaveGame returnDecoded(byte[] encoded)throws IOException,
                                                      ClassNotFoundException {
		String decode = cipher.decode(encoded);
        return ( SaveGame ) fromString( decode );
		
	}
    /** Read the object from Base64 string. */
	//This class implements a decoder for decoding byte data using the Base64 encoding scheme as specified in RFC 4648 and RFC 2045.
	// Decodes a Base64 encoded String into a newly-allocated byte array using the Base64 encoding scheme.
	// credit of the bases64 code goes to https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
   private static Object fromString( String s ) throws IOException ,
                                                       ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();		//return the object 
        ois.close();
        return o;
   }

    /** Write the object to a Base64 string. */
	//encode bytearray stream into string
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }
}
