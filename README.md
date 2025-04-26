## Project Overview

What you're looking at is an overengineered version of a simple and classical game: Tic Tac Toe.
A login/registration system is implemented to keep track of some relevant user data that might help the game feel more person while playing.
First, the user will be prompted to either register or login with their personal account(s). The login page here might feel a little out of place, but there is a good reason for it to be the way it is.
To avoid complication for this simple project with zero intricacies, there is no over-the-internet play. This means both players will have to be logged in simultaneously and play on a single process/window.
Upon registration, a predictable success (or failure if the email is already in use) message will be displayed. Upon success, the entry will be registered to a table in a MySQL database from which it will be accessed in the future for logging the user in.
In the case of login, the prompt will ask two users to log in at the same time. A game will only begin if both users are logged in successfully. Upon success on this operation, a new window will open, displaying some relevant user information on top and displaying the 3x3 grid for the Tic Tac Toe game below it.
From here, the player can simply proceed to play the game. The win count will be updated for the victorious player if it is not a draw.

## Features

- Login and registration system powered by MySQL integration.
- Keeps track of the win count for each player via their account.
- Offers an intuitive interface for playing the game via GUI.
- 1v1 between two humans.

## Shortcomings

- No encryption whatsoever. The user accounts are not secure.
- Prone to SQL injection.
- Outdated GUI library is in use, and no styling is done, the interface is not pretty.

## Contribute

Don't bother.