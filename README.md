![enter image description here](https://i.imgur.com/xpV7QDM.png)

[![Paetron](https://img.shields.io/badge/Patreon-subscribe-lightblue.svg)](https://www.patreon.com/GlareMasters) [![Build Status](https://ci.glaremasters.me/buildStatus/icon?job=PlayerTime)](https://ci.glaremasters.me/job/PlayerTime/) [![Spigot](https://img.shields.io/badge/Spigot-Project%20Page-orange.svg)](https://www.spigotmc.org/resources/58915/) [![Discord](https://img.shields.io/discord/272126301010264064.svg)](https://glaremasters.me/discord) [![Minecraft](https://img.shields.io/badge/Minecraft-1.8--1.12.2-red.svg)]()

# About 
PlayerTime is a simple little plugin I created that allows you to check how long a player has been on the server for. I know this may seem like "every other player time plugin" but I have yet to see one that will work with offline players, simply because you can't. That's why I decided to do my own take on this. I wanted to make that a possibility.
# Core Features

 - Supports Offline Players
 - Supports MySQL
 - Supports BungeeCord (via connecting to same MySQL DB on all servers)
 - PlaceholderAPI
 - Top 10 Leaderboard (GUI and Text based)
# PlaceholderAPI Placeholders
 - %playertime_time% - Returns a formatted version of the time 
 - %playertime_time_seconds% - Returns the time in seconds
 - %playertime_time_minutes% - Returns the time in minutes
 - %playertime_time_hours% - Returns the time in hours
# Commands
 - /ptcheck [player] - Without the argument, it will check the user who ran the command. If the argument is used it will use the name insterted into the argument
 - /pttop - Will display the plugin's top 10 leaderboard
 - /ptreload - Reloads the config
# Permissions
 - playertime.check (check yourself)
 - playertime.others (check others)
 - playertime.top (displays the leaderboard)
 - playertime.reload (reloads the config)
