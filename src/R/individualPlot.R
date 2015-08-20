require(gdata)
#plot the tranditional release result
getresultTraditional <-function(){
  versions <- c("litmus_30","litmus_35","litmus_36","litmus_40","litmus_50",
                "litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
                "litmus_11","litmus_12","litmus_13");
  results = list();
  par(mfrow=c(2,2))
  for(i in 1:4){
    version = versions[i];
    result <- read.xls("../data/result/result_100.xls",sheet=version);

    title = paste("APFD of ", version,sep="");
    
    #set data and the name
   # boxFig <- boxplot(result$random,result$string,result$lda,result$cluster_random,result$cluster_string,
   #                   names = c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse"), main = title); 
   boxFig <- boxplot(result$random,result$string,result$lda,result$cluster_random,result$cluster_string,
                     names = c("random","TextDiversity","Topic\nCoverage","RiskDrivenRandom","RiskDrivenDiverse")
                     ,ylim=c(0,100), main = title,ylab="APFD",cex.lab=1.3,cex=1.2,cex.main=2,lwd=1.2,lwd.ticks=2); 
  }

}

#plot rapid release result
getresultRapid <-function(){
  versions <- c("litmus_30","litmus_35","litmus_36","litmus_40","litmus_50",
                "litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
                "litmus_11","litmus_12","litmus_13");
  results = list();
  par(mfrow=c(3,3));
  for(i in 5:13){
    version = versions[i];
    result <- read.xls("../data/result/result_100.xls",sheet=version);
    
    title = paste("APFD of ", version,sep="");
    boxFig <- boxplot(result$random,result$string,result$lda,result$cluster_random,result$cluster_string,
                      names = c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse")
                      ,ylim=c(0,100), main = title,ylab="APFD",cex.lab=1.3,cex=1.2,cex.main=2,lwd=1.2,lwd.ticks=2); 

  }

}



#plot the tranditional release result
getresultTraditional.rotate <-function(){
  versions <- c("litmus_30","litmus_35","litmus_36","litmus_40","litmus_50",
                "litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
                "litmus_11","litmus_12","litmus_13");
  results = list();
  par(mfrow=c(2,2))
  for(i in 1:4){
    version = versions[i];
    result <- read.xls("../data/result/result_100.xls",sheet=version);
    names = c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse");
    title = paste("APFD of ", version,sep="");
    
    #set data and the name
    # boxFig <- boxplot(result$random,result$string,result$lda,result$cluster_random,result$cluster_string,
    #                   names = c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse"), main = title); 
    boxFig <- boxplot(result$random,result$string,result$lda,result$cluster_random,result$cluster_string,
                      ylim=c(0,100), main = title,ylab="APFD",cex.lab=1.3,cex=1.2,cex.main=2,lwd=1.2,lwd.ticks=2); 
    text(x = seq(1, 5, by=1), par("usr")[3] - 0.2, labels = names, srt = 30, pos = 1, xpd = TRUE)
  }
  
}

#plot rapid release result
getresultRapid.rotate <-function(){
  versions <- c("litmus_30","litmus_35","litmus_36","litmus_40","litmus_50",
                "litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
                "litmus_11","litmus_12","litmus_13");
  results = list();
  par(mfrow=c(3,3));
  for(i in 5:13){
    version = versions[i];
    result <- read.xls("../data/result/result_100.xls",sheet=version);
    names =  c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse");
    title = paste("APFD of ", version,sep="");
    boxFig <- boxplot(result$random,result$string,result$lda,result$cluster_random,result$cluster_string,
                      
                      ylim=c(0,100), main = title,ylab="APFD",cex.lab=1.3,cex=1.2,cex.main=2,lwd=1.2,lwd.ticks=2); 
    text(x = seq(1, 5, by=1), par("usr")[3] - 0.2, labels = names, srt = 30, pos = 1, xpd = TRUE);
  }
  
}

