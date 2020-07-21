# DisBun
A Plugin for Bungeecord that allows you to connect a bungeecord server to a Discord server. </br> 

<h3>Setup Guid</h3> 

1) Install the Plugin  </br> 
2) Run the Bungeecord server once to generate the required config options.  </br> 
3) Goto plugins/DBcon/config.txt  </br> 
4) Look for "Bot Token:" and replace whatever is after the ":" with a Discord bot token  </br> 
(<a href="https://discordpy.readthedocs.io/en/latest/discord.html"> See how to get one here</a>)  </br> 
5) Reboot you Bungeecord server or type /dbreload. If you added a valid bot token in the last step. It will now generate a bot invite in the output console </br> 
6) Invite the bot to a server of your choosing and run "!Setup" on that server. </br> 
</br> 
If you're lost, use !help in the same discord server your bot is in. </br> 
</br> 
<h3>Permissions</h3>

1) DisBun.link ->Whether or no someone can manually link their account with /link in game </br> 
2) DisBun.unlink ->Whether or no someone can manually unlink their account with /unlink in game </br>
3) DisBun.reload ->(Should be admin only) Whether or not someone can run /DBreload and reload the whole plugin </br>

<h3>PlaceholderAPI integration</h3>

1) %DisBun_DiscordColour% ->The current colour of someone's name 
2) %DisBun_DiscordRole% ->The top most role someone has 
3) %DisBun_DiscordName% ->The linked Discord account nickname
