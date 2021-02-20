LineDiff
=========
Small command line tool to compare two files and identify which lines they have in common and which they don't 

Metrics: 
The Build is ![Buildstatus](https://github.com/patrickuhlmann/linediff/workflows/Main/badge.svg)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=coverage)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=ncloc)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)

## Current Release

The latest version is 
<a href="https://github.com/patrickuhlmann/linediff/releases"><img src="https://img.shields.io/github/release/patrickuhlmann/linediff.svg" alt="github release"></a>

## Getting Started

Given two input files this tool generates three output files.
  * both.txt = all lines that appear in both files (sorted)
  * first_only.txt = all lines that appear in the first file (sorted)
  * second_only.txt = all lines that appear in the second file (sorted)

It is designed to use low memory in order to enable it to compare very large files. It is able to process 1 GB large files with -Xmx64 in 60 seconds

Furthermore it contains additional tools to preprocess the input files. Those are:
  * externalsort: to sort a file using temporary files on the harddisk instead of main memory
  * split: to split a file into pieces after a given number of lines
  * decodeurl: to decode every line in case it contains urls
  * countlines: to count the number of lines in a file
  * removelines: to remove lines that match a given pattern from the file
  * replace: to replace a given pattern with another given pattern on every line
  
All tools process line by line. The input files are expected to be UTF-8. All tools check it the output files already exist and abort in such a case

Cross-plattform note: all tools will write line endings native to the plattform on which they are executed. e. g. if you take a file having windows-line-endings and run the tool under linux, the result file will have linux line-endings!

This tool is using Semantic Versioning.

## Usage

 * `java -jar LineDifference-<release>.jar linediff [firstfile] [secondfile] [outputfolder]` <br>
 * `java -jar LineDifference-<release>.jar externalsort [inputfile] [outputfile]` <br>
 * `java -jar LineDifference-<release>.jar split [inputfile] [numberOfLines]` <br>
 * `java -jar LineDifference-<release>.jar decodeurl [inputfile] [outputfile]` <br>
 * `java -jar LineDifference-<release>.jar countlines [inputfile] [searchPattern]` <br>
 * `java -jar LineDifference-<release>.jar removelines [inputfile] [searchPattern]` <br>
 * `java -jar LineDifference-<release>.jar replace [inputfile] [outputfile] [searchPattern] [replacePattern]` <br>

If you want to control the memory usage, use java -jar -Xmx128m ← adapt as it makes sense

The split command will create the output files with the same name as the input file and a numberd suffix (e. g. input_1.txt, input_2.txt, ...)

All patterns are Java 8 patterns. You find a reference here: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html

## Building

It uses JDK 11

 * `./gradlew jar` <br>
Creates a jar that can be executed. It also creates a source and a javadoc jar.

 * `./gradlew check` <br>
Executes the unit tests and generates various reports (checkstyle, jacoco, pmd, spotbugs, tests)

 * `./gradlew nativeImage` <br>
Creates a graal native image
