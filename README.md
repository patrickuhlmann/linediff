License:
![GitHub](https://img.shields.io/github/license/patrickuhlmann/linedifference)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fpatrickuhlmann%2Flinedifference.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fpatrickuhlmann%2Flinedifference?ref=badge_shield)

Build: [![Build Status](https://travis-ci.com/patrickuhlmann/linedifference.svg?branch=master)](https://travis-ci.com/patrickuhlmann/linedifference)

Metrics: 
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=alert_status)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=security_rating)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=bugs)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=code_smells)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=coverage)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=ncloc)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=sqale_index)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=patrickuhlmann_linedifference&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=patrickuhlmann_linedifference)

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

## Commandline Interface

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
Creates a jar that can be executed

 * `./gradlew check` <br>
Executes the unit tests and generates various reports (unit test, coverage, pmd)

 * `./gradlew pitest` <br>
Executes mutation testing, note that the findings are not necessarily bugs

## License

The code is available under the terms of the MIT License

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fpatrickuhlmann%2Flinedifference.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fpatrickuhlmann%2Flinedifference?ref=badge_large)