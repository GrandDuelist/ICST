getPriorityFileName <- function(s)
{
  s$priorityFileName=matrix(nrow=length(s$whichToTry),ncol=1);
  for(i in 1:nrow(s$priorityFileName))
  {
    temp = s$whichToTry[i]
    s$priorityFileName=[i,1]=names(s$files)[temp];
   
    
  }
 
  s$priorityFileName
}