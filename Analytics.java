package ohar8139;

import java.io.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/**
 * Analytics class to parse the Generation files and report the min, mean, and max values of each generation for graphing
 * @author Wesley Howell
 *
 */
public class Analytics {

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		checkXMLFiles();
//	}
	
	/**
	 * Check the XML files
	 * Reads the XML files in the XML directory, Loads the generation, parses the info and reports the mean, min, and max
	 */
	private static void checkXMLFiles(){
		File dir = new File("C:\\Users\\Wesley Howell\\spacewar\\src\\XMLFiles\\");
		File[] listOfXMLs = dir.listFiles();

		//Check each file in the directory and import each xml file.
		for(File file : listOfXMLs){
			if(file.isFile() && !file.isHidden()){
				double min,max,sum,mean;
				int size = 0;
				
				//Set the values to default values.
				min = 1000000000.0;
				max = 0.0;
				sum = 0.0;
				mean = 0.0;
				
				//Load the generation from XML data
				Generation generation = null;
				//Read XML Data
				XStream xstream = new XStream();
				xstream.alias("Generation", Generation.class);

				try { 
					generation = (Generation) xstream.fromXML(file);
					System.out.println("Generation size" + generation.getGeneration().size());
				} catch (XStreamException e) {
					// if you get an error, handle it other than a null pointer because
					// the error will happen the first time you run
				}
				
				
				//Run Through the loaded generation
				if (generation != null){
					//Pull each chromosome
					for(Chromosome c : generation.getGeneration()){
						//Add to sum
						sum += c.getMoneyCollected();
						
						//Check Max
						if(c.getMoneyCollected() > max) max = c.getMoneyCollected();
						
						//Check Min
						if(c.getMoneyCollected() < min) min = c.getMoneyCollected();
						
						//Set Size
						size = generation.getGeneration().size();
					}
				}
				
				//Calculate average
				mean = sum/size;
				
				//Print the results
				System.out.println("Generation - " + file.getName());
				System.out.println("Average - " + mean);
				System.out.println("Min     - " + min);
				System.out.println("Max     - " + max);
				System.out.println(" ");
				
			}
		}
	}

}
