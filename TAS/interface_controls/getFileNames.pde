/**   This code was written by Daniel Shiffman and can be found at:
 **   http://processing.org/learning/topics/directorylist.html
 **   To test if the functions are working properly, paste into setup()
      void setup() {
        String path = sketchPath + "/media";
        
        println("Listing all filenames in a directory: ");
        String[] filenames = listFileNames(path);
        println(filenames);
        
        println("\nListing info about all files in a directory: ");
        File[] files = listFiles(path);
        for (int i = 0; i < files.length; i++) {
          File f = files[i];    
          println("Name: " + f.getName());
          println("Is directory: " + f.isDirectory());
          println("Size: " + f.length());
          String lastModified = new Date(f.lastModified()).toString();
          println("Last Modified: " + lastModified);
          println("-----------------------");
        }
        
        println("\nListing info about all files in a directory and all subdirectories: ");
        ArrayList allFiles = listFilesRecursive(path);
        
        for (int i = 0; i < allFiles.size(); i++) {
          File f = (File) allFiles.get(i);    
          println("Name: " + f.getName());
          println("Full path: " + f.getAbsolutePath());
          println("Is directory: " + f.isDirectory());
          println("Size: " + f.length());
          String lastModified = new Date(f.lastModified()).toString();
          println("Last Modified: " + lastModified);
          println("-----------------------");
        }
        noLoop();
      }
 **  END TEST FOR getFileNames   ***/

// This function returns all the files in a directory as an array of Strings  
String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else {
    // If it's not a directory
    return null;
  }
}

// This function returns all the files in a directory as an array of File objects
// This is useful if you want more info about the file
File[] listFiles(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    File[] files = file.listFiles();
    return files;
  } else {
    // If it's not a directory
    return null;
  }
}

// Function to get a list ofall files in a directory and all subdirectories
ArrayList listFilesRecursive(String dir) {
   ArrayList fileList = new ArrayList(); 
   recurseDir(fileList,dir);
   return fileList;
}

// Recursive function to traverse subdirectories
void recurseDir(ArrayList a, String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    // If you want to include directories in the list
    a.add(file);  
    File[] subfiles = file.listFiles();
    for (int i = 0; i < subfiles.length; i++) {
      // Call this function on all files in this directory
      recurseDir(a,subfiles[i].getAbsolutePath());
    }
  } else {
    a.add(file);
  }
}
