Instructions for running the game:

1. Run the app. You need internet connection to log in. If the first time doesn't go smooth, please
give it another try.
2. If you have an account, use your email and password to login. Else,
you may use username: guest@gmail.com and password: guest123 to login
3. Once you are signed in, you will see a screen to either make a new game or load a saved game
5. First time users should select the new game option.
6. This will take you to a screen where you will press buttons to select if you are using
numbers or a picture,  and you will scroll on a numberpicker to select your game size.
After making these choices, click next. Notice that game would not start if next is clicked
before pressing any buttons.
7. If you select the pictures option, select the photo from the gallery
you wish to use as the game background.
8. The game will now begin. At the top left corner you will find the undo button. On the top
right you willl find the pause button. At the pause screen, you may set the number of moves to
Undo, or may quit to main menu. If you quit, the game will be automatically saved at that instance.
9. Click on a tile next to the empty one to move the tile. The longer the game goes and the more
moves you make the lower your score will be.
10. Once you finish the game, you will be taken to a screen which will say you finished the game,
display your score, and have a button to go to the leaderboard.
11. In the leaderboard, you can swipe to move left and right to see the leaderboard for varying
dimensions (2x2, 3x3, etc up to 10x10)  in ascending order.
12. The leaderboard will also display your current score if you navigate to the corresponding
dimension. For example, if you have just played the 4x4 game, you will see your current score
displayed in the bottom right on the 4x4 leaderboard page.
13. On the top right corner of the leaderboard, you will see a three dot icon. If you click on
it, you will have an option to either play a new game or sign out.

about saved game:
- A game would be saved if it is quit before finished
- All games are saved automatically every 10 seconds
- There are three game slots to store saved games, Once all three game slots are filled, the user
would be asked to delete games if he/she is trying to start a new game.
- All saved games can be accessed by clicking on the saved game button on the main page
- To play a saved game, simply click on the desired orange game slot with the saved game's name on it
- To delete a saved game, simply press the corresponding red button on the right side of the game
slot that said "delete". The corresponding game slot would then be deleted.

New features implmented:
-Picture tile mode
-More than three game sizes, from 2x2 to 10x10
-Online authentication process
-Online leaderboard, global and local
-Games saved online
-Decreasing score based on time and number of moves
-All games are solveable
-Tiles are animated
-User can set undo moves in the pause menu of game


Note:
Our entire app is hosted on the FireBase platform. This includes the save functions, leaderboards,
and of course the authentication. In addition, a local reference of the calls, boards and saved
games are also saved on the user device. If a device goes offline, the database method calls are
tracked in a stack and stored in a cache. This means data will not be sent if the app is restarted
while the user is offline, but should otherwise function as expected.
