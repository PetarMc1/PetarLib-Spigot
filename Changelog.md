# v0.4.0 
#### Each commit in this version was published to maven as `0.4.0-SNAPSHOT` and the final version was published as `0.4.0`. Javadocs are only avalilable for the final version.
- Added /petarlib send <type> <player> <message> command to send a message to a player (through action bar or chat).
- Added petarlib.send permission to allow players to use the /petarlib send command.
- Added `use-minimessage` config option to use MiniMessage format in the /petarlib send command. If its enabled legacy color codes wont work.
- Most of the messages sent by the plugin now can be modified in the `messages.yml` file.
- Added support for placeholders in the messages sent by the plugin. You can use placeholders like {version} to display the plugin version in the messages.
- Added TabCompleter for the /petarlib send command to suggest players and message types.
- Added a class that checks if the server is running on Paper or Spigot. 
- Fixed CraftBukkit/Spigot support. Libs are now separately built for Spigot and Paper, so the Spigot one has Adventure API included and the Paper one doesnt as its natively included.
- Added support for sending messages to all online players through the /petarlib send command.
- Added CooldownManager to manage item cooldowns. Cooldowns are set using a cooldownId.
- Unify legacy and adventure type notifications