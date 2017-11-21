package acial.selenium.utils;

import java.io.File;
import java.io.IOException;

public final class Misc {

	public static void DeleteFiles (String dir, String startsWith, String ext) {
		File file = new File(dir);
	    File[] files = file.listFiles(); 
	    for (File f:files) 
	    {if (f.isFile() && f.exists()) 
	     {
	    	if ((f.getName().startsWith(startsWith)) &&(f.getName().endsWith(ext))) 
	      		f.delete();
	     }
	    }
      }
	public static void Pause () {
		System.out.println("Press Enter to continue...");
	    try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("Finished.");
	}
}
