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

import java.awt.image.BufferedImage;
import java.net.URL;

public class Comic {
	
	private String comicName;
	private URL url;
	private BufferedImage cover;
	
	public Comic(String comicName, URL url, BufferedImage cover) {
		this.comicName = comicName;
		this.url = url;
		this.cover = cover;
	}
	
	// Getter
	
	public URL getURL() {
		return url;
	}

	public BufferedImage getCover() {
		return cover;
	}
	
	public String getName() {
		return comicName;
	}

}
