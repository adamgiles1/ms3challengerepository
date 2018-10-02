Adam Giles 10/2/2018

this program was made using maven, so the pom.xml file should contain all dependencies needed to run the file. The program was coded and tested using intellij, but it should run using any ide.

The program should work just by running main, with the one pre-requisite being the csv filename must be given on line 95 in the method openCsv. If the csv is not in the programs main folder, you must put the entire path in as a parameter.

for the approach I took, the program is pretty straight forward. it takes the csv file, and goes through it one line at a time using the open source library opencsv. for each line it checks it to make sure that every row is filled out, and then if it is valid it writes it to the database, and if it is not valid it writes it to the csv of failed records. After finishing, it outputs the file log.txt with the successrate information.

When I made the database, I had to decide the data types. most of the data in the csv is text, but column G is representing money, which is effectively a decimal number, and columns H and I are boolean. I decided to keep everything as a string/text type, as I do not have information on how they will be used in the database. Depending on how the data will be utilized in the database, it may have been better to store it as a number instead of characters, but since I did not have that information I figured that keeping it as intact as possible was the way to go.

at the end of the program, I additionally made it write all records in the sqlite database to the console to show that it worked.

I at first couldn't decide if I wanted to store the valid lines and then write them all to the database in one go, or if I wanted to write them one line at a time. I decided to write them one line at a time, as if the data quantity was higher, it might take some time to fully execute, and this way if it somehow is exited partially through processing it could be restarted where it left off instead of having to redo the work.