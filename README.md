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

<h3>Advancement module in version of Minecraft older then 1.12</h3>
Advancement's did not exist before 1.12. The Advancements module will only work for Spigot servers that run a version of Minecraft that is newer then 1.12.
If you're running a version of Spigot older then 1.12, please ignore the error generated by the fact advancements do not exist in 1.11 and older. As to why I cannot simple implement similar code for achievements as what I currently have for advancements for these older versions of the game. The 1.16 version of the Spigot API got rid of the achievement method. There is thus no easy method to implement achievements.</br>

<h3>Discord role colour in versions of Minecraft older then 1.16</h3>
This relies on a new feature in 1.16 to function properly. That being said, nothing stops you from using it anyway.</br>

<h3>Death Messages (0.0.10 update) </h3>
The new update will fix some oversights with regards to explosives in the Death Message module. In 0.0.9 Creepers and TNT are handled incorrectly. However, it appears there are some API anomalies with regards to handling TNT in versions of Minecraft older then 1.12. This is a known issue and doesn't appear fixable.   

<h3>Translation settings</h3>
<h4>Pre 0.0.10</h4>
You are free to use any minecraft language file with this plugin. Simple put the language json file in the DisBun file within your "plugins/DisBun" file and edit the plugin config accordingly. 
<h4>Post 0.0.10</h4>
You are free to use any minecraft language file with this plugin. Simple put the language json file in the DisBun file within your "plugins/DisBun/lang" file and edit the plugin config accordingly. 

<h3>Customizable command text(WIP)</h3>
By customizing "plugins/DisBun/discord_config.txt". You can now, to a degree, customize the command and help text a set discord command uses (Please avoid adding spaces). Certain commands also have further customizable text that's accessible via the "plugins/DisBun/discord_config.txt" text file.

<h3>If you're lost</h3> 
Use !help in the same discord server your bot is in. </br> 
