iterate.result <- function(s=NULL,  # If calling this function a second time, this should be
                        # resulting object of the first
                        testDir=NULL,  # Name of the directory with the preprocessed test files
                        testNamesMap=NULL,  # testing map
                        truthName=NULL, # ground truth (fault matrix)
                        kDir="",
                        iter=200,   # LDA parameter number of iterations
                        alpha=0.1,  # LDA parameter alpha
                        beta=0.1,  #LDA parameter beta                        
                        distance,  # distance measure to use (passed to dist())
                        maximization, # max. algorithm to use (greedy or clustering)
                        rerunLDA=FALSE, # Should we rerun LDA, if it's already been run?
                        verbose=FALSE,
                        iT=30,
                        method="cluster_string")
{
  
  names <- c("13","30","35","40","50","60","80","70","90","10","11","12","36");
  #names <- c("80");
  apfds <-matrix(nrow=length(names),ncol=iT);
  for(m in 1:length(names)){
    top_dir = "../../data/"
    testDir2=paste(top_dir,"lda_input_steps_to_perform/litmus_",names[m],"/",sep="");
    clusterDir2=paste(top_dir,"cluster_reverse/litmus_",names[m],"/",sep="");
    
    #testDir2=paste(testDir2,"/",sep="");
    truthName2=paste(top_dir,"fault_matrix/steps_to_perform/litmus_",names[m],"/fault_matrix.txt",sep="");
    
    #truthName2=(truthName2,"/fault_matrix.txt");
    dirname = paste(top_dir,"result_reverse/",kDir,sep="");
    if(!file.exists(dirname)){
      dir.create(dirname);
    }
    filename = paste(top_dir,"result_reverse/",kDir,"litmus_",names[m],"_",method,".txt",sep="");
    sink(filename);
    
    for(n in 1:iT)
    {
      
     # print(clusterDir2)
      if(method=="string")
      {
        tcp <- tcp.string(testDir=testDir2,truthName=truthName2);
      }else if(method=="random")
      {
        tcp <- tcp.random(testDir=testDir2,truthName=truthName2);
      }else if(method=="cluster_random")
      {
        tcp <- random.cluster(testDir=testDir2,truthName=truthName2, testDirCluster = clusterDir2);
      }else if(method=="cluster_string")
      {
        tcp <- string.cluster(testDir=testDir2,truthName=truthName2, testDirCluster = clusterDir2);
      }else if(method=="lda_coverage"){
	tcp <-tcp.lda(testDir=testDir2,truthName=truthName2,maximization="max",K=15);
      }else if(method=="lda_greedy"){
      	tcp <-tcp.lda(testDir=testDir2,truthName=truthName2,maximization="greedy",K=5);
      }
      #topic <- tcp.lda(testDir,K,truthName);
      apfds[m,n] <- tcp$apfd;
     # print(tcp$apfd)
      
      
    }
    
    apfds[m,]=sort(apfds[m,])
    
    print( apfds[m,]);
    
    print('medium:');
    
    medium_point=(iT+1)/2;
    if(is.integer(medium_point))
    {
      medium=apfds[m,medium_point];
    }else{
      medium=(apfds[m,(iT+2)/2]+apfds[m,iT/2])/2;
    }
    
    print(medium);
    sink();
  }
  apfds
}


iterate.result.all <- function(itNum=100)
{
  
  
 #methods=c("string","random","lda_coverage","lda_greedy","cluster_string","cluster_random");
  methods=c("cluster_string","cluster_random");
  #methods=c("lda_greedy")
  #methods=c("cluster_random");
  for(i in 1:length(methods)){
    
    dirName = paste(methods[i],"/",sep="")
    iterate.result(iT=itNum,method=methods[i],kDir=dirName);
  }
}



