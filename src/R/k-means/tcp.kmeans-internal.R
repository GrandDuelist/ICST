
.preComputeKmeans <-
  function(s){

  a = lapply(s$files, function(x){ paste(as.character(x), collapse=" ") })
  re <- lexicalize(a,lower=TRUE)
  # re = unlist(lapply(a, nchar)) # Get how many columns we need (max string length)
  
  
  my.vocab <- re$vocab
  my.doc <- re$documents
  n_col <- length(my.vocab)
  n_row <- s$howManyTests
  
  init_tclist <- matrix(ncol=n_col,nrow=n_row, data=0)
  init_tclist <- as.data.frame(init_tclist)
  colnames(init_tclist) <- my.vocab
  #所有tc进行循环
  for(i in 1:n_row){
    current <- my.doc[[i]][1,]
    # 0  1  2  3  4  5  6  7  8  0  9 10  8  0  7  1 11 12  0 13 14 15 16
    l_current <- length(current)
    #对一个document进行循环
    for(j in 1:l_current){
      position_of_word <-current[j]+1   #get position 
      init_tclist[i,position_of_word] = init_tclist[i,position_of_word]+1;
    }
    clRnkList = list();
   # rnkMap = array(data=FALSE,dim=n_row) #位图 FALSE 表示下标未排序
    
    #当前较大的聚类
    currentBiggestCluster
    #
    
    
    
    
    
  }
  print(init_tclist)
  #存入一个tc的向量
  s$re <- re
}

#从cluster中选取一个随机数并删除它
.selectRandomTCFromCluster <- function(clusterList){
  l_cluster <- length(clusterList)
  #取随机数
  result <- list()
  random_index <- sample(1:l_cluster,1)
  target_value <- clusterList[[random_index]];
  
  result$random_index <- random_index #随机下标
  result$target_value <- target_value #对应的数值
  
  #从clusterList中移除该下标数据
  clusterList <- clusterList[-random_index]
  result$clusterList <- clusterList
  return(result)
}



