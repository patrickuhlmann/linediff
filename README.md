[![Build Status](https://travis-ci.com/patrickuhlmann/linedifference.svg?branch=master)](https://travis-ci.com/patrickuhlmann/linedifference)

## Getting Started

This is a command line tool which compares two input files. It creates an output folder containing three files:
  * both.txt = all lines that appear in both files (sorted)
  * first_only.txt = all lines that appear in the first file (sorted)
  * second_only.txt = all lines that appear in the second file (sorted)

It is designed to use low memory in order to enable it to compare also large files.

If the lines in an input file are not yet sorted, the tool uses external sorting (files are splitted in chunks of 20 mB) in order to lower the RAM usage. The input files are not manipulated during execution. Sorted copies are created in the output folder

It expects the following conditions to hold:
- the two input files must exist
- the above listed files mustn't exist in the output folder 
- if the input files are not sorted already, the output folder needs to be able to store a sorted copy (prefixed with sorted_). Also the temp directory needs to have enough space to store the splitted files during the sorting phase
- neither unsorted input file should be larger than 20 gB -> limit not enforced
- individual lines are always read in completely, if they are to long, they blow the memory -> no limit enforced
- the encoding of the input files should be UTF-8

## Commandline Interface 
```
Usage: java -jar LineDifference-1.0-SNAPSHOT.jar [firstfile] [secondfile] [outputfolder]
```
If you want to control the memory usage, use java -jar -Xmx128m ← adapt as it makes sense

## Performance
- Sorting and diffing two 1 GB files in 60seconds using -Xmx64m

## Building

It uses JDK 8

 * `gradlew jar` <br>
Creates a jar that can be executed
 * `gradlew check` <br>
Executes the unit tests and generates a report

## License

The code is available under the terms of the MIT License
