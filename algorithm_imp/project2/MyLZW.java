package project2;
import java.lang.Math.*;

public class MyLZW {

    private static final int R = 256;                   // number of input chars

    public static void compress(String mode) { 
        int W = 9;                                      // codeword width
        int L = (int)Math.pow(2.00, W);                    // number of codewords
        double originalData = 0, compressData = 0, compressRatio = 0, currCompressRatio = 0;
        
        String input = BinaryStdIn.readString();
        originalData = input.length();                  // document the original size of the file
        
        TST<Integer> st = new TST<Integer>();   // symbol table
        for (int i = 0; i < R; i++)             // initialize to ASCII value
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF
        BinaryStdOut.write(st.get(mode), W);        // document mode type
        
        if (mode.equals("m"))                       // store originalData for monitor mode
            BinaryStdOut.write(originalData);
        
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            compressData += (W/8);                  // size of compressed data
            
            if (t < input.length()) {
                if (code < L) {                         // Add s to symbol table.
                    st.put(input.substring(0, t + 1), code++);
                } else if (W < 16){                       // expand codeword width
                    W++;
                    L = (int)Math.pow(2.00, W); 
                    st.put(input.substring(0, t + 1), code++);
                } else {                            // codebook is full
                    switch (mode) {
                        case "n":               // do nothing mode
                            break;
                        case "r":               // reset mode
                            st = new TST<Integer>();            // new symbol table
                            for (int i = 0; i < R; i++)         // re-initialize
                                st.put("" + (char)i, i);
                            code = R+1;
                            W = 9;
                            L = (int)Math.pow(2.00, W);                    // number of codewords
                            st.put(input.substring(0, t + 1), code++);
                            break;
                        case "m":               // monitor mode
                            if (compressRatio == 0){        // didn't start monitoring yet
                                compressRatio = (originalData/compressData);    // start monitorning
                            }else {                      // have started monitoring
                                currCompressRatio = (originalData/compressData);
                                if ((compressRatio/currCompressRatio ) > 1.100){
                                    st = new TST<Integer>();            // new symbol table
                                    for (int i = 0; i < R; i++)         // re-initialize
                                        st.put("" + (char)i, i);
                                    code = R+1;
                                    W = 9;
                                    L = (int)Math.pow(2.00, W);                    // number of codewords
                                    st.put(input.substring(0, t + 1), code++);
                                    compressRatio = (originalData/compressData);    // start monitorning again
                                }
                            }
                            break;
                    }
                }
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        int W = 9;
        int L = (int)Math.pow(2.00, W);
        double originalData = 0, compressData = 0, compressRatio = 0, currCompressRatio = 0;
        String[] st = new String[(int)Math.pow(2.00, 16)];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        String mode = st[BinaryStdIn.readInt(W)];       // retrieve mode
        if (mode.equals("m"))
            originalData = BinaryStdIn.readDouble();        // retrieve originalData
        compressData += (W/8);                            // keep track of compressData
        
        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            if (i == L){                        // used all possible W-bit codewords
                if (W < 16){                     // expand codeword width
                    W++;
                    L = (int)Math.pow(2.00, W);
                } else {
                    switch (mode){
                        case "n":               // do nothing mode
                            break;
                        case "r":               // reset mode
                            st = new String[(int)Math.pow(2.00, 16)];   // new symbol table
                            for (i = 0; i < R; i++)                     // initialize
                                st[i] = "" + (char) i;
                            st[i++] = "";
                            W = 9;                                      // reset W & L
                            L = (int)Math.pow(2.00, W);
                            break;
                        case "m":               // monitor mode
                            if (compressRatio == 0){                            // didn't start monitoring yet
                                compressRatio = (originalData/compressData);    // start monitoring
                            } else {                                            // have started monitoring
                                currCompressRatio = (originalData/compressData);
                                if ((compressRatio/currCompressRatio ) > 1.100){
                                    st = new String[(int)Math.pow(2.00, 16)];       // new symbol table
                                    for (i = 0; i < R; i++)                         // re-initialize
                                        st[i] = "" + (char) i;
                                    st[i++] = "";
                                    W = 9;                          // re-set W & L
                                    L = (int)Math.pow(2.00, W);
                                    compressRatio = (originalData/compressData);    //monitor compressRatio
                                }
                            }
                            break;
                    }
                }
            }
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            compressData += (W/8);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }


    public static void main(String[] args) {
        if (args[0].equals("-")) 
            compress(args[1]);          // args[1] = "n", "r", or "m"
        else if (args[0].equals("+")) 
            expand();
        else 
            throw new IllegalArgumentException("Illegal command line argument");
    }
    
}
