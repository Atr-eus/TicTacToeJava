## Project Overview

This project is an overengineered version of a simple and classic game: Tic-Tac-Toe. On top of the game itself, the project implements a login/registration system to keep track of some relevant user data that might help the game feel more personal while playing. In the structural flow, first, the user(s) will be prompted to either register or log in with their personal account(s).

The login page here might feel a little out of place, but there is a good reason for it to be the way it is. To avoid complication for a project with zero intricacies, there is no over-the-internet play, which means both players will have to be logged in simultaneously and play on a single process/window.

Upon registration, a predictable success (or failure if the email is already in use) message will be displayed. Upon success, the entry will be registered to a table in a MySQL database from which it will be accessed in the future for logging the user in.
In the case of login, the prompt will ask two users to log in at the same time.

A game will only begin if both users are logged in successfully. However, it will prompt the user to load the last unfinished game between the pair instead of starting a new game. This offer can either be accepted or rejected. Regardless of the choice, a new window will open, displaying some relevant user information on top and displaying the 3x3 grid for the Tic-Tac-Toe game below it. From here, the player can simply proceed to play the game. If the outcome is not a draw, the win count of the victorious player will be increased by one.
## Features

- Login and registration system powered by MySQL integration.
- Keeps track of the win count for each player via their account.
- Offers an intuitive interface for playing the game via GUI.
- 1v1 between two humans.

## MySQL Schema

The default `xampp` configuration for MySQL server is hardcoded in the project for now.

- Create database `entries` and use it for subsequent operations.
```sql
CREATE DATABASE IF NOT EXISTS entries;
USE entries;
```

- Create table `credentials` for storing account information.
```sql
CREATE TABLE IF NOT EXISTS credentials (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    won INT DEFAULT 0,
    passwd VARCHAR(255)
);
```

- Create table `saved_games` for storing information of the latest unfinished game between a distinct pair of players.
```sql
CREATE TABLE IF NOT EXISTS saved_games (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player1_email VARCHAR(100),
    player2_email VARCHAR(100),
    board_state VARCHAR(9), -- serialized board format like "XOX_O____"
    turn BOOLEAN,           -- true = O's turn, false = X's turn
    UNIQUE (player1_email, player2_email) -- unique player pair
);
```

**PS**: No need to run these manually. As long as the server is running on the fixed URL, the program will create DB and tables according to this schema.

## Screenshots

Some screenshots of each window.

- Welcome page.

![welcome page](https://images2.imgbox.com/27/5a/Mq5iU360_o.png)

- Registration page.

![registration page](https://images2.imgbox.com/d7/d7/Dyb0xTs5_o.png)

- Login page and saved game detection.

![login page and saved game detection](https://images2.imgbox.com/73/98/9kSufZnw_o.png)

- Game board.

![game board](https://images2.imgbox.com/a1/2a/C2ixdpmZ_o.png)

## Shortcomings

- No full-database encryption. The user accounts are not secure.
- Prone to SQL injection.
- Backdated GUI library is in use. It is challenging to be made pretty.

## Possible Improvements

- Switch to JavaFX.
- Implement over-the-internet play.
- Implement full-database encryption.
- Add salt to avoid matching password hashes.

## Contribute

Don't bother.