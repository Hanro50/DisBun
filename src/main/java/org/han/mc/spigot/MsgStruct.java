package org.han.mc.spigot;

import java.util.UUID;

import org.han.bot.RoleObj;
import org.han.xlib.FileObj;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class MsgStruct {
	public String Discordname;
	public UUID PlayerID;
	public String Message;
	public RoleObj role;
	
	public MsgStruct(String Discordname,UUID PlayerID,String Message,RoleObj role) {
		this.Discordname = Discordname;
		this.PlayerID = PlayerID;
		this.Message = Message;
		this.role = role;
	}

	public MsgStruct(ByteArrayDataInput Read) {		
		this.Discordname =Read.readUTF();
		try {
		this.PlayerID = UUID.fromString(Read.readUTF());
		} catch (IllegalArgumentException e) {
			this.PlayerID = null;
		}
		this.Message = Read.readUTF();
		this.role = FileObj.fromjson(Read.readUTF(), RoleObj.class);
		
		
		//return FileObj.fromjson(IncodededMsg, MsgStruct.class);
	}
	
	//public static ByteArrayDataInput Encode(MsgStruct MsgStruct) {
		//ByteArrayDataInput = MsgStruct
		
		
		
		//return FileObj.tojson(MsgStruct);
	//}
	
	public ByteArrayDataOutput Encode(ByteArrayDataOutput Write) {		
		Write.writeUTF(Discordname!=null?Discordname:"");
		Write.writeUTF(PlayerID!=null?PlayerID.toString():"");
		Write.writeUTF(Message!=null?Message:"");
		Write.writeUTF(FileObj.tojson(role));
		return Write;
	}
	
}
