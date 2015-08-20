.doTopicGreedy2 <- function(t){
  m = .doMaxSum(m=t)
  #get the row number and column number of theta 
  num_row = nrow(m)

  
  #define two vector, one is for order test cases, and the other is for the unordered test cases
  ordered = vector();
  unordered = vector(length=num_row, mode="numeric");
  for(i in 1:length(unordered)){
    unordered[i] = i;
  }
  
  random <- sample(1:num_row,1)
  ordered[1]= random
 # x=which(unordered==946)
 #unordered= unordered[-x]
 #print(unordered[x])
 #ordered_num = 1;
 #ordered[ordered_num] = 1;
  
  #use iterator to caculate the sum of each test case and find the biggest one 
  for(i in 1:num_row){
    
  
    big_ave = 0;
    big_index =1;
    
    for(j in 1:length(unordered))
    {
      temp_sum=0;
      temp_ave=0;
      current_unordered = unordered[j];
      
      #compute the sum of unordered[j] with ordered test cases
      for(n in 1:length(ordered)){ 
        temp_sum = temp_sum + m[ordered[n],current_unordered];  
      }
      
      temp_ave = temp_sum/length(ordered);
      
      if(temp_ave > big_ave){
        big_ave = temp_ave;
        big_index=current_unordered;
      }
      
      
    }
    
    ordered[i]=big_index;
    unordered=unordered[-which(unordered==big_index)];
 
  
      
  }
  print(ordered)
}

.doTopicGreedy <- function(t){
  m = .doMaxSum(m=t)
  #get the row number and column number of theta 
  num_row = nrow(m)
  
  
  #define two vector, one is for order test cases, and the other is for the unordered test cases
  ordered = vector();
  unordered = vector(length=num_row, mode="numeric");
  for(i in 1:length(unordered)){
    unordered[i] = i;
  }
  
  random <- sample(1:num_row,1)
  ordered[1]= random
  current_cov = vector()
  current_cov=m[ordered[1],]
 
  # x=which(unordered==946)
  #unordered= unordered[-x]
  #print(unordered[x])
  #ordered_num = 1;
  #ordered[ordered_num] = 1;
  
  #use iterator to caculate the sum of each test case and find the biggest one 
  for(i in 1:num_row){
    
    
    big_sum = 0;
    big_index =1;
    
    for(j in 1:length(unordered))
    {
      temp_sum=0;
      
      current_unordered = unordered[j];
       
      for(n in 1:length(current_cov)){
        temp_sum=temp_sum+max(current_cov[n],m[current_unordered,n]);
      }
      
      
    
      
      if(temp_sum > big_sum){
        big_sum = temp_sum;
        big_index=current_unordered;
      }else if(temp_sum==big_sum){
        temp_random <- sample(1:2,1);
        if(temp_random==2)
          {
            big_index= current_unordered;
        }
      }
      
      
    }
    
    ordered[i]=big_index;
    for(n in 1:length(current_cov)){
      current_cov[n]=max(current_cov[n],m[big_index,n]);
    }
    #print(current_cov)
    unordered=unordered[-which(unordered==big_index)];
    
    
    
  }
  ordered
}

#get the max sum of topic of each two test cases
.doMaxSum2 <- function(t){
  num_row = nrow(t)
  num_col = ncol(t)
  
  max_sum = matrix(nrow=num_row,ncol=num_row)
  
  for(i in 1:num_row){
    
    
    for(j in i:num_row){
      row_sum = 0
      for(n in 1:num_col){
        row_sum = row_sum + max(t[i,n],t[j,n])
      }
     
      max_sum[i,j]=row_sum
      max_sum[j,i]=row_sum
    }
  }
  
  max_sum
}

.doMaxSum <- function(m){
  
  t=t(sapply(m, function(row, max_length) c(row, rep(NA, max_length - length(row))), max(sapply(m, length))))
 
  
  t
}