/*
 * Rasengan - Manga and Comic Downloader
 *    Copyright (C) 2013  Sauriel
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.sauriel.rasengan.services;

import java.net.URL;
import java.util.TreeMap;

public interface ComicService {
	
	// All Comic Services have to extend java.util.Observable;
	
	// Get a String of the first character (or first few characters) 
	// and return a TreeMap with the Comic Name as a Key and a URL as the Value
	public TreeMap<String, URL> getComicList(String selectedInitial);
	
	// Get a String, search for every Comic which has it in its name 
	// and return a TreeMap with the Comic Name as a Key and a URL as the Value
	public TreeMap<String, URL> searchComic(String searchString);
	
	// Get a String of a Comic Name and return a Comic Object with all needed informations
	public Comic getComic(String comicName);
	
	// Get a Comic Object and start downloading it
	public void downloadComic(Comic comic);
}
