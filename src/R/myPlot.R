require(gdata)

getresult <-function(){
versions <- c("litmus_30","litmus_35","litmus_36","litmus_40","litmus_50",
             "litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
             "litmus_11","litmus_12","litmus_13");
results = list();
for(i in 1:length(versions)){
  version = versions[i];
  result <- read.xls("../../tcp/data/result/result_100.xls",sheet=version);
 
  
  file <- paste("plots/individual/R_S_L/", version,".jpg",sep="")
  jpeg(file);
  
  title = paste("APFD of ", version,sep="");
  boxFig <- boxplot(result$random,result$string,result$lda,
                    names = c("random","string","lda"), main = title); 
  dev.off()
  results[i] <- boxFig;
}
results;
}


getClusterPlotTrad <-function(){
  versions <- c("litmus_30","litmus_35","litmus_36","litmus_40","litmus_50",
                "litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
                "litmus_11","litmus_12","litmus_13");
  results = list();
  file <- paste("plots/layout/R_S_L/traditional release",".jpg",sep="")
  
  jpeg(filename=file,width=600,height = 600);
  par(mfrow=c(2,2))
  for(i in 1:4){
    version = versions[i];
    result <- read.xls("../../tcp/data/result/result_100.xls",sheet=version);
    
    
   
    title = paste("APFD of ", version,sep="");
    boxplot(result$random,result$string,result$lda,
                      names = c("random","string","lda"),main = title); 
    
    bestOfThree <- max(result$random,result$string,result$lda);
    
   
  }
  
  dev.off()
  
  
  file <- paste("plots/layout/R_S_L/rapid release",".jpg",sep="")
  
  
  jpeg(filename=file,width=900,height = 900);
  par(mfrow=c(3,3))
  for(i in 5:13){
    version = versions[i];
    result <- read.xls("../../tcp/data/result/result_100.xls",sheet=version);
    title = paste("APFD of ", version,sep="");
    boxplot(result$random,result$string,result$lda,
            names = c("random","string","lda"),main = title); 
    
    
  }
  dev.off()
  
  
  
 
}




getClusterPlotRapid <-function(){
  versions <- c("litmus_30","litmus_35","litmus_36","litmus_40","litmus_50",
                "litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
                "litmus_11","litmus_12","litmus_13");
  results = list();
  file <- paste("plots/layout/cluster/traditional release",".jpg",sep="")
  
  jpeg(filename=file,width=600,height = 600);
  par(mfrow=c(2,2))
  for(i in 1:4){
    version = versions[i];
    result <- read.xls("../../tcp/data/result/result_100.xls",sheet=version);
    
    
    
    title = paste("APFD of ", version,sep="");
    median1= median(result$random);
    median2= median(result$string);
    median3= median(result$lda);
    
    median_best = max(median1,median2,median3);
    
    if(median_best == median1){
      bestOfThree=result$random;  
    }else if(median_best == median2){
      bestOfThree = result$string;
    }else if (median_best == median3){
      bestOfThree = result$lda;
    }
    
    boxplot(bestOfThree,result$cluster_string,result$cluster_random,
            names = c("best of three","cluster of string","cluster of random"),main = title); 
    
    
    
    
  }
  
  dev.off()
  
  
  file <- paste("plots/layout/cluster/rapid release",".jpg",sep="")
  
  
  jpeg(filename=file,width=900,height = 900);
  par(mfrow=c(3,3))
  for(i in 5:13){
    version = versions[i];
    result <- read.xls("../../tcp/data/result/result_100.xls",sheet=version);
    title = paste("APFD of ", version,sep="");
    median1= median(result$random);
    median2= median(result$string);
    median3= median(result$lda);
    
    median_best = max(median1,median2,median3);
    
    if(median_best == median1){
      bestOfThree=result$random;  
    }else if(median_best == median2){
      bestOfThree = result$string;
    }else if (median_best == median3){
      bestOfThree = result$lda;
    }
    
    boxplot(bestOfThree,result$cluster_string,result$cluster_random,
            names = c("best of three","cluster of string","cluster of random"),main = title); 
    
    
  }
  dev.off()
  
  
  
  
}

