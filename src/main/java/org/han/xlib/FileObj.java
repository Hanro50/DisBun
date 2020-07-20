package org.han.xlib;

/*
 * Copyright 2020 Hanro
 * 
 * All subsequent copies of this code should contain this header in addition to any other license information.
 * 
 * If no other license information is provided by original author then:
 * All rights reserved by original author
 */
import static org.han.xlib.Debug.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * org.han.xlib File handler
 * 
 * @author hanro50
 * @version 3.0
 */
public class FileObj {
	/**
	 * The ClassPath, it's a legacy left over from before I knew what it meant and
	 * now I'm to lazy to change the name. Useful for when you want to store
	 * everything in a neat sub-directory and most methods will reference it in some
	 * way. This is likely to change in a future release of this library
	 */
	public static String ClassPath;
	/**
	 * The Path to the folder the jar file is located in.
	 */
	public static String Root;

	/**
	 * Overrides the {@link #Init() Normal Init} Process for this class
	 * 
	 * @param Pfile
	 */

	public static void Init(File Pfile) {
		Root = Pfile.getName();
		ClassPath = Pfile.getName() + "/DBcon/";
		FileChk("");

	}

	/**
	 * The Default Init behavior for this class. This should be called at the
	 * beginning of everything.
	 */

	public static void Init() {
		String file = "";
		if (System.getProperty("os.name").contains("Window")) {
			file = System.getProperty("user.dir") + "/";
		} else {
			if (!System.getProperty("os.name").contains("Linux")) {
				out("Unsupported OS -> Assuming Linux config");
			}

			file = System.getProperty("java.class.path").split(System.getProperty("path.separator"))[0];

			String[] path = file.split("/");
			file = path[0];
			for (int i = 1; i < path.length - 1; i++) {
				file = file + "/" + path[i];
			}
		}
		Root = file + "/";
		ClassPath = Root + "Data/";
		FileChk("");
	}

	/**
	 * Gets called to make sure the files being saved are save to store.
	 * 
	 * @param raw The raw name string
	 * @return a wrapped version of the previously mentioned name string
	 */
	public static String SafeStr(String raw) {
		return raw.trim().replaceAll("\\.", "").replaceAll("[^a-zA-Z0-9\\._]+", "_").toLowerCase();
	}

	/**
	 * Checks if a file exists within the {@link #ClassPath ClassPath} folder. </br>
	 * 
	 * @see {@link #FileChkroot(String) FileChkroot(String Path)} for a version of
	 *      this that doesn't use the path stored in {@link #FileChkroot(String)
	 *      FileChkroot(String Path)}
	 * @param Path The Path of the file you want to check relative to
	 *             {@link #ClassPath the path given here}
	 * 
	 * @implNote Doesn't check more then one level. If you want to check "F/C/".
	 *           Check F/ and then F/C/. This is mostly just here to make discourage
	 *           deeper directory listings then that which is required
	 * 
	 */
	public static void FileChk(String Path) {
		FileChkroot(ClassPath + Path);
	}

	/**
	 * Checks if a file exists within the {@link #Root Root} folder. </br>
	 * 
	 * @see {@link #FileChkroot(String) FileChkroot(String Path)} for a version of
	 *      this that uses the path stored in {@link #Root Root} folder (That
	 *      contains the jar file)
	 * @param Path The Path of the file you want to check relative to {@link #Root
	 *             the path given here}
	 * 
	 * @implNote Doesn't check more then one level. If you want to check "F/C/".
	 *           Check F/ and then F/C/. This is mostly just here to make discourage
	 *           deeper directory listings then that which is required
	 */
	public static void FileChkroot(String Path) {
		try {
			File F = new File(Path);
			if (!F.exists())
				F.mkdir();
		} catch (Throwable e) {
			Trace(e);
		}
	}

	/**
	 * Fetches a file for you from the path specified in {@link #ClassPath
	 * ClassPath}.
	 * 
	 * @param Path      The path to the file relative to {@link #ClassPath
	 *                  ClassPath}. Please run the required {@link #FileChk(String)
	 *                  File checks} on the path provided. Make sure the Path ends
	 *                  on "/"
	 * @param Name      The name of the file in question. It is ran thru
	 *                  {@link #SafeStr(String) this}
	 * @param extention The file extension of the file in question. It is ran thru
	 *                  {@link #SafeStr(String) this}. Do not add a dot, it's not
	 *                  needed
	 * @return Returns a normal java File variable
	 */
	static public File Fetch(String Path, String Name, String extention) {
		Name = SafeStr(Name);
		extention = SafeStr(extention);
		return new File(ClassPath + Path + Name + "." + extention);
	}

	/**
	 * Uses {@link #Fetch(String,String,String) the Fetch} function to get a file
	 * and then it deletes said file
	 * 
	 * @param Path      The path to the file relative to {@link #ClassPath
	 *                  ClassPath}. Please run the required {@link #FileChk(String)
	 *                  File checks} on the path provided. Make sure the Path ends
	 *                  on "/"
	 * @param Name      The name of the file in question. It is ran thru
	 *                  {@link #SafeStr(String) this}
	 * @param extention The file extension of the file in question. It is ran thru
	 *                  {@link #SafeStr(String) this}. Do not add a dot, it's not
	 *                  needed
	 * @return Whether the file was deleted successfully
	 * @throws FileNotFoundException If the file doesn't exist.
	 * @implNote This is a wrapper for the normal java File.delete() method.
	 */
	static public boolean erase(String Path, String Name, String extention) throws FileNotFoundException {
		File file = Fetch(Path, Name, extention);
		return erase(file);
	}

	/**
	 * his is a wrapper for the normal java File.delete() method.
	 * 
	 * @param The file that should be deleted
	 * @return Whether the file was deleted successfully
	 * @throws FileNotFoundException If the file doesn't exist.
	 */
	static public boolean erase(File file) throws FileNotFoundException {
		if (!file.exists())
			throw new FileNotFoundException();
		try {
			return file.delete();
		} catch (SecurityException e) {
			Trace(e);
			return false;
		}

	}

	/**
	 * Reads a file and produces a string array representing the text stored in that
	 * file. If you simple want a simple string use. Then use
	 * {@link #read(File,String) this} instead.
	 * 
	 * @param file The file you want to read
	 * @return Returns a String array of the file. Each line of the file represents
	 *         an entry into this array.
	 * @throws FileNotFoundException if the file doesn't exist
	 * @throws IOException           if the file couldn't be read
	 */
	static public String[] read(File file) throws FileNotFoundException, IOException {
		return read(file, " \u2029 ").split(" \u2029 ");
	}

	/**
	 * Reads a file and produces a simple string representing the text stored in
	 * that file. If you simple want an array instead. Then use {@link #read(File)
	 * this} instead.
	 * 
	 * @param file      The file you want to read
	 * @param Seperator Use "\n" as the separator to get a String version of the
	 *                  file's contents. This is what separates each line contained
	 *                  read from the file in question
	 * @return A string.
	 * @throws FileNotFoundException if the file doesn't exist
	 * @throws IOException           if the file couldn't be read
	 */
	static public String read(File file, String Seperator) throws FileNotFoundException, IOException {
		if (file.exists() && file.canRead()) {
			// rep("Reading: " + file.getPath());
			String T = "";
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String T2 = buffReader.readLine();
			while (T2 != null) {
				T = T + T2 + Seperator;
				T2 = buffReader.readLine();
			}
			buffReader.close();
			return T;
		} else if (!file.exists()) {
			err("Cannot find File " + file.getPath());
			throw new FileNotFoundException("Cannot find File " + file.getPath());
		} else {
			err("File is not readable");
			throw new IOException("File is not readable");
		}
	}

	/**
	 * For opening files saved using {@link #Save(Object,File) Save(Object,File)},
	 * {@link #SaveNF(Object,File) SaveNF(Object,File)} or
	 * {@link #Save(Object,File,boolean) Save(Object,File,Write lock)}
	 * 
	 * @param <T>      The base class of the object being retrieved form file. This
	 *                 is needed mostly because java needs to know (on a basic
	 *                 level) what it is trying to read from this saved class.
	 * @param file     The file variable that tells this function what to read from
	 * @param classOfT The base class of the object being retrieved form file. This
	 *                 is needed mostly because java needs to know (on a basic
	 *                 level) what it is trying to read from this saved class.
	 * @return An object of type T.
	 * @throws FileNotFoundException  The file variable provided doesn't point to
	 *                                anything
	 * @throws IOException            Failed to read the file provided by the file
	 *                                variable
	 * @throws ClassNotFoundException The class of the saved object could not be
	 *                                found. This might be due to the class in
	 *                                question being moved around in your project.
	 * 
	 * @implSpec <b> Warning </b> It is not recommended to use this function. Also
	 *           do not move around classes that this function handles. As it will
	 *           cause problems. This function is considered unsupported.
	 */
	static public <T> T Open(File file, Class<T> classOfT)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		String[] File = read(file);
		if (File.length < 2) {
			throw new IOException("Invalid file format : " + File.length);
		}
		Class<? extends T> classOfB = Class.forName(File[0]).asSubclass(classOfT);
		return fromjson(File[1], classOfB);
	}

	/**
	 * A write protected function. This function won't overwrite existing files. If
	 * you simple want to write files without worrying if they already exist. Check
	 * out {@link #SaveNF(Object,File) SaveNF(Object,File)}. To open this file once
	 * it is saved to disk. Use {@link #Open(File,Class) this}
	 * 
	 * @param Obj       The object you want to save
	 * @param Save_File The file you want to write to.
	 * @throws IOException Thrown if the file provided by the save file parameter
	 *                     couldn't be written to.
	 * @implSpec <b> Warning </b> It is not recommended to use this function. Also
	 *           do not move around classes that this function handles. As it will
	 *           cause problems. This function is considered unsupported.
	 */
	static public void Save(Object Obj, File Save_File) throws IOException {
		Save(Obj, Save_File, true);
	}

	/**
	 * This function will overwrite existing files. If you don't want to
	 * accidentally overwrite existing files. Check out {@link #Save(Object,File)
	 * Save(Object,File)}. To open this file once it is saved to disk. Use
	 * {@link #Open(File,Class) this}
	 * 
	 * @param Obj       The object you want to save
	 * @param Save_File The file you want to write to.
	 * @throws IOException Thrown if the file provided by the save file parameter
	 *                     couldn't be written to.
	 * @implSpec <b> Warning </b> It is not recommended to use this function. Also
	 *           do not move around classes that this function handles. As it will
	 *           cause problems. This function is considered unsupported.
	 */
	static public void SaveNF(Object Obj, File Save_File) throws IOException {
		Save(Obj, Save_File, false);
	}

	/**
	 * This function is the backend function for {@link #SaveNF(Object,File)
	 * SaveNF(Object,File)} and {@link #Save(Object,File) Save(Object,File)}.To open
	 * this file once it is saved to disk. Use {@link #Open(File,Class) this}
	 * 
	 * @param Obj       The object you want to save
	 * @param Save_File The file you want to write to.
	 * @param writelock Whether this should check if the file already exists before
	 *                  writing it to disk
	 * @throws IOException Thrown if the file provided by the save file parameter
	 *                     couldn't be written to.
	 * @implSpec <b> Warning </b> It is not recommended to use this function. Also
	 *           do not move around classes that this function handles. As it will
	 *           cause problems. This function is considered unsupported.
	 */
	static public void Save(Object Obj, File Save_File, boolean writelock) throws IOException {
		String[] ObjF = { Obj.getClass().getName(), tojson(Obj) };
		String[] Past = null;
		try {
			Past = read(Save_File);
		} catch (IOException e) {
		}

		if (Past == null || Past.length != 2 || !(ObjF[0].equals(Past[0]) && ObjF[1].equals(Past[1])))
			write(ObjF, Save_File, ObjF.getClass().getName(), writelock);
	}

	/**
	 * A write protected function. This function won't overwrite existing files. If
	 * you simple want to write files without worrying if they already exist. Check
	 * out {@link #writeNF(String[],File,String) writeNF(String[],File,String)}
	 * 
	 * @param In      The String array you want to write to the file. Each entry
	 *                will be put on a separate line
	 * @param file    The file you want to write to.
	 * @param objType This could be anything. It's just use for error logging
	 * @throws IOException Thrown if the file provided by the save file parameter
	 *                     couldn't be written to.
	 */
	static public void write(String[] In, File file, String objType) throws IOException {
		write(In, file, objType, true);
	}

	/**
	 * This function will overwrite existing files. If you don't want to
	 * accidentally overwrite existing files. Check out
	 * {@link #write(String[],File,String) write(String[],File,String)}.
	 * 
	 * @param In      The String array you want to write to the file. Each entry
	 *                will be put on a separate line
	 * @param file    The file you want to write to.
	 * @param objType This could be anything. It's just use for error logging
	 * @throws IOException Thrown if the file provided by the save file parameter
	 *                     couldn't be written to.
	 */
	static public void writeNF(String[] In, File file, String objType) throws IOException {
		write(In, file, objType, false);
	}

	/**
	 * This function is the backend function for {@link #WriteNF(Object,File)
	 * WriteNF(Object,File)} and {@link #Write(Object,File) Write(Object,File)}
	 * 
	 * @param In        The String array you want to write to the file. Each entry
	 *                  will be put on a separate line
	 * @param file      The file you want to write to.
	 * @param objType   This could be anything. It's just use for error logging
	 * @param writelock Whether this should check if the file already exists before
	 *                  writing it to disk
	 * @throws IOException Thrown if the file provided by the save file parameter
	 *                     couldn't be written to.
	 */
	static public void write(String[] In, File file, String objType, boolean writelock) throws IOException {

		if ((file.exists()) && (writelock)) {
			throw new IOException(objType + " already exists");
		}
		rep("Writing: " + file.getPath() + '\n');

		if (!file.getParentFile().exists())
			file.getParentFile().mkdir();
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new IOException("Could not save " + objType);
			}
		}

		String T = "";
		for (int i = 0; i < In.length; i++) {
			T = T + In[i] + "\n";
		}

		FileWriter writer = new FileWriter(file);

		writer.write(T);
		writer.close();

	}

	static private Gson builder() {
		GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		return builder.create();
	}

	/**
	 * A wrapper for Google's Gson library. This will convert a json String back
	 * into an object of type T. See {@link #tojson(Object) tojson(Object)} to
	 * convert an object back into a string
	 * 
	 * @param <T>      The exact class the given json string reverts back to.
	 * @param json     The json string mentioned earlier.
	 * @param classOfT The exact class the given json string reverts back to.
	 * @return A class of type T.
	 */
	static public <T> T fromjson(String json, Class<T> classOfT) {
		T R = builder().fromJson(json, classOfT);
		return R;
	}

	/**
	 * A wrapper for Google's Gson library. Converts a given object into a json
	 * string. To reverse this check out {@link #fromjson(String, Class) fromjson(String, Class)}
	 * 
	 * @param raw The object you want to convert
	 * @return The string the object reverts into
	 */
	static public String tojson(Object raw) {
		String s = builder().toJson(raw);
		return s;
	}

	static public File[] FileList(String path, String extention) {
		final String ext = "." + SafeStr(extention);
		File F = new File(ClassPath + path);
		FileFilter Ff = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(ext);
			}
		};

		return F.listFiles(Ff);
	}

	static public String[] FileListString(String path, String extention) throws FileNotFoundException {
		extention = SafeStr(extention);
		File[] FL = FileList(path, extention);
		if (FL == null || FL.length == 0) {
			throw new FileNotFoundException("No files in directory");
		}
		String[] R = new String[FL.length];

		for (int i = 0; i < FL.length; i++) {
			String T = FL[i].getName();
			int WithoutExtLen = T.length() - extention.length() - 1;
			R[i] = T.substring(0, WithoutExtLen);
		}
		return R;
	}

	static public String[] FileListString(String path) {

		File[] FL = FileList(path);
		if (FL == null || FL.length == 0) {
			return new String[] { "No files found" };
		}
		String[] R = new String[FL.length];

		for (int i = 0; i < FL.length; i++) {
			String T = FL[i].getName();

			R[i] = T;
		}
		return R;
	}

	static public File[] FileList(String path) {
		File F = new File(ClassPath + path);
		FileFilter Ff = new FileFilter() {
			public boolean accept(File f) {
				return true;
			}
		};

		return F.listFiles(Ff);
	}
}
