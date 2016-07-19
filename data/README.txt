GrowthIndicator
********************************************************************************************
* Developed by Jieun Choi
* Date : 2016.7.18.
* version : 0.1
* Required environment : Java Runtime Environment jre 1.8 (or higher version)
********************************************************************************************
* Program Requirements
1. 8 Data files should be in the directory. Names are like below :
        GI_height_male.csv
        GI_height_female.csv
        GI_weight_male.csv
        GI_weight_female.csv
        GI_head_male.csv
        GI_head_female.csv
        GI_bmi_male.csv
        GI_bmi_female.csv

2. Each data file has 13 fields which are separated by ',' except the end of each line

3. Fields in each file (8 files have same type) like below :
age by months, L value, M value, S value, 3rd, 5th, 10th, 25th, 50th, 75th, 90th, 95th, 97th

4. age by months should be like
 0, 0.5, 1.0, 1.5, ....
 ( increased by 0.5 )
