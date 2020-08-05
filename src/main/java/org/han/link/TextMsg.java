package org.han.link;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.han.bot.RoleObj;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;
import org.han.xlib.types.DiMultiMap;

import com.google.gson.annotations.Expose;

import net.dv8tion.jda.api.entities.Guild;

public interface TextMsg {
	// Default stuff


	public static List<String> GetServ(long ChannelID) {
		List<String> T = new ArrayList<String>();
		T.add("error 404:server not found");
		return Container.get().Data.Getkey1default(ChannelID, T);
	}

	public static Set<Long> getchannels() {
		return Container.get().Data.GetKey2Map().keySet();
	}

	public static List<Long> GetChn(String ServName) {
		List<Long> T = new ArrayList<Long>();
		T.add(0L);
		return Container.get().Data.Getkey2default(ServName, T);
	}

	public static void SaveLink(long ChannelID, String ServName) {
		// Container C = (new Container());
		Container C = Container.get();
		C.Data.add(ServName, ChannelID);
		C.Save();
	}

	public static void remLink(long ChannelID, String ServName) {
		// Container C = (new Container());
		Container C = Container.get();
		C.Data.remove(ServName, ChannelID);
		C.Save();
	}

	public static void remChannel(long ChannelID) {
		// Container C = (new Container());
		Container C = Container.get();
		C.Data.removeKey2(ChannelID);
		C.Save();
	}

	public default void sendfromJDA(long ChannelID, String player, String DiscordID, String Message, RoleObj roleobj) {
		for (String T : GetServ(ChannelID)) {
			send(ChannelID, T, player, DiscordID, LinkUp.GetUUID(DiscordID), Message, roleobj);
		}
	}

	public default void sendfromMC(String ServName, String player, UUID UUID, String Message) {
		String L = LinkUp.GetDiscordID(UUID);
		if (L != null) {
			try {
				Guild T = GetDetails.getGuild();
				player = T.retrieveMemberById(L).complete().getEffectiveName();
			} catch (IllegalStateException e) {
				Debug.wrn("Null value avoided in (sendfromMC) ");
			}
		}

		for (long T : GetChn(ServName)) {
			send(T, ServName, player, L, UUID, Message, null);
		}
	}

	// Thing that must be implemented

	public void send(long ChannelID, String ServName, String player, String DiscordID, UUID UUID, String Message,
			RoleObj roleobj);

	// Save File Generator
	public static class Container {
		@Expose
		DiMultiMap<String, Long> Data;
		static Container self;

		private Container() {
			Data = new DiMultiMap<String, Long>();
		}

		public static Container get() {
			if (self != null) {
				return self;
			}
			File F = FileObj.Fetch("", "Links", "json");

			if (F.exists()) {
				try {
					Container Temp = FileObj.fromjson(FileObj.read(F, ""), Container.class);
					return Temp;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Debug.Trace(e);
					Debug.out("Regenerating link file.");
				}
			}

			self = new Container();
			return self;
		}

		private void Save() {
			Debug.out("Saving file:  Link Settings");
			File F = FileObj.Fetch("", "Links", "json");

			try {
				FileObj.writeNF(new String[] { FileObj.tojson(this) }, F, "Link Settings");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Debug.Trace(e);
				Debug.out("Could not save link file");
			}

		}
	}

}
