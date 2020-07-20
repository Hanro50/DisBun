package org.han.mc.bungee;

import java.util.UUID;

import org.han.bot.RoleObj;
import org.han.link.TextMsg;
import org.han.mc.spigot.MsgStruct;
import org.han.xlib.Debug;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeIN implements TextMsg {

	@Override
	public void send(long ChannelID, String ServName, String player, String DiscordID, UUID UUID, String Message, RoleObj roleobj) {
		// TODO Auto-generated method stub
		if (BPlugin.getservinfo().containsKey(ServName)) {
		
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(TextMsg.SubChannel);
			out = (new MsgStruct(player,UUID,Message,roleobj)).Encode(out);
	
			BPlugin.getservinfo().get(ServName).sendData(TextMsg.Channel, out.toByteArray());
		} else {
			Debug.wrn("server \"" + ServName + "\" doesn't seem to exist");
		}
	}
}
/*try {
player = BPlugin.Self.getProxy().getPlayer(UUID).getDisplayName();
FileObj.FileChk("PData/");
FileObj.writeNF(new String[] {player}, FileObj.Fetch("PData/", UUID.toString(), "json"),
		"playerdata");
} catch (NullPointerException e) {
try {
	player = FileObj.read(FileObj.Fetch("PData/", UUID.toString(), "json"), "");
} catch (IOException | NullPointerException e1) {
	Debug.wrn("(" + player + ") not linked, maybe they've not logged in before?");
}
} catch (IOException e) {
}
*/

//out.writeUTF(Message);
//Debug.rep("Sending: [Discord] " + player + ":" + Message);