import java.awt.*; 
import java.io.File; 
import squint.*;
import javax.swing.*;

import java.util.Scanner;
import java.util.*;
import java.awt.event.*;



import java.io.*;


public class ImageViewer extends GUIManager {
    //The label in which the image will be displayed
    public JLabel imageLabel;

    //All the buttons that are required to do the tasks

    private JButton loadButton;
    private JButton saveButton;

    //Declaring the image that will be edited
    private SImage theImage;

    //Declaring this to choose which image to open
    private JFileChooser chooser =  
        new JFileChooser( new File( System.getProperty("user.dir")) + "/AllImages" );
        
    public SImage image;

    public ImageViewer(StegoDisplay stegoDisplay) {



        contentPane.setLayout(new BorderLayout());
        //Add a Label that says Hi and center the text
        imageLabel = new JLabel("", SwingConstants.CENTER);

        //Add the load image button and set its action command
        loadButton = new JButton("Load Image");
        loadButton.setActionCommand("load");


        //Add the requantize button and set its action command
        saveButton = new JButton("Save Image");
        saveButton.setActionCommand("save");

        //Set the JPanels and contentPane so that the GUI looks like the desired product
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        contentPane.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

    }

    public void buttonClicked(JButton whichButton) {

        //Get the action command of the button pressed and store it in a string
        String actionCommand = whichButton.getActionCommand();

        if (actionCommand.equals(loadButton.getActionCommand())) {

            //If the load button was pressed do the following

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                String filename = chooser.getSelectedFile().getAbsolutePath();

                if ( filename.toLowerCase().endsWith( ".pgm" ) ) {

                    /*
                    SImage cannot handle PGM files by default. The following code will read the 
                    image and store the contents in an array and then create a new image using
                    that array
                     */

                    image = readPGMImage( filename );

                } else {

                    // Java can handle this type of image 

                    image = new SImage( filename );

                }

                //Display the selected image in the ImageLabel 
                imageLabel.setIcon(image);
            }
        }

        else if(actionCommand.equals(saveButton.getActionCommand())){
            if ( chooser.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION ) {
                boolean overWriteFile = false;
                if (chooser.getSelectedFile().exists() ) {

                    /*
                    When you try to save the modified image, check to see if the file with the 
                    name already exists. If it does, print out a warning message asking if the
                    the user wants toreplace the file
                     */

                    int returnValue = JOptionPane.showOptionDialog( this,
                            "File already exists. Replace?", "Warning: File Exists",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, null, null );

                    if ( returnValue == JOptionPane.YES_OPTION ) {

                        //If the user selects yes, replace the old file

                        overWriteFile = true;

                    }

                } else {

                    overWriteFile = true;

                }
                if ( overWriteFile == true ) {

                    String fileName = chooser.getSelectedFile().getName();
                    String pathname = chooser.getSelectedFile().getAbsolutePath();

                    if ( fileName.toLowerCase().endsWith( ".pgm" ) ) {                            
                        writePGMImage( pathname );
                    } else if ( fileName.toLowerCase().endsWith( ".png" ) ) {
                        image.saveAs( pathname );

                    }
                }

            }
        }
        
        

    }
    
    private void writePGMImage( String filename ) {
        try {
            
            BufferedWriter outStream = new BufferedWriter( new FileWriter( filename ) );           
            // OutStream is connected to the file file for writing
            // It will overwrite files with the same name in the diretory

            

            // First line of a pgm file is what?  It's just a line with P2
            outStream.write("P2\n");
            
            // Get the dimensions of the image
            int numCols = image.getWidth();
            int numRows = image.getHeight();

            outStream.write(numCols + " " + numRows + "\n");
            // Now write the 255 (max intensity value)
            outStream.write("255\n");
            // At this point, we need to start writing the pixels

            // Get the pixels that we need to write
            int[][] pixel = image.getPixelArray();

            // Loop through pixels writing as we go
            int pixelsWritten = 0;

            for (int row = 0; row < numRows; ++row) {
                
                for (int col = 0; col < numCols; ++col) {
                    
                    outStream.write(pixel[col][row] + " ");
                    ++pixelsWritten;
                    
                    if (pixelsWritten % 15 == 0) {
                        outStream.write("\n");
                    }                  
                    
                }
                
            }

            // We should be done!  Except for closing the stream!
            outStream.close();

        } catch (Exception e) {
            System.out.println( e );
        }
    }
    
    private SImage readPGMImage( String filename ) {

        try {
            // put my file reading code in here.           
            Scanner inputStream = new Scanner( new File(filename));
            // At this point, we are connected to our file for READING!            
            // Read the P2 line and ignore it
            String devNull = inputStream.nextLine();            
            // Get image width and height           
            int numCols = inputStream.nextInt();           
            int numRows = inputStream.nextInt();                      
            // Get the next line (max intensity) and toss it           
            int garbage = inputStream.nextInt();           
            int[][] pixel = new int[numCols][numRows];

            // Get the pixel values
            for (int row = 0; row < numRows; ++row) {
                
                for (int col = 0; col < numCols; ++col) {
                    
                    pixel[col][row] = inputStream.nextInt();
                    
                }
                
            }

            // close the stream!
            inputStream.close();
            return new SImage(pixel);

        } catch (Exception e) {
            
            System.out.print("Error: Caught and Exception!\n" + e + "\n");
            System.exit(1);       
            
        }

        return null;

    }
    
    public SImage getImage(){
        
        return image;
        
        
    }
}
