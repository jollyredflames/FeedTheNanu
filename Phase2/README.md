Instructions for running the game:


1. Run the app. You need an internet connection to log in. If the first time doesn't go smooth, please give it another try.
2. If you have an account, use your email and password to login. Otherwise, you may create a new account by signing up.
3. Once you are signed in, you will see a screen where you can choose 3 games to play. Click on any of the game buttons.
4. You will see a screen to either make a new game or load a saved game, in the context of the game that you chose previously.
5. Press new game.


About Feed The Nanu:
In the Feed The Nanu game, there would be food and spiders constantly falling from the top of the screen. The name of the frog like creature sitting on the bottom of the screen is Nanu. Users can drag Nanu left or right to move the creature such that he can catch the food. Nanu is very weak and needs to be fed constantly, the health bar displayed on the top left corner of the screen will show Nanu's health decreasing slowly as time passes. When Nanu eats any kind of food, his health would increase. When a spider is eaten, health would decrease. In addition to that, if Nanu has a coffee, the chewing speed of the nanu becomes faster. As a side note, the Nanu creature is taken from a game called "Cut The Rope", and the background and all of the edible items except for the coffee are all from Cut The Rope. There is a git commit message that describes this process, and gives credit to the textures creators.


About Memory Matrix:
There are two versions of the memory matrix game. One easy version, and one hard version. In the easy memory matrix game, some tiles will turn yellow at the beginning of each round, then they will change to the color of a normal tile. To pass the level, the user would have to tap on the tile that was once yellow. As the level increase, the number of tiles position for the user to memorize would increase, and the time of the tiles showing yellow at the beginning of each round would decrease.  The hard version of the memory matrix game is similar to the easy one, except this time, all the tile would be moving, and the users would be asked to tap on the correct moving tile to pass the level.


About Saving and Loading:
- All games are autosaved as you play.
- Each game can have up to 3 slots saved.
- If you try to start a new game when all slots are full, you will be asked to delete some, and will be taken to the saved games to do so.
- You can tap on Saved Games and select a game to load. You can also delete any of your saved games while you are there.
- To play a saved game, simply click on the desired orange game slot with the saved game's name on it.
- To delete a saved game, simply press the corresponding red button on the right side of the game slot that said "delete". The corresponding game slot would then be deleted.
- When you complete a game, or lose a game, you will be taken to finished screen, and the save for that game will be deleted, since the game is now over.



About Leaderboards:
Once a game is finished, there would be an option to go to leader board of the finished game. For games like sliding tiles and memory matrix where games have different version, simply swipe right or left to view leader boards of different versions of the game. A menu would show up if user click on the top right corner of the leader board, allowing users to go to leader boards of different games or to start a new game. The leader board would first show user to public leaderboard, allowing users to compare their score to others, since only the top ten score would be display on the public leaderboard, users might not have chance to see their own score on it. However, there is a private leaderboard available for the users. users can access the private leaderboard by clicking the button on the bottom of the screen which said View your leaderboard.



About Sliding Tiles:
Sliding tiles games are solvable. It allows users to play the game in different dimensions, all the way from 2 x 2 to 10 x 10. In addition to that sliding tile would ask user to set their game background, so users can customize their game background using an image in their phones' gallery. Users win the games by putting all the numbers in order or to make a fine image by moving the tiles.

1. After selecting to play a Sliding Tiles game, you'll see a screen where you will press buttons to select if you are using
numbers or a picture,  and you will scroll on a numberpicker to select your game size. After making these choices, click next. Notice that game would not start if next is clicked
before pressing any buttons.
2. If you select the pictures option, select the photo from the gallery
you wish to use as the game background.
3. The game will now begin. At the top left corner you will find the undo button. On the top
right you will find the pause button. At the pause screen, you may set the number of moves to
Undo, or may quit to main menu. If you quit, the game will be automatically saved at that instance.
4. Click on a tile next to the empty one to move the tile. The longer the game goes and the more
moves you make the lower your score will be.


New features implemented:
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
