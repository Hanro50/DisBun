package org.han.mc.bungee;

import java.util.UUID;

import org.han.common.RoleObj;
import org.han.link.Channels;
import org.han.link.TextMsg;
import org.han.spigot.MsgStruct;
import org.han.xlib.Debug;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeIN implements TextMsg {

	@Override
	public void send(long ChannelID, String ServName, String player, String DiscordID, UUID UUID, String Message, RoleObj roleobj) {
		// TODO Auto-generated method stub
		if (BPlugin.getservinfo().containsKey(ServName)) {
		
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(Channels.Out);
			out = (new MsgStruct(player,UUID,Message,roleobj)).Encode(out);
	
			BPlugin.getservinfo().get(ServName).sendData(Channels.Main, out.toByteArray());
		} else {
			Debug.wrn("server \"" + ServName + "\" doesn't seem to exist");
		}
	}
}
