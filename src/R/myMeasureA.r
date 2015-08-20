require(gdata)

measureA <- function(a,b){
  # Computes the Vargha-Delaney A measure for two populations a and b.
  #
  # Equation numbers below refer to the paper:
  # @article{vargha2000critique,
  #  title={A critique and improvement of the CL common language effect size
  #               statistics of McGraw and Wong},
  #  author={Vargha, A. and Delaney, H.D.},
  #  journal={Journal of Educational and Behavioral Statistics},
  #  volume={25},
  #  number={2},
  #  pages={101--132},
  #  year={2000},
  #  publisher={Sage Publications}
  # }
  # 
  # Returns: A real number between 0 and 1 
  # TODO:
  #
  # Args
  #   a: a vector of real numbers
  #   b: a vector of real numbers 
  #
  # Results
  #   A real number between 0 and 1
  
  
  # Compute the rank sum (Eqn 13)
  r = rank(c(a,b))
  r1 = sum(r[seq_along(a)])
  
  # Compute the measure (Eqn 14) 
  m = length(a)
  n = length(b)
  A = (r1/m - (m+1)/2)/n
  
  A
}

myMeasureATran <-function()
{
  versions <- c("litmus_30","litmus_35","litmus_36","litmus_40");
                #,"litmus_50",
                #"litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
                #"litmus_11","litmus_12","litmus_13");
  
  results = list();
  output_result = matrix(ncol = 13, nrow = 4);
  names = c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse","measureA(RDD&R)",
            "measureA(RDD&TD)","measureA(RDD&TC)","measureA(RDD&RR)","p(RDD&R)", "p(RDD&TD)","p(RDD&TC)","p(RDD&RR)");
  colnames(output_result) <- names;
  rownames(output_result) <-versions;
  
  
  for(i in 1:4){
    version = versions[i];
    result <- read.xls("../data/result/result_100.xls",sheet=version);
  
    title = paste("APFD of ", version,sep="");

    output_result[i,1] <- median(result$random);
    output_result[i,2] <- median(result$string);
    output_result[i,3] <- median(result$lda);
    output_result[i,4] <- median(result$cluster_random);
    output_result[i,5] <- median(result$cluster_string);
    
    output_result[i,6] <- measureA(result$cluster_string,result$random);
    output_result[i,7] <- measureA(result$cluster_string,result$string);
    output_result[i,8] <- measureA(result$cluster_string,result$lda);
    output_result[i,9] <- measureA(result$cluster_string,result$cluster_random);
    
    test <- wilcox.test(result$cluster_string,result$random)
    output_result[i,10] <- wilcox.test(result$cluster_string,result$random)$p.value;
    output_result[i,11] <- wilcox.test(result$cluster_string,result$string)$p.value;
    output_result[i,12] <- wilcox.test(result$cluster_string,result$lda)$p.value;
    output_result[i,13] <- wilcox.test(result$cluster_string,result$cluster_random)$p.value;
    
    
#     boxFig <- boxplot(result$random,result$string,result$lda,result$cluster_random,result$cluster_string,
#                       names = c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse")
#                       ,ylim=c(0,100), main = title,ylab="APFD",cex.lab=1.3,cex=1.2,cex.main=2,lwd=1.2,lwd.ticks=2); 
    
  }
print(output_result)
}


myMeasureARapid <-function()
{
  versions <- c("litmus_50",
  "litmus_60","litmus_70","litmus_80","litmus_90","litmus_10",
  "litmus_11","litmus_12","litmus_13");
  
  results = list();
  output_result = matrix(ncol = 13, nrow = 9);
  names = c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse","measureA(RDD&R)",
            "measureA(RDD&TD)","measureA(RDD&TC)","measureA(RDD&RR)","p(RDD&R)", "p(RDD&TD)","p(RDD&TC)","p(RDD&RR)");
  colnames(output_result) <- names;
  rownames(output_result) <-versions;
  
  
  for(i in 1:9){
    version = versions[i];
    result <- read.xls("../data/result/result_100.xls",sheet=version);
    
    title = paste("APFD of ", version,sep="");
    
    output_result[i,1] <- median(result$random);
    output_result[i,2] <- median(result$string);
    output_result[i,3] <- median(result$lda);
    output_result[i,4] <- median(result$cluster_random);
    output_result[i,5] <- median(result$cluster_string);
    
    output_result[i,6] <- measureA(result$cluster_string,result$random);
    output_result[i,7] <- measureA(result$cluster_string,result$string);
    output_result[i,8] <- measureA(result$cluster_string,result$lda);
    output_result[i,9] <- measureA(result$cluster_string,result$cluster_random);
    
    test <- wilcox.test(result$cluster_string,result$random)
    output_result[i,10] <- wilcox.test(result$cluster_string,result$random)$p.value;
    output_result[i,11] <- wilcox.test(result$cluster_string,result$string)$p.value;
    output_result[i,12] <- wilcox.test(result$cluster_string,result$lda)$p.value;
    output_result[i,13] <- wilcox.test(result$cluster_string,result$cluster_random)$p.value;
    
    
    #     boxFig <- boxplot(result$random,result$string,result$lda,result$cluster_random,result$cluster_string,
    #                       names = c("random","TextDiversity","TopicCoverage","RiskDrivenRandom","RiskDrivenDiverse")
    #                       ,ylim=c(0,100), main = title,ylab="APFD",cex.lab=1.3,cex=1.2,cex.main=2,lwd=1.2,lwd.ticks=2); 
    
  }
  print(output_result)
}