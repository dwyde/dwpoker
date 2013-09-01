# Introduction
This program allows you to play limit Texas Hold 'Em poker against different bots.  It currently features 4 players: 1 human and 3 computers.

I wrote this program as an Independent Study Project at New College of Florida, in January 2009.

# Use
The program is written in Java and should run in any environment that has Java Runtime Environment installed.

- On Windows and Mac, go into the `release` directory, and double-click the file `dwpoker.jar`.
- On Linux, run `cd release; java -jar dwpoker.jar`.

If the images don't appear, you're running it from the wrong directory.  

# Miscellaneous
- Note that the white [D] indicates the dealer.  Other than that, I hope the interface is intuitive.
- Although the program can handle a variable number of players, it is currently hard-coded to have 4.
- There's nothing to stop bankrolls from going negative.  If this bothers you, please delete the "data/bankrolls.dat" file and it will be recreated.

# Licensing
The files in the `images/board` directory are originally from KDE 3.5.9, which I suppose puts them under the LGPL.

