import java.awt.*; 
import java.io.File; 
import squint.*;
import javax.swing.*;

public class ImageFilter
{

    //Declaring the integer arrays specific to this class
    private int[][] pixels;
    private int[][] secretImageArray;
    private int[][] coverImageArray;
    private int[][] scaledImageArray;
    
    //Declaring the images specific to this class
    private SImage modifiedImage;    
    private SImage addedImage;
    private SImage subtractImage; 
    private SImage subtractedImage;

    public ImageFilter(){

    }

    public static SImage clearLowerBit(SImage image){
        
        
        //Check to see if the image is a color image
        boolean color = isColor(image);
        
        //Extract the green and blue pixel arrays regardless of the image being colored or not
        int[][] greenPixel = image.getGreenPixelArray();        
        int[][] bluePixel = image.getBluePixelArray(); 

        
        
        if (color == false){
            //If the image is not a color image carry out the following set of commands
            //Extract the pixel array of the image and store it in the array that we will modify
            int[][] pixels = image.getPixelArray();
            
            //call the scaleImage method on the array of pixels extracted and return the resulting image
            SImage bitClearImage = scaleImage(image, 2, 2, pixels, color, greenPixel, bluePixel);
            
            //the lower bit of the image's pixel array should be cleared now
            return bitClearImage;
            
        }
        else {
            
            //If the image is a color image carry out the following set of commands    
            //Extract the red pixel array of the image and store it in the array to modify
            int[][] pixels = image.getRedPixelArray();
            
            //call the scaleImage method on the array of pixels extracted and return the resulting image
            SImage bitClearImage = scaleImage(image, 2, 2, pixels, color, greenPixel, bluePixel);
            
            //the lower bit of image's red pixel array should be cleared now
            return bitClearImage;
        }

    }

    public static SImage scaleImage(SImage image, int factor, int divider, int[][] pixels, boolean color, int[][] greenPixel, int[][] bluePixel) {
        
        
        for (int i = 0; i < image.getWidth(); i++ ) { 
            
            for (int j = 0; j < image.getHeight(); j++ ) { 
                
                //For each pixel of the image passed, multiply it by the factor and then divide by the divider
                pixels[i][j] = pixels[i][j]/divider;
                pixels[i][j] = pixels[i][j]*factor;

            }

        }

        if (color == false){
            
            //if the passed image is not a color image use the modified pixel to create a new greyscale
            //image and then return that image
            SImage modifiedImage = new SImage(pixels);
            return modifiedImage;
            
        }
        
        else{
            
            //if the passed image is a color image use the modified pixel to create a new greyscale
            //image and then return that image
            SImage modifiedImage = new SImage(pixels, greenPixel, bluePixel);
            return modifiedImage;
            
        }
    }

    private static boolean areEqual(int[][] a, int[][] b) {
        //When are two arrays equal?
        // a.length is first dimension
        // a[0].length is second dimension

        if ( (a.length != b.length) || (a[0].length != b[0].length)) {
            
            // Not same size, so return false
            return false;
            
        }

        // Now check all of the entries
        for (int i = 0; i < a.length; ++i) {
            
            for (int j = 0; j < a[0].length; ++j) {

                if (a[i][j] != b[i][j]) {
                    
                    return false;
                    
                }
                
            }
            
        }

        // If we get to here, they have the same size and
        // the same entries, so they are the same!
        return true;

    }

    public static boolean isColor(SImage image) {

        int[][] bluePixel = image.getBluePixelArray();
        int[][] greenPixel = image.getGreenPixelArray();        
        int[][] redPixel = image.getRedPixelArray();    

        // Are all of the arrays equal? If so, it's grayscale.
        // If not, it's color 

        if ( areEqual(bluePixel, greenPixel) && areEqual(redPixel, greenPixel)) {
            // All arrays equal, so grayscale
            return false;
        }

        // Arrays not all equal, so color
        return true;

    }

    public static SImage addImages(SImage coverImage, SImage secretImage){
        
        
        
        //Get the dimensions of both images passed to the method
        int coverWidth = coverImage.getWidth();
        int coverHeight = coverImage.getHeight();
        
        int secretWidth = secretImage.getWidth();
        int secretHeight = secretImage.getHeight();
        
        //Store the lower width of the two images and lower height of the two images
        //These are the dimensions we will use to in our loop to get our encrypted image
        int width = Math.min(coverWidth, secretWidth);
        int height = Math.min(coverHeight, secretHeight);
        
        
        //Get the pixel array of the secret image
        int[][] secretImageArray = secretImage.getPixelArray();
        
        //Now declare a new int[][] with the minimum width and height parameters
        int[][] scaledImageArray = new int[width][height];

        //Check to see if the coverImage is colored
        boolean color = isColor(coverImage);
        
        
        //Get the green and blue pixels of the cover image regardless of the cover images color
        int[][] greenPixel = coverImage.getGreenPixelArray();        
        int[][] bluePixel = coverImage.getBluePixelArray(); 

        if (color == false){
            
            //If the image is not a color image execute the following set of commands
            
            //clear the lower bit of the color image
            SImage bitClearImage = clearLowerBit(coverImage);
            
            //create a new array from the image of which we just cleared the lower bit
            int[][] coverImageArray = bitClearImage.getPixelArray();

            for (int i = 0; i < width; i++){

                for (int j = 0; j < height; j++){

                    //for every pixel of the cover image, add to it the pixel value of the secret image 
                    //divided by 128 and then store the new value at that point in a new array
                    
                    //we divide by 128 because the maximum value of an encoded pixel value of the image
                    //is going to be 128 and this way we just add the value 1 as the least significant
                    //bit to the end of the binary value of the pixel
                    scaledImageArray[i][j] = (secretImageArray[i][j]/128) + (coverImageArray[i][j]);

                }
            }
            
            
            //use the new array above that adds the pixels to create a new image and return it
            SImage addedImage = new SImage(scaledImageArray);
            return addedImage;

        }
        else {
            
            //If the image is a color image execute the following set of commands
            
            //clear the lower bit of the color image
            SImage bitClearImage = clearLowerBit(coverImage);
            
            //create a new array by extracting the red pixel array of the image of which we just 
            //cleared the lower bit of
            int[][] coverImageArray = bitClearImage.getRedPixelArray();
            
            //declare two new arrays for the green and blue pixels that will store the color pixels
            //up to the minimum width and height that we are using
            int[][] newGreenArray = new int[width][height];
            int[][] newBlueArray = new int[width][height];

            for (int i = 0; i < width; i++){

                for (int j = 0; j < height; j++){
                    
                    //for every red-pixel of the cover image, add to it the pixel value of the secret-image 
                    //divided by 128 and then store the new value at that point in a new array
                    
                    //we divide by 128 because the maximum value of an encoded pixel value of the image
                    //is going to be 128 and this way we just add the value 1 as the least significant
                    scaledImageArray[i][j] = (secretImageArray[i][j]/128) + (coverImageArray[i][j]);
                    
                    //for the green and blue pixel arrays we do not change them but we make new arrays to
                    //store the value up till the loop runs. this will crop one of the images if both
                    //images aren't of the same size
                    newGreenArray[i][j] = greenPixel[i][j];
                    newBlueArray[i][j] = bluePixel[i][j];

                }
            }
            
            
            //use the new array and the modifed green and blue pixel arrays to create a new image and return it
            SImage addedImage = new SImage(scaledImageArray, newGreenArray, newBlueArray);
            return addedImage;
        }
    }
    
    
    public static SImage subtractImages(SImage encryptedImage){
        
        //get the width and height of the encrypted image
        int width = encryptedImage.getWidth();
        int height = encryptedImage.getHeight();

        //declaring a new int[][] the size of the width and height of the encrypted image
        int[][] subtractedImageArray = new int[width][height];

        //clear the lower bit of the encrypted image
        SImage bitClearImage = clearLowerBit(encryptedImage);
        
        //check to see if the image is a color image
        boolean color = isColor(encryptedImage);

        int[][] greenPixel = encryptedImage.getGreenPixelArray();        
        int[][] bluePixel = encryptedImage.getBluePixelArray(); 

        if (color == false){
            
            //if the image is not a color image get the pixel array of the encrypted image
            int[][] encryptedImageArray = encryptedImage.getPixelArray();
            
            
            //get the pixel array of the image we cleared the lower bit of
            int[][] bitClearImageArray = bitClearImage.getPixelArray();

            for (int i = 0; i < encryptedImage.getWidth(); i++){

                for (int j = 0; j < encryptedImage.getHeight(); j++){

                    //subtract the pixel values at every point of the two arrays from above.
                    //this will give you an array 
                    subtractedImageArray[i][j] = (encryptedImageArray[i][j]) - (bitClearImageArray[i][j]);
                    
                    //since we divided by 128 when adding the pixels we multiply by 128 now to get
                    //those encoded pixels back. In the case of an encrypted image we'll get a grayscale
                    //and less bright version of the original image we encrypted
                    subtractedImageArray[i][j] = subtractedImageArray[i][j]*128;

                }
            }

            //use the subtractedImageArray to create a new image and return it
            SImage subtractedImage = new SImage(subtractedImageArray);
            return subtractedImage;

        }
        else {
            
            //if the image is a color image get the red pixel array of the encrypted image
            int[][] encryptedImageArray = encryptedImage.getRedPixelArray();
            
            //get the red pixel array of the image we cleared the lower bit of the red pixel array of
            int[][] bitClearImageArray = bitClearImage.getRedPixelArray();

            for (int i = 0; i < encryptedImage.getWidth(); i++){

                for (int j = 0; j < encryptedImage.getHeight(); j++){

                    //subtract the pixel values at every point of the two arrays from above.
                    //this will give you an array 
                    subtractedImageArray[i][j] = (encryptedImageArray[i][j]) - (bitClearImageArray[i][j]);
                    
                    //since we divided by 128 when adding the pixels we multiply by 128 now to get
                    //those encoded pixels back. In the case of an encrypted image we'll get a grayscale
                    //and less bright version of the original image we encrypted
                    subtractedImageArray[i][j] = subtractedImageArray[i][j]*128;

                }
            }

            //use the subtractedImageArray to create a new image and return it
            SImage addedImage = new SImage(subtractedImageArray);
            return addedImage;
            
        }

    }
}
