#top topics
.getTopFive <-
  function(result){
    result$topfive=matrix(nrow=nrow(result$topics),ncol=5)
    test=matrix(data={0},nrow=nrow(result$topics),ncol=5)
for (i in 1:nrow(result$topics)){
  

  
  for(j in 1:ncol(result$topics))
  {
    temp = result$topics[i,j];
    
    if(temp > test[i,1])
    {
      for(k in 5:2)
      {
        result$topfive[i,k]=result$topfive[i,k-1];
        test[i,k]=test[i,k-1]
      }
      result$topfive[i,1]=paste(colnames(result$topics)[j],temp);
      test[i,1]=temp;
     
    }else  if(temp > test[i,2])
    {
      for(k in 5:3)
      {
        result$topfive[i,k]=result$topfive[i,k-1];  
        test[i,k]=test[i,k-1]
      }
      result$topfive[i,2]=paste(colnames(result$topics)[j],temp);
      test[i,2]=temp;
      
    }
    
    else  if(temp > test[i,3])
    {
      for(k in 5:4)
      {
        result$topfive[i,k]=result$topfive[i,k-1];  
        test[i,k]=test[i,k-1]
      }
      result$topfive[i,3]=paste(colnames(result$topics)[j],temp);
      test[i,3]=temp;
      
    }else  if(temp > test[i,4])
    {
      for(k in 5:5)
      {
        result$topfive[i,k]=result$topfive[i,k-1];  
        test[i,k]=test[i,k-1]
      }
      result$topfive[i,4]=paste(colnames(result$topics)[j],temp);
      test[i,4]=temp;
      
    }
    else  if(temp > test[i,5])
    {
      
      result$topfive[i,5]=paste(colnames(result$topics)[j],temp);
      test[i,5]=temp;
      
    }
  }
  
}
 result
}

#get random vector for evaluation
.getRandomOrder <- 
  function(s)
{
  s$whichToTry <- vector(length=s$howManyTests)
  
  for(n in 1:s$howManyTests)
  {
    s$whichToTry[n]=n
  }
  
  for(n in 1:s$howManyTests)
  {
    random <- sample(1:s$howManyTests,1)
    temp <- s$whichToTry[n]
    s$whichToTry[n] <- s$whichToTry[random]
    s$whichToTry[random] <-temp
   # (s$whichToTry[n],s$whichToTry[random]) <-.swapTwoNumber(a=s$whichToTry[n],s$which)
  }
  s
}


