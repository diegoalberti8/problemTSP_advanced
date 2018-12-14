postscript("Problems.IGD.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"../data/"
qIndicator <- function(indicator, problem)
{
fileNSGAII1<-paste(resultDirectory, "NSGAII1", sep="/")
fileNSGAII1<-paste(fileNSGAII1, problem, sep="/")
fileNSGAII1<-paste(fileNSGAII1, indicator, sep="/")
NSGAII1<-scan(fileNSGAII1)

fileNSGAII2<-paste(resultDirectory, "NSGAII2", sep="/")
fileNSGAII2<-paste(fileNSGAII2, problem, sep="/")
fileNSGAII2<-paste(fileNSGAII2, indicator, sep="/")
NSGAII2<-scan(fileNSGAII2)

fileNSGAII3<-paste(resultDirectory, "NSGAII3", sep="/")
fileNSGAII3<-paste(fileNSGAII3, problem, sep="/")
fileNSGAII3<-paste(fileNSGAII3, indicator, sep="/")
NSGAII3<-scan(fileNSGAII3)

fileNSGAII4<-paste(resultDirectory, "NSGAII4", sep="/")
fileNSGAII4<-paste(fileNSGAII4, problem, sep="/")
fileNSGAII4<-paste(fileNSGAII4, indicator, sep="/")
NSGAII4<-scan(fileNSGAII4)

fileSPEA21<-paste(resultDirectory, "SPEA21", sep="/")
fileSPEA21<-paste(fileSPEA21, problem, sep="/")
fileSPEA21<-paste(fileSPEA21, indicator, sep="/")
SPEA21<-scan(fileSPEA21)

fileSPEA22<-paste(resultDirectory, "SPEA22", sep="/")
fileSPEA22<-paste(fileSPEA22, problem, sep="/")
fileSPEA22<-paste(fileSPEA22, indicator, sep="/")
SPEA22<-scan(fileSPEA22)

fileSPEA23<-paste(resultDirectory, "SPEA23", sep="/")
fileSPEA23<-paste(fileSPEA23, problem, sep="/")
fileSPEA23<-paste(fileSPEA23, indicator, sep="/")
SPEA23<-scan(fileSPEA23)

fileSPEA24<-paste(resultDirectory, "SPEA24", sep="/")
fileSPEA24<-paste(fileSPEA24, problem, sep="/")
fileSPEA24<-paste(fileSPEA24, indicator, sep="/")
SPEA24<-scan(fileSPEA24)

algs<-c("NSGAII1","NSGAII2","NSGAII3","NSGAII4","SPEA21","SPEA22","SPEA23","SPEA24")
boxplot(NSGAII1,NSGAII2,NSGAII3,NSGAII4,SPEA21,SPEA22,SPEA23,SPEA24,names=algs, notch = TRUE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(2,3))
indicator<-"IGD"
qIndicator(indicator, "ProblemTSP")
