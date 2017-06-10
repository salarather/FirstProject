public class StringConverter
{

    //Declaring instance variables of the class
    private int[] binary;
    private String message;
    private int[] bitMessage;

    public StringConverter()
    {
        
    }

    public static int[] convert2Bits(String text){

        //declare a new array of size 8 times the length of the text because every character has an
        //8 bit ASCII value.
        int[] bits = new int[8*text.length()];

        for (int i = 0; i < text.length(); i++){

            //extact the ASCII value of the character at that position of the string.
            int val = text.charAt(i);

            //convert that ASCII value to its 8 bit binary form
            int[] bit = convertFromInt(val);

            //copy the array into a new array of size 8 greater than the current so that a new 8 bit
            //ASCII value of the character can be stored in there
            System.arraycopy(bit, 0, bits, i*8, 8);

        }

        return bits;

    }

    public static String convert2String(int[] bits){

        int[] bitMessage = new int[8];

        String message = "";

        int count = 0;
        int bitCount = 0;

        for (int i = 0; i < (bits.length)/8; i++){
            //the outside loop runs for the amount of characters which are one-eighth the amount of bits
            bitCount = 0;

            for (int j = count; j < (count + 1); j++){
                
                //writing the inner loop this way along with the if statement allows the inner loop 
                //to run only 8 times while incrementing the value of count so that j starts off 8 
                //values ahead every time the outer loop is run again.

                if(bitCount < 8){
                    
                    
                    //store the value of the passed array at that position of j into the bitMessage
                    //array at the value of bitCount which starts off from 0 every time the outer
                    //loop is run so that we can extract exactly 8 bits
                    bitMessage[bitCount] = bits[j];

                
                    count++;
                    bitCount++;
                    
                }

            }

            //convert the bits to their integer value
            int ASCII = convertToInt(bitMessage);

            //convert the integer value to its corresponding ASCII character
            char character = (char) ASCII;

            if (ASCII != 0){

                //As long as the ASCII value is not 0 append the character to text message
                message =  message + character;

            }

            
        }

        //return the text
        return message;

    }

    private static int[] convertFromInt(int x){

        //declare an array to store the 8 bit binary value of the integer
        int[] binary = new int[8];

        double y = (double) x;

        while (y >= 1){

            for (int count = 7; count >= 0; count--){

                if (y % 2 == 0) {
                    
                    //start adding from the end of the array
                    
                    //if y at this point is even, add 0 at that particular position of the array
                    //for example for the integer 66, we start from the least significant bit and
                    //add 0 to that point: this makes sense since even numbers always have 0 as 
                    //their least significant bit.
                    binary[count] = 0;

                    //divide y by 2 and repeat
                    y = y/2;

                }

                else {

                    
                    //if y at this point is odd, add 1 to that particular position of the arrayq
                    //for example for the integer 65, we start from the least significant bit and
                    //add 1 at that point in the array: this makes sense since odd numbers always 
                    //have 1 as their least significant bit
                    binary[count] = 1;

                    y = y/2 - 0.5;

                }

            }
            
        }
        
        //return the 8 bit binary number of the integer
        return binary;
        
    }
    
    private static int convertToInt(int[] bits){
        
        //using the logic provided in the lab,
        int val = 0;
        
        for (int i = 0; i < bits.length; i++){
            
            //multiply the value of val by 2 on every iteration 
            val = val*2;
            
            if (bits[i] == 1){
                
                //if the value at that point in the array is a 1, then add 1 to the current value
                val = val + 1;
                
            }
            
        }

        return val;

    }
}
