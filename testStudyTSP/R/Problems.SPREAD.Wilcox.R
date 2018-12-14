write("", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex",append=FALSE)
resultDirectory<-"testStudyTSP/data"
latexHeader <- function() {
  write("\\documentclass{article}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\title{StandardStudy}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\usepackage{amssymb}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\begin{document}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\maketitle", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\section{Tables}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\caption{", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write(problem, "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write(".SPREAD.}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)

  write("\\label{Table:", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write(problem, "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write(".SPREAD.}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)

  write("\\centering", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\begin{scriptsize}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\begin{tabular}{", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write(tabularString, "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write(latexTableFirstLine, "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\hline ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
}

latexTableTail <- function() { 
  write("\\hline", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\end{tabular}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\end{scriptsize}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  write("\\end{table}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
}

printTableLine <- function(indicator, algorithm1, algorithm2, i, j, problem) { 
  file1<-paste(resultDirectory, algorithm1, sep="/")
  file1<-paste(file1, problem, sep="/")
  file1<-paste(file1, indicator, sep="/")
  data1<-scan(file1)
  file2<-paste(resultDirectory, algorithm2, sep="/")
  file2<-paste(file2, problem, sep="/")
  file2<-paste(file2, indicator, sep="/")
  data2<-scan(file2)
  if (i == j) {
    write("-- ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  }
  else if (i < j) {
    if (wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE) 
      }
    }
    else {
      write("--", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
  }
}

### START OF SCRIPT 
# Constants
problemList <-c("ProblemTSP") 
algorithmList <-c("NSGAII1", "NSGAII2", "NSGAII3", "NSGAII4", "SPEA21", "SPEA22", "SPEA23", "SPEA24") 
tabularString <-c("lccccccc") 
latexTableFirstLine <-c("\\hline  & NSGAII2 & NSGAII3 & NSGAII4 & SPEA21 & SPEA22 & SPEA23 & SPEA24\\\\ ") 
indicator<-"SPREAD"

 # Step 1.  Writes the latex header
latexHeader()
# Step 2. Problem loop 
for (problem in problemList) {
  latexTableHeader(problem,  tabularString, latexTableFirstLine)

  indx = 0
  for (i in algorithmList) {
    if (i != "SPEA24") {
      write(i , "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
      write(" & ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
      jndx = 0 
      for (j in algorithmList) {
        if (jndx != 0) {
          if (indx != jndx) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
          }
          if (j != "SPEA24") {
            write(" & ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
          }
          else {
            write(" \\\\ ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
          }
        }
        jndx = jndx + 1
      }
      indx = indx + 1
    }
  }

  latexTableTail()
} # for problem

tabularString <-c("| l | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{1}{c|}{NSGAII2} & \\multicolumn{1}{c|}{NSGAII3} & \\multicolumn{1}{c|}{NSGAII4} & \\multicolumn{1}{c|}{SPEA21} & \\multicolumn{1}{c|}{SPEA22} & \\multicolumn{1}{c|}{SPEA23} & \\multicolumn{1}{c|}{SPEA24} \\\\") 

# Step 3. Problem loop 
latexTableHeader("ProblemTSP ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "SPEA24") {
    write(i , "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
    write(" & ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
          } 
          if (problem == "ProblemTSP") {
            if (j == "SPEA24") {
              write(" \\\\ ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
            } 
            else {
              write(" & ", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
            }
          }
     else {
    write("&", "testStudyTSP/R/Problems.SPREAD.Wilcox.tex", append=TRUE)
     }
        }
      }
      jndx = jndx + 1
    }
    indx = indx + 1
  }
} # for algorithm

  latexTableTail()

#Step 3. Writes the end of latex file 
latexTail()

