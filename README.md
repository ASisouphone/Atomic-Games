# Othello 2018

Welcome to the 2018 Atomic Games! This year's challenge is Othello.

Clone this repository to access the SDKs, and download the game [JAR file] (http://atomobj.io/othello) to run the game server.

## Usage

To run the game, you'll need a recent Java VM and the JAR file mentioned above. Invoke the game from the command line:

    $ java -jar othello.jar [args]

## Options

You can specify that the server should invoke your player, use a "robot" player with a predetermined set of moves, or use a random player for one or both players.

The player can be one of three types:
remote - the game will listen for a player to connect to the server
random - the game will make a random valid move for the player
robot - the game use moves specified in the --p1-moves or --p2-moves argument

You'll most likely want to run with your client as a remote player, and a random player for the opponent.

The game will log moves to the console and run a webserver on port 8080 for a UI.
Use the --ui-port to specify a different UI port.
Pass the --wait-for-ui option in order to have the server wait for a UI connection before starting the game.

The game will by default time out if a player has not responeded within 15 seconds
You can change this with the --max-turn-time arg (--max-turn-time 20000 for 20 seconds).

Usage:
java -jar othello.jar
java -jar othello.jar --p1-type remote --p2-type random --wait-for-ui

Options:
      --p1-type TYPE          :remote     Player one's type - remote, random, or robot
      --p2-type TYPE          :remote     Player two's type - remote, random, or robot
      --p1-name NAME          Player One  Player one's team name
      --p2-name NAME          Player Two  Player two's team name
      --p1-moves MOVES        []          Moves for a P1 robot player
      --p2-moves MOVES        []          Moves for a P2 robot player
      --p1-port PORT          1337        Port number for the P1 client
      --p2-port PORT          1338        Port number for the P2 client
      --ui-port PORT          8080        Port number for UI clients
  -w, --wait-for-ui                       Wait for a UI client to connect before starting game
  -m, --min-turn-time MILLIS  1000        Minimum amount of time to wait between turns.
  -x, --max-turn-time MILLIS  15000       Maximum amount of time to allow an AI for a turn.
  -h, --help

## Starter Kits

Look under the /sdks directory to find starter kits for C#, Java, and Python. If you'd rather use a different language - feel free to do so and you can use the starter kits as a reference.

The starter kit implementations will connect to the game server, read a board state, and then send a response. You'll need to modify them to analyze the game state and return an optimal move.

## Moves
When the game server starts, it will wait for players to connect, then begin executing moves until it determines a winner.

When the game server needs a move from your client it will send the game state as JSON, followed by a newline. For example:

{"board":[[0,0,0,0,0,0,0,0],[0,0,0,0,2,0,0,0],[0,0,0,0,2,0,0,0],[0,0,0,1,2,0,0,0],[0,0,0,1,2,2,0,0],[0,0,0,1,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]],"maxTurnTime":15000,"player":1}\n

The "board" data structure is a list of game board rows, from the top to the bottom of the game board. A "0" indicates an empty square, a "1" indicates a player one piece, and a "2" indicates a player two piece.

Note the "player" field - read this field to determine if you are player one or player two. Your client may be invoked as either player.

When you've computed a move, return it as a JSON array, followed by a newline, for example:

[1, 2]\n

The coordinate system begins at the top left of the board. The coordinates are in [row, column] format. So [7,0] would indicate the lower left corner of the board. [0,0] indicates the top left corner and [7,7] indicates the bottom right corner.

Be sure to terminate your response with the newline, otherwise your move will timeout.

Returning an invalid move will forfeit the game. Timing out (the default timeout is 15 seconds) will also forfeit the game.

## License

Copyright © 2018

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
