

.failedMean <- function(version){

fileName = "failed.txt";
con = file(fileName,open="r");
line = readLines(con);
long = length(line);
for(i in 1:long){
  temp = paste(line[i],".txt",sep="");
  line[i] <- sub("[?]","@",temp);
}

close(con);
a = nrow(version$lda.dists);
b = ncol(version$lda.dists);
for(i in 1:a){

  if(rownames(version$lda.dists)[i] %in% line){
    for(j in 1:b){
      
    }  
  }
  else{
    
  }

 
}

}

lda.mean.fail <- function(version){
  
  num_pass=0;
  num_fail=0;
  result = list()
  string_pass = vector();
  string_fail = vector()
  fileName = "failed.txt";
  con = file(fileName,open="r");
  line = readLines(con);
  long = length(line);
  for(i in 1:long){
    line[i] = paste(line[i],".txt",sep="");
   
  }
  close(con);
 
  for(i in 1:ncol(version$lda.dists)){
    temp_name = version$orderName[i,1]
    temp_name_2 <- sub("[@]","?",temp_name);
    if(temp_name_2 %in% line){
        num_fail = num_fail+1;
        string_fail[num_fail] <- temp_name; 
    }else{
        num_pass = num_pass+1;
        string_pass[num_pass] <- temp_name;
    }
    
  }
 
 
  result$fail.dists=matrix(nrow=num_fail,ncol=num_fail,dimnames=list(string_fail,string_fail));
  for(i in 1:num_fail){
    for(j in 1:num_fail){
     result$fail.dists[string_fail[i],string_fail[j]]=    version$lda.dists[string_fail[i],string_fail[j]];
    }
  }
  
  result$failpass.dists=matrix(nrow=num_fail,ncol=num_pass,dimnames=list(string_fail,string_pass));
  for(i in 1:num_fail){
    for(j in 1:num_pass){
      result$failpass.dists[string_fail[i],string_pass[j]]=version$lda.dists[string_fail[i],string_pass[j]];
    }
  }

  result$fail.mean = .myMean(result$fail.dists)
  result$failpass.mean = .myMean(result$failpass.dists)
  result$all.mean = .myMean(version$lda.dists)
  
  
  
}

.myMean <- function(mat,removeNum=10000){
  a = nrow(mat);
  b = ncol(mat);
  num = 0
  sum = 0
  for(i in 1:a){
    for(j in 1:b){
      temp = mat[i,j]
      if(temp != removeNum)
      {
        sum=sum+temp;
        num=num+1;
      }
    }    
  }
  result<- sum/num;
  result
  
 
}