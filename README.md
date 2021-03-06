# GrowthIndicator
-----

    Developed by Jieun Choi
    Date : 2016.7.18.
    Last Update : 2016. 9. 19.
    version : 1.0
    Required environment : Java Runtime Environment jre 1.8 (or higher version)

## Program Requirements
1. 8 Data files should be in the directory. Names are like below :
 - GI\_height\_male.csv
 - GI\_height\_female.csv
 - GI\_weight\_male.csv
 - GI\_weight\_female.csv
 - GI\_head\_male.csv
 - GI\_head\_female.csv
 - GI\_bmi\_male.csv
 - GI\_bmi\_female.csv

2. Each data file has 13 fields which are separated by ',' except the end of each line

3. Fields in each file (8 files have same type) like below :
age by months, L value, M value, S value, 3rd, 5th, 10th, 25th, 50th, 75th, 90th, 95th, 97th

4. age by months should be like
 0, 0.5, 1.0, 1.5, ....
 ( increased by 0.5 )

## How to Compile
- Using IntelliJ
 1. Set Project SDK to 1.8 
 (File -> Project Structure -> Project)
 2. Set Project Language Level to 8
 (File -> Project Structure -> Project)
 3. Set output path
   (File -> Project Structure -> Project, bottom of right panel -> New folder 'out' in GrowthIndicator directory)
 4. Import GrowthIndicator module
  (File -> Project Structure -> (left pane) Modules -> '+' -> import Module)
 5. Add library module (commons-math3-3.6.jar) and 
    (File -> Project Structure -> (right pane) Dependencies tab -> '+' -> JARs or directories) 
 6. Set 'data' directory as a resource directory
  (File -> Project Structure -> (left pane) Modules -> choose 'data' directory -> set as resource directory)
 7. Set 'res' and 'lib' as resource directories

 

## How to Run
 - Using IntelliJ
   - Make Project (Ctrl+F9)
   - Run Main (Shift+F10)
 - Using Command Line (in the project directory)
   - java -cp .;commons-math3-3.6.1.jar com.cje.Main


