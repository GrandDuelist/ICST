source("./tcp.kmeans-internal.R")
tcp.kmeans <- function(s=NULL,  
  testDir,  
  testNamesMap=NULL, 
  truthName=NULL,
  verbose=F){
  if (is.null(s)){
    s <- new.env()
  }
  
  if (is.null(s$files)){
    if (verbose==T){
      cat(sprintf("Reading test directory...\n"))
    }
    .loadAndCleanData(s, testDir, testNamesMap, truthName, verbose)
  }
  
  .preComputeKmeans(s);


  if (!is.null(s$truth_inverse)){
      if (verbose==TRUE){
          cat(sprintf("Evaluating results using ground truth...\n"))
      }
      .evaluate(s)
  }

  if (verbose==T){
      cat(sprintf("Done.\n"))
  }
    
  s
}