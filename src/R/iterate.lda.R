source("tcp.lda.R");
source("tcp.string.R");
source("tcp.lda-internal.R");
source("tcp.random.R");
source("tcp.static.R");

iterate.lda.all <- function(kNum=50,itNum=10)
{
  
  
  methods=c("lda","string","random");
  for(i in 1:length(methods)){
    
    iterate.lda(K=kNum,iT=itNum,method=methods[i]);
  }
}

iterate.lda.dif_k <- function (itNum=10,others=TRUE, distance = "manhattan",maximization="max"){
  ks=c(5,10,25,50);
  for(i in 1:length(ks)){
    kDir = paste("K=",ks[i],"/",sep="");
    
    iterate.lda(K=ks[i],iT=itNum,kDir=kDir,method="lda",distance = distance,maximization=maximization);
  }
  
  if(others == TRUE){
    iterate.lda(iT=itNum,kDir="string/",method="string");
    iterate.lda(iT=itNum,kDir="random/",method="random");
  }
  
}


iterate.lda <- function(s=NULL,  # If calling this function a second time, this should be
                        # resulting object of the first
                        testDir=NULL,  # Name of the directory with the preprocessed test files
                        testNamesMap=NULL,  # testing map
                        truthName=NULL, # ground truth (fault matrix)
                        kDir="",
                        K,   # LDA parameter number of topics
                        iter=200,   # LDA parameter number of iterations
                        alpha=0.1,  # LDA parameter alpha
                        beta=0.1,  #LDA parameter beta                        
                        distance,  # distance measure to use (passed to dist())
                        maximization, # max. algorithm to use (greedy or clustering)
                        rerunLDA=FALSE, # Should we rerun LDA, if it's already been run?
                        verbose=FALSE,
                        iT=10,
                        method="lda")
{
  
  #names <- c("80","70","35","90","10","11","12","13");
  names <- c("12");
  apfds <-matrix(nrow=length(names),ncol=iT);
  for(m in 1:length(names)){
    testDir2=paste("../../web_lscp/data/lda_input_steps_to_perform/litmus_",names[m],"/",sep="");
    
    #testDir2=paste(testDir2,"/",sep="");
    truthName2=paste("../../web_lscp/data/fault_matrix/steps_to_perform/litmus_",names[m],"/fault_matrix.txt",sep="");
    #truthName2=(truthName2,"/fault_matrix.txt");
    dirname = paste("result/",kDir,sep="");
    if(!file.exists(dirname)){
      dir.create(dirname);
    }
    filename = paste("result/",kDir,"litmus_",names[m],"_",method,".txt",sep="");
    sink(filename);
    
    for(n in 1:iT)
    {
      if(method=="lda"){
        tcp <- tcp.lda(s,testDir=testDir2,testNamesMap,truthName=truthName2,K=K,iter,alpha,beta,distance = distance,maximization=maximization,rerunLDA,verbose);
      }else if(method=="string")
      {
        tcp <- tcp.string(testDir=testDir2,truthName=truthName2);
      }else if(method=="random")
      {
        tcp <- tcp.random(testDir=testDir2,truthName=truthName2);
      }else if(method=="static")
      {
        tcp <- tcp.static(testDir=testDir2,truthName=truthName2);
      }
      #topic <- tcp.lda(testDir,K,truthName);
      apfds[m,n] <- tcp$apfd;
      
      
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


# get the distance of 8 version

iterate.dis <- function(s=NULL,  # If calling this function a second time, this should be
                        # resulting object of the first
                        testDir=NULL,  # Name of the directory with the preprocessed test files
                        testNamesMap=NULL,  # testing map
                        truthName=NULL, # ground truth (fault matrix)
                        K,   # LDA parameter number of topics
                        iter=200,   # LDA parameter number of iterations
                        alpha=0.1,  # LDA parameter alpha
                        beta=0.1,  #LDA parameter beta                        
                        distance="manhattan",  # distance measure to use (passed to dist())
                        maximization="greedy", # max. algorithm to use (greedy or clustering)
                        rerunLDA=FALSE, # Should we rerun LDA, if it's already been run?
                        verbose=FALSE    
)
{
  
  names <- c("80","70","35","90","10","11","12","13");
  dists <- c("all","fail","fail_pass")
  
  for(n in 1:length(dists)){
    dirname = paste("result/dists/",dists[n],"(K=",K,")",sep="");
    if(!file.exists(dirname)){
      dir.create(dirname);
    }
  }
    
    for(m in 1:length(names)){
      testDir2=paste("../../web_lscp/data/lda_input_steps_to_perform/litmus_",names[m],"/",sep="");
      
      
      tcp <- tcp.lda(s,testDir=testDir2,truthName,testNamesMap,K,iter,alpha,beta,distance=distance,maximization,rerunLDA,verbose);
      
      dirname = paste("result/dists/",dists[1],"(K=",K,")",sep="");
      filename = paste(dirname,"/litmus_",names[m],".txt",sep="");
      sink(filename);
      print(tcp$otherdists$all.mean);
      sink();
     
      dirname = paste("result/dists/",dists[2],"(K=",K,")",sep="");
      filename = paste(dirname,"/litmus_",names[m],".txt",sep="");
      sink(filename);
      print(tcp$otherdists$fail.mean);
      sink();
      
      
      dirname = paste("result/dists/",dists[3],"(K=",K,")",sep="");
      filename = paste(dirname,"/litmus_",names[m],".txt",sep="");
      sink(filename);
      print(tcp$otherdists$failpass.mean)
      sink();
     
      
      
    } 
}

  iterate.dis.dif_k <- function (distance="manhattan"){
    ks=c(5,10,25,50);
    for(i in 1:length(ks)){
    
      
      iterate.dis(K=ks[i],distance=distance);
    }
    

    
    
  }

#get the topics of 8 version

iterate.topics <- function(s=NULL,  # If calling this function a second time, this should be
                        # resulting object of the first
                        testDir=NULL,  # Name of the directory with the preprocessed test files
                        testNamesMap=NULL,  # testing map
                        truthName=NULL, # ground truth (fault matrix)
                        K,   # LDA parameter number of topics
                        iter=200,   # LDA parameter number of iterations
                        alpha=0.1,  # LDA parameter alpha
                        beta=0.1,  #LDA parameter beta                        
                        distance="manhattan",  # distance measure to use (passed to dist())
                        maximization="greedy", # max. algorithm to use (greedy or clustering)
                        rerunLDA=FALSE, # Should we rerun LDA, if it's already been run?
                        verbose=FALSE    
)
{
  
  names <- c("80","70","35","90","10","11","12","13");
  
  
  
    dirname = paste("result/topics/","K=",K,sep="");
    if(!file.exists(dirname)){
      dir.create(dirname);
    }
  
  
  for(m in 1:length(names)){
    testDir2=paste("../../web_lscp/data/lda_input_steps_to_perform/litmus_",names[m],"/",sep="");
    
    
    tcp <- tcp.lda(s,testDir=testDir2,truthName,testNamesMap,K,iter,alpha,beta,distance,maximization,rerunLDA,verbose);
    
   
    filename = paste(dirname,"/litmus_",names[m],".txt",sep="");
    sink(filename);
    print(tcp$lda$topfive);
    sink();
    
    
  
    
  } 
}

iterate.topics.dif_k <-function(){
  ks=c(5,10,25,50);
  for(i in 1:length(ks)){
    
    
    iterate.topics(K=ks[i]);
  }
  
  
}


