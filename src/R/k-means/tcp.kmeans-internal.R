
.preComputeKmeans <- function(s){

 s <- .setInitDataFrame(s)

 init_tclist_data <- s$init_tclist_data
 #distinctMap <- s$distinctInitDataMap

 n_row <- nrow(init_tclist_data)
 n_col <- ncol(init_tclist_data)
  #---组建好data frame
  
  rnkMap = array(data=FALSE,dim=n_row) #位图 FALSE 表示下标未排序
  allCluster <- list()
  #初始化
  clRnkList = list();
  initCluster <- as.list(c(1:n_row))
  result <- .selectRandomTCFromCluster(initCluster,rnkMap);
  clRnkList <- append(clRnkList,result$target_value) #将随机存入已经排序中
  #更新rnkMap
  rnkMap <- result$rnkMap
  lastTi <- result$target_value
  allCluster[[length(allCluster)+1]] <- initCluster
  isDone <- .allClusterIsDone(allCluster,init_tclist_data)


  while(!isDone){
    #得到当前要进行分类的聚类信息
    current_tclist_index <- .getCurrentBiggestTCClusterIndex(allCluster,init_tclist_data)
    current_tclist <- allCluster[[current_tclist_index]]
    #从已有中的分类移除
    allCluster <- allCluster[-current_tclist_index]
    current_tclist_package <- .getTcClusterListData(init_tclist_data,current_tclist)
    current_tclist_data <- current_tclist_package$tclist_data
    current_tclist_data_map <- current_tclist_package$tclist_data_map

   # print(paste("tclist_data length:",nrow(current_tclist_data)))
    #聚类开始
    kmeans_result <- kmeans(x=current_tclist_data,centers=2)
    cluster_result <- kmeans_result$cluster
    divide_result <- .findTargetTiInCluster(cluster_result,current_tclist_data_map,rnkMap)

    
    #看Ti 在哪个聚类 来决定 Tj
    if(divide_result$whereIsTi == 1){
      Tj_result <- .selectRandomTCFromCluster(divide_result$CyList,rnkMap)
      }else{
        Tj_result <- .selectRandomTCFromCluster(divide_result$CxList,rnkMap)
    }

    rnkMap <- Tj_result$rnkMap 
    # print(rnkMap)
    Tj_value <- Tj_result$target_value
    lastTi <- Tj_value

    #存入排序数组
    clRnkList <- append(clRnkList,Tj_value)
    allCluster[[length(allCluster)+1]] <- divide_result$CxList
    allCluster[[length(allCluster)+1]] <- divide_result$CyList
    #是否已经完成
    isDone <- .allClusterIsDone(allCluster,init_tclist_data)
}

  for(i in 1:length(rnkMap)){
    if(!rnkMap[i]){
      clRnkList <- append(clRnkList,i)
    }
  }

  s$whichToTry <- array(dim=length(clRnkList),data=0)
  for(i in 1:length(s$whichToTry)){
    s$whichToTry[i] <- clRnkList[[i]]
  }

  s
}


.setInitDataFrame <- function(s){

  a = lapply(s$files, function(x){ paste(as.character(x), collapse=" ") })
  re <- lexicalize(a,lower=TRUE)
  # re = unlist(lapply(a, nchar)) # Get how many columns we need (max string length)
  
  
  my.vocab <- re$vocab
  my.doc <- re$documents
  n_col <- length(my.vocab)
  n_row <- s$howManyTests
  
  init_tclist_data <- matrix(ncol=n_col,nrow=n_row, data=0)
  init_tclist_data <- as.data.frame(init_tclist_data)
  colnames(init_tclist_data) <- my.vocab
  
  
  #所有tc进行循环
  for(i in 1:n_row){
    current <- my.doc[[i]][1,]
    # 0  1  2  3  4  5  6  7  8  0  9 10  8  0  7  1 11 12  0 13 14 15 16
    l_current <- length(current)
    #对一个document进行循环
    for(j in 1:l_current){
      position_of_word <-current[j]+1   #get position 
      init_tclist_data[i,position_of_word] = init_tclist_data[i,position_of_word]+1;
    }    
  }
 
  s$init_tclist_data <- init_tclist_data
 # s$distinctInitDataMap <- .buildDistinctMap(init_tclist_data)
  return(s)
}



#从cluster中选取一个随机数并删除它
.selectRandomTCFromCluster <- function(clusterList,rnkMap){
  l_cluster <- length(clusterList)
  #取随机数
  result <- list()
  
  random_index <- sample(1:l_cluster,1)
  target_value <- clusterList[[random_index]];
  

  while(rnkMap[target_value]){
    random_index <- sample(1:l_cluster,1)
    target_value <- clusterList[[random_index]];
  }
  

  rnkMap[target_value] <- TRUE
  result <- list()
  result$random_index <- random_index #随机下标
  result$target_value <- target_value #对应的数值
  result$rnkMap <- rnkMap
  #从clusterList中移除该下标数据
  #clusterList <- clusterList[-random_index]
  #result$clusterList <- clusterList
  return(result)
}


#得到cluster中最大cluster的下标
.getCurrentBiggestTCClusterIndex <- function(allCluster,init_tclist_data){
  ac_length <- length(allCluster);
  cluster_length_array <- array(dim=ac_length,data=0)

  max_length=0
  max_index =0
  for(i in 1:ac_length){
    current_cluster_list <- allCluster[[i]]
    current_cluster_length <- .distinctLength(current_cluster_list,init_tclist_data)

    if(current_cluster_length>max_length){

     # for(j in 1:length(current_cluster_list)){
       # if(!rnkMap[current_cluster_list[[j]]]){
           max_length=current_cluster_length
           max_index = i
      #  }
     # }
    
    }
  }
 
  return(max_index)
}


#为init_tclsit_data 建立一个单独的标签，如果一个矩阵两行相同，这两行标签相同
.buildDistinctMap <- function (init_tclist_data){
  n_row = nrow(init_tclist_data)
  n_col = ncol(init_tclist_data)

  distinctMap <- array(dim=n_row,data=0)



  for(i in 1:n_row){
    current_row <- init_tclist_data[i,]
    for(j in 1:i){
      previous_row　<- init_tclist_data[j,]

      if(.arrayEqual(current_row,previous_row　)){
        distinctMap[i] <- distinctMap[j]
        break
      }
    }
    if(distinctMap[i]==0){
      distinctMap[i] = i;
    }
  }
  return(distinctMap)
}


#判断两个数组是否相同
.arrayEqual <- function(arrayA, arrayB){
  equalArray <- (arrayA == arrayB)
  for(i in 1:length(equalArray)){
    if(!equalArray[i]){
      return(FALSE)
    }
  }

  return(TRUE)
}


# #clusterList 不重复的test case的数量
# .distinctLength <- function(clusterList,init_tclist_data){
#   if(length(clusterList)>3){
#       return(length(clusterList));
#   }
#   else{
#       clusterListDataResult <- .getTcClusterListData(init_tclist_data,clusterList)
#       distinctData <- .buildDistinctMap(clusterListDataResult$tclist_data)
#       return(length(unique(distinctData)))

# }
# }

.distinctLength <- function(clusterList,init_tclist_data){
  if(length(clusterList)>5){
      return(length(clusterList));
  }else{
    return(2)
  }
}

# .distinctLength <- function(clusterList,distinctMap){

#    resultArray <- array(dim = length(clusterList),data=0)

#     for(i in 1:length(clusterList)){
#       current <- clusterList[[i]]
#       resultArray[i] <- distinctMap[current]
#     }
#     return(length(unique(resultArray)))
# }



#判断所有聚类大小是否都小于或者等于2
.allClusterIsDone <- function(allCluster,init_tclist_data){
  #print(length(allCluster))
  for(i in 1:length(allCluster)){
    current_cluster <- allCluster[[i]] #选取第i个聚类
    if(.distinctLength(current_cluster,init_tclist_data)>2){
      return(FALSE);
    } 
  }
  return(TRUE);
}

# .notRankedLength <- function(cluster,rnkMap){
#   size = 0
#   for(i in 1:length(cluster)){
#     current <- cluster[[i]]
#     if(!rnkMap[current]){
#       size <- size+1
#     }
#   }

#   return(size)
# }

#通过聚类list来得到对应的数据,返回器对应的对应信息和数据
.getTcClusterListData <- function(init_tclist_data, tclist){
  l_tclist <- length(tclist)
  l_column <- ncol(init_tclist_data)

  tclist_data <- matrix(nrow=l_tclist,ncol=l_column,data=0)
  tclist_data_map <- array(dim=l_tclist,data=0)

  for(i in 1:l_tclist){
    current <- tclist[[i]]
    #tclist_data[i,] <- init_tclist_data[current,] 
    for(j in 1:l_column){
      tclist_data[i,j] <- init_tclist_data[current,j]
    }
    tclist_data_map[i] <- current
  }

  result <- list()
  result$tclist_data <- tclist_data
  result$tclist_data_map <- tclist_data_map

  return(result)
}


#如果Ti 在Cx中，返回1， 在Cy中，返回2
.findTargetTiInCluster <- function(cluster_result,tclist_data_map,rnkMap){
    CxList <- list()
    CyList <- list()
    whereIsTi = 0

    for(i in 1:length(cluster_result)){
      if(cluster_result[i]==1){
        #如果属于第一个分类，存入第一个分类
        CxList <- append(CxList,tclist_data_map[i])
        if(rnkMap[tclist_data_map[i]]){
          whereIsTi = 1
        }
        }else{
        #如果属于第二个分类，存入第二个分类
        CyList <- append(CyList,tclist_data_map[i])
        if(rnkMap[tclist_data_map[i]]){
          whereIsTi = 2 
        }
      }    
    }

    divive_result <- list()
    divive_result$whereIsTi <- whereIsTi
    divive_result$CxList <- CxList
    divive_result$CyList <- CyList
    return(divive_result)
}