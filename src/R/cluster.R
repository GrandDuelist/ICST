source("tcp.string.R")

.read.file.class <- function(){
  inputFilePath <- "/home/ogre/Desktop/cluster/litmus_10/";
  subDirs = list.files(inputFilePath);
  subDirs
}

random.cluster <- function(s = NULL,
                            testDir,
                            testDirCluster,
                            testNamesMap=NULL, 
                            truthName=NULL,
                            verbose=F){
  
  if (is.null(s)){
    s <- new.env()
  }
    
  if (is.null(s$files)){
    if (verbose==TRUE){
      cat(sprintf("Reading test directory...\n"))
    }
    .loadAndCleanData(s, testDir, testNamesMap, truthName, verbose)
  }
  
  
  
 
  subDirs = list.files(testDirCluster);
  subDirs = as.numeric(subDirs)
  subDirs = sort(subDirs)
  wholeNames <- names(s$files)
  #print(subDirs)
  howManyTest = 0
  whichToTry <- vector();
  for(i in 1:length(subDirs)){
    #print(subDirs[i])
    temp = NULL
    testDirWhole = paste(testDirCluster,subDirs[i],"/",sep="")
    #print(testDirWhole)
    temp$files     <- Corpus(DirSource(testDirWhole))
    temp$howManyTests <- length(temp$files)
    temp$whichToTry  <- sample(1:temp$howManyTests)
  #  print(names(temp$files)[temp$whichToTry])
    tempName <- names(temp$files)[temp$whichToTry]
    #print(length(tempName))
    for(j in 1:length(tempName)){
      howManyTest= howManyTest +1
      index <- which(wholeNames == tempName[j])
      whichToTry[howManyTest] = index[1]
      
    }
  
   
  
  }
  s$whichToTry <- whichToTry
  #print(whichToTry)
 
 # index <- which(names == "show_test.cgi@id=42081.txt")
 
  
  if (!is.null(s$truth_inverse)){
    if (verbose==TRUE){
      cat(sprintf("Evaluating results using ground truth...\n"))
    }
    .evaluate(s)
  }
  
  if (verbose==TRUE){
    cat(sprintf("Done.\n"))
  }
 
  s
}

.main <- function(){
  inputFilePath <- "/home/ogre/Desktop/cluster/litmus_10/";
  directories <- .read.file.class()
  s <- .random.cluster(testDir= inputFilePath,subDirs = directories)
}



string.cluster <- function(s = NULL,
                            testDir,
                            testDirCluster,
                            testNamesMap=NULL, 
                            truthName=NULL,
                
                            verbose=F){
  
  if (is.null(s)){
    s <- new.env()
  }
  
  if (is.null(s$files)){
    if (verbose==TRUE){
      cat(sprintf("Reading test directory...\n"))
    }
    .loadAndCleanData(s, testDir, testNamesMap, truthName, verbose)
  }
  
  
  
  
  subDirs = list.files(testDirCluster);
  subDirs = as.numeric(subDirs)
  subDirs = sort(subDirs)
  wholeNames <- names(s$files)
  #print(subDirs)
  howManyTest = 0
  whichToTry <- vector();

  for(i in 1:length(subDirs)){
    
    #print(subDirs[i])
    temp = NULL
    testDirWhole = paste(testDirCluster,subDirs[i],"/",sep="")
     tempfiles    <- Corpus(DirSource(testDirWhole))
    if(length(tempfiles)>1){
    
      temp <- tcp.string(testDir = testDirWhole)
     #print(testDirWhole)
     #  print(names(temp$files)[temp$whichToTry])
     tempName <- names(temp$files)[temp$whichToTry]
      #print(length(tempName))
    for(j in 1:length(tempName)){
      howManyTest= howManyTest +1
      index <- which(wholeNames == tempName[j])
      whichToTry[howManyTest] = index[1]
      
    }
    }else
    {
      tempName <- names(tempfiles);
      
      
      for(j in 1:length(tempName)){
        howManyTest= howManyTest +1
        index <- which(wholeNames == tempName[j])
        whichToTry[howManyTest] = index[1]
        
      }
    }
    
    
  }
  
  s$whichToTry <- whichToTry
  #print(whichToTry)
  
  # index <- which(names == "show_test.cgi@id=42081.txt")
  
  
  if (!is.null(s$truth_inverse)){
    if (verbose==TRUE){
      cat(sprintf("Evaluating results using ground truth...\n"))
    }
    .evaluate(s)
  }
  
  if (verbose==TRUE){
    cat(sprintf("Done.\n"))
  }
 # print(s$whichToTry)
  s
}

.main <- function(){
  inputFilePath <- "/home/ogre/Desktop/cluster/litmus_10/";
  directories <- .read.file.class()
  s <- .random.cluster(testDir= inputFilePath,subDirs = directories)
}


