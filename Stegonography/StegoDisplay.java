import java.awt.*; 
import java.io.File; 
import squint.*;
import javax.swing.*;

public class StegoDisplay extends GUIManager
{

    //declaring ImageViewer objects that we're going to use
    private ImageViewer leftImageViewer;
    private ImageViewer rightImageViewer;
    private ImageViewer midImageViewer;

    //declaring all the buttons
    private JButton encode;
    private JButton decode;
    private JButton encrypt;
    private JButton decrypt;
    private JButton clearText;
    private JButton clearImage;

    //declaring the text area where the text is going to appear
    private JTextArea messageArea;

    //declaring the int[][] needed
    private int[][] image;
    private int[][] extractImage;

    //declring the int[] needed
    private int[] imageBits;
    private int[] images;

    //declaring all the instance images
    private SImage newImage;
    private SImage modifiedImage;
    private SImage secretImage;

    private JLabel newImageLabel;

    //constant values for the dimensions of the GUI
    private final int width = 1100;
    private final int height = 500;
    private final int textAreaWidth = 300;
    private final int textAreaHeight = 300;


    public static void main(String[][] args)
    {


    	new StegoDisplay();


    }
    public StegoDisplay(){
        //create a window of the fixed dimensions
        this.createWindow(width, height, "StegoDisplay");
        contentPane.setLayout(new BorderLayout());

        //add a main JPanel
        JPanel mainPanel = new JPanel();

        //calling the ImageViewer class thrice
        leftImageViewer = new ImageViewer(this);
        rightImageViewer = new ImageViewer(this);
        midImageViewer = new ImageViewer(this);

        //initialize the text area with the given dimensions.
        messageArea = new JTextArea(textAreaWidth,textAreaHeight);

        //Add a split pane to divide the the middle area into two areas: one for imageviewer and the other for the text area
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setTopComponent(new JScrollPane(messageArea));
        split.setBottomComponent(midImageViewer);
        split.setDividerLocation(200);

        //frid layout divides the area into 3
        GridLayout StegoDisplay = new GridLayout(1,3);
        mainPanel.setLayout(StegoDisplay);
        mainPanel.add(leftImageViewer);

        mainPanel.add(split);
        mainPanel.add(rightImageViewer);

        JPanel buttonPanel = new JPanel();

        //initialize the buttons and then add the buttons to the button panel
        encode = new JButton("Encode");
        decode = new JButton("Decode");
        encrypt = new JButton("Encrypt");
        decrypt = new JButton("Decrypt");
        clearText = new JButton("Clear Text");
        clearImage = new JButton("Clear Image");
        newImageLabel = new JLabel("");

        buttonPanel.add(encode);
        buttonPanel.add(decode);
        buttonPanel.add(encrypt);
        buttonPanel.add(decrypt);
        buttonPanel.add(clearText);
        buttonPanel.add(clearImage);
        buttonPanel.add(newImageLabel);

        // add the main and button panels to the contenpane.
        contentPane.add(mainPanel);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

    }

    public void buttonClicked(JButton whichButton) {

        String actionCommand = whichButton.getActionCommand();

        if (actionCommand.equals(clearText.getActionCommand())) {

            //clear the text when the button is pressed
            messageArea.setText(null);

        }

        else if (actionCommand.equals(encode.getActionCommand())) {

            if(leftImageViewer.image != null){
                //convert the text to its ASCII value in bits and then use the bits to convert that text
                //into an image

                modifiedImage = convert2Image(StringConverter.convert2Bits(messageArea.getText()), leftImageViewer.image.getWidth(), leftImageViewer.image.getHeight());

                //set the icon of the mid image viewer to the image we just created
                midImageViewer.imageLabel.setIcon(modifiedImage);
                midImageViewer.image = modifiedImage;
            }

        }

        else if (actionCommand.equals(decode.getActionCommand())) {

            if(midImageViewer.image != null){
                //use the image in the mid image viewer ie the decrypted/encoded image to convert the image
                //into bits and then convert those bits into text and add the text to the text area
                int[] images = convert2Bits(midImageViewer.image);
                messageArea.setText(StringConverter.convert2String(images));
            }

        }

        else if (actionCommand.equals(clearImage.getActionCommand())) {

            //clear the image when this button is pressed.
            SImage clearImage = new SImage(100,100,255);
            midImageViewer.image = clearImage;
            midImageViewer.imageLabel.setIcon(clearImage);

        }

        else if (actionCommand.equals(encrypt.getActionCommand())) {

            if(leftImageViewer.image != null && midImageViewer.image != null){
                //encrypt the two images by adding them together
                rightImageViewer.image = ImageFilter.addImages(leftImageViewer.image, midImageViewer.image);
                rightImageViewer.imageLabel.setIcon(ImageFilter.addImages(leftImageViewer.image, midImageViewer.image));
            }

        }

        else if (actionCommand.equals(decrypt.getActionCommand())) {

            if(rightImageViewer.image != null){
                //decrypt the image by subtracting the encrypted image from the cover image and then 
                //displaying the image in the mid image viewer
                midImageViewer.image = ImageFilter.subtractImages(rightImageViewer.image);
                midImageViewer.imageLabel.setIcon(ImageFilter.subtractImages(rightImageViewer.image));
            }

        }
    }

    SImage convert2Image(int[] bits, int width, int height){

        int count = 0;

        //declaring a new array of size of the dimensions of the cover image
        int[][] image = new int[width][height];

        for (int i = 0; i < width; i++){

            for (int j = 0; j < height; j++){

                if (count < bits.length){

                    //for every 1 in the bits of the array of the text set the brightness value at that
                    //point to 128
                    image[i][j] = bits[count]*128;
                    count = count + 1;

                }
                else {

                    //if the count exceeds the lenght of the array set all the brightness values to 0. 
                    //this is why most of the picture is black if there is very little text
                    image[i][j] = 0;

                } 

            }
        }

        //create an image with the pixel array from above
        SImage newImage = new SImage(image);
        return newImage;

    }

    int[] convert2Bits(SImage image){

        int count = 0;

        //declare a new int[] of size of the dimensions of the passed image 
        imageBits = new int[image.getWidth()*image.getHeight()];

        //check to see if the image is a color image
        boolean color = ImageFilter.isColor(image);

        if( color == false){

            //if the image is not a color image extract the pixel array
            int[][] extractImage = image.getPixelArray();

            for (int i = 0; i < image.getWidth(); i++){

                for (int j = 0; j < image.getHeight(); j++){

                    imageBits[count] = extractImage[i][j]/128;
                    count = count + 1;

                }
            }

            return imageBits;
            
        }

        else{
            //if the image is a color image, extract the red pixel array
            int[][] extractImage = image.getRedPixelArray();

            for (int i = 0; i < image.getWidth(); i++){

                for (int j = 0; j < image.getHeight(); j++){

                    //extract the bits by dividing the pixel values by 128 and storing them in an array
                    imageBits[count] = extractImage[i][j]/128;
                    count = count + 1;

                }
            }

            //return the array of bits
            return imageBits;

        }
    }
}
