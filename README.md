# ToxicReport
BungeeCord report plugin for german server

## Installation ##

Download the latest release [here](https://github.com/MaxPlays/ToxicReport/releases/latest) and drop it into you BungeeCord plugins folder. Start the BungeeCord server and let the plugin create the config.yml file. Stop the server again and set the MySQL credentials in the config.yml file to their corresponding values. Replace "example.com" by your server's domain. Start the server. Create a new folder in the root directory of your webserver's webfolder and call it "chatlog" (Without the quotation marks). Download all the files from the [web directory](https://github.com/MaxPlays/ToxicReport/tree/master/web) of this repository and put them in the folder you just created. Open the file config.php and set the MySQL credentials. Save the file and close it. Now, you should be able to run the plugin without any issues. Remember to install all the dependencies mentioned bellow without which the plugin won't work.

## Dependencies ##
- MySQL server
- Webserver (preferably Apache)
- PHP 5 and upwards
- BungeeCord and Bukkit/Spigot servers

## Commands and permissions ##
**/report** Get a list of all report IDs  
**/report Player** Get a list of all report IDs onto which you can click to instantly report the player  
**/report list** If you have the permission **ToxicReport.supporter**: List of all open reports. If you don't have it: List off all **your** open reports  
**/chatlog Player** If you have the permission **ToxicReport.supporter**: Create a chatlog of a player (Chatlogs are automatically created when a player gets reportet)  

### Permission ###
The permission to use some of the above-mentioned commands and to receive notifications about incoming reports is **ToxicReport.supporter**
