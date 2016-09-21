
/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of Jorge Miguel Ferreira da Silva.
 *
 *  WrapperAnon is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WrapperAnon is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
*/
package pt.ieeta.anonymouspatientdata.wrapper;
/**
 * @author Jorge Miguel Ferreira da Silva
 */

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;



public class WrapperAnon implements AnonimizerWrapper {


	private static WrapperAnon Instance;
	//TODO See better approach to this method

	private String Pth = ("C:\\Users\\Miguel\\Desktop\\Dicoogle-EXE");
	private static Path PluginsPath;
	boolean PluginsExist;
	boolean JarFilesExist;
	ArrayList<File> fList= new ArrayList<File>();

	private WrapperAnon() {
	}

	public static WrapperAnon getInstance(){
		if (Instance==null)
			Instance= new WrapperAnon();
		return Instance;		
	}

	public String getcurrentpath() {
		String CurrentPath=Paths.get(".").toAbsolutePath().normalize().toString();
		System.out.println(CurrentPath);
		return CurrentPath;
	}

	public boolean VerifyFolder(){
		PluginsPath = Paths.get(Pth + "//Plugins"); 
		if ( !Files.exists(PluginsPath)) 
		{System.out.println("The Folder doesn't Exist: " + PluginsPath);
		PluginsExist=false;
		return PluginsExist;}
		else {System.out.println("The Folder Plugins Exists in: " + PluginsPath);
		PluginsExist=Files.isDirectory(PluginsPath);
		return PluginsExist; }
	}
	public boolean LoadPluginFiles() {
		if(PluginsExist){ 

			File folder = PluginsPath.toFile();
			File[] fList = folder.listFiles(); 
			if (fList.length>0){
				for (File file : fList) {
					if (file.isFile()) {
						System.out.println(file.getAbsolutePath());
						this.fList.add(file);;
					} else if (file.isDirectory()) {
						System.out.println(file.getAbsolutePath());		
					}
				}
				System.out.println("The Folder List is: \n" + this.fList);
			}
		}
		if (this.fList.isEmpty())return JarFilesExist=false;
		else return JarFilesExist=true;
	}

	
	public void LoadPluginJars() {
		if (JarFilesExist){
			Iterator<File> iterator = fList.iterator();
			while (iterator.hasNext()) {
				InputStream input = getClass().getResourceAsStream(iterator.next().toString());
				System.out.println("hi: " + input);
			}		
		}
	}

public static void main(String [] args){
		WrapperAnon.getInstance().getcurrentpath();
		WrapperAnon.getInstance().VerifyFolder();
		WrapperAnon.getInstance().LoadPluginFiles();
		WrapperAnon.getInstance().LoadPluginJars();
	}

}








