/*==============  checkFileTypes =============*
 On setup();
 Store all files from the media folder in 
a global array by type, to access later.
*=========================================*/
void checkFileTypes() {
  String path = sketchPath + "/data"; // path to media folder
  // store the files to loop in a local array
  String[] filesToLoop = listFileNames(path);
 
  // loop through all files and list only the name
  for(int i =0; i<filesToLoop.length; i++) {
      
    // store the filename in a local var
    String mediaFileName = filesToLoop[i];
    // add all media to global var
    allFiles.add(mediaFileName);

    // check for acceptable image formats
    if( (mediaFileName.endsWith("png")) || (mediaFileName.endsWith("jpg")) || (mediaFileName.endsWith("jpeg")) || mediaFileName.endsWith("gif") ){ 
      imageFiles.add(mediaFileName);
    }
    // check for accetable audio formats
    else if( (mediaFileName.endsWith("aif")) || (mediaFileName.endsWith("mp3")) ){  
      audioFiles.add(mediaFileName);
    }
    // check for acceptable movie formats
    else if( (mediaFileName.endsWith("mov")) || (mediaFileName.endsWith("mp4")) ){  
      movieFiles.add(mediaFileName);
    }
    // finally check for all other formats
    else {  
      otherFiles.add(mediaFileName);
    }

  }
  
}


/*============== listFileNames =============*
 Returns all the files in a directory as 
 an array of Strings
*=========================================*/
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
