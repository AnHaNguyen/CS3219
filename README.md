# CS3219 Assignment 1
Author:
Nguyen An Ha: A0113038U
Nguyen Le Huu Tue: A0112115A

This repository combines 2 projects: Assignment 1 ADT and Assignment 1 PipelineFilters, each of which is implemented in a different architecture: Abstract Data Type and Pipeline and Filters respectively.

Assignment 1 ADT:
-Input: The data would be read from 2 files, which contain words to be ignored and lines to be shifted. By default, the files should be created and stored by users in the same directory with the program under the name “ignore.txt” and “line.txt” respectively. At the start, users are prompted with an option to specify the directory they would want to store their files.
The ignore words are assumed to be separated by commas or spaces
The lines are assumed to be separated by new lines
-Output: The output will be both displayed on-screen and written to “output.txt” which is created in the current program’s directory.


Assignment 1 PipelineFilters:
-Input: To input ignore words and input lines, users can either run the GUI and input ignore words and input lines into respective text boxes, each must be separated by Enter. The result will be updated immediately after user enters a new ignore word or input line. Another way is to write the ignore words and input lines to the files: ignore-words.txt and input-lines.txt respectively, each word/line is separated by a new line, after that, click the "Import from files" button on GUI, the application will read ignore words and input lines from those 2 files and display the result on GUI.

-Output: The output will be displayed on GUI, however, if the output is too large for GUI to display, user can still view it in the file output.txt.
All 3 files: ignore-words.txt, input-lines.txt and output.txt are in the folder Assignment 1 PipelineFilters/.
