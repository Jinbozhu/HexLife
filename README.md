HexLife
=============
This is a variance of [Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life). Instead of having 8 neighbors, each cell in HexLife has 6 or 12 neighbors depending on your choice.

To run this program: 

1. Change directory to /THE_PATH_TO_THE_FOLDER/HexLife/out/artifacts/HexLife_jar

2. Type "Java -jar HexLife.jar" in terminal to run it using default setting.

3. You can specify parameters using the following flags:

	-12: use the 12 neighbor rules (default is the 6 neighbor rules)

	-size n: specify the size of the grid to be n X n (default is 100)

	-f filename: read in the initial configuration from the specified file (see below for input file format)

	-g n: specifiy the number of generations to simulate (default is 10)

	-p n: specify that every nth generation should be printed (for plain text output only, default is 1)

	-i p: specify the probabilty of a cell being alive in the initial configuration if no file is provided (default is .5)

For example, type "Java -jar HexLife.jar -12 -size 10 -f /Users/Jinbo/Desktop/board.txt" in the terminal, it will activate the 12-neighbor mode, the size of the board will be 10, and it will load the initial board from /Users/Jinbo/Desktop/board.txt.
