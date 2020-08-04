# DisBun
A Plugin for Bungeecord that allows you to connect a Bungeecord server to a Discord server. </br> 

<h3>Setup Guide</h3> 

1) Install the Plugin  </br> 
2) Run the Bungeecord server once to generate the required config options.  </br> 
3) Goto plugins/DBcon/config.txt  </br> 
4) Look for "Bot Token:" and replace whatever is after the ":" with a Discord bot token  </br> 
(<a href="https://discordpy.readthedocs.io/en/latest/discord.html"> See how to get one here</a>)  </br> 
5) Reboot your Bungeecord server or type /dbreload. If you added a valid bot token in the last step. It will now generate a bot invite in the output console </br> 
6) Invite the bot to a server of your choosing and run "!Setup" on that server. </br> 

<h3>Permissions</h3>

1) DisBun.link ->Whether or not someone can manually link their account with /link in game </br> 
2) DisBun.unlink ->Whether or not someone can manually unlink their account with /unlink in game </br>
3) DisBun.reload ->(Should be admin only) Whether or not someone can run /DBreload and reload the whole plugin </br>

<h3>PlaceholderAPI integration</h3>

1) %DisBun_DiscordColour% ->The current colour of someone's name 
2) %DisBun_DiscordRole% ->The top most role someone has 
3) %DisBun_DiscordName% ->The linked Discord account nickname

<h3>The method changer</h3>
This plugin now provides a method to edit the "Activity" your bot is performing. On first boot a text file called "Methodfile will be generated". This almost a direct pass through to the section of the discord bot API that handles the "Activity" text for the discord bot this plugin will start. </br>
This can be toggled off in the config file like every other module.</br>

<h3>If you're lost</h3> 
Use !help in the same discord server your bot is in. </br> 
