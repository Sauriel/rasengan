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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MangaReaderNetService extends Observable implements ComicService {
	
	private final int TIMEOUT_WAIT = 30000;
	
	private TreeMap<String, URL> mangaMap = new TreeMap<>();
	private String mangaName;
	
	// Info for the Observer (42/100 completed)
	private int[] imagesCount = new int[2];
	
	public MangaReaderNetService() {
		try {
		Document htmlDoc = Jsoup.connect("http://www.mangareader.net/alphabetical").timeout(TIMEOUT_WAIT).get();
		Elements lists = htmlDoc.getElementsByTag("ul");
		
		for (int i = 4; i <= 39; i++) {
			Element list = lists.get(i);
			for (Element manga : list.getElementsByTag("a")) {
				URL link = new URL(manga.absUrl("href"));
				String name = manga.text();
				mangaMap.put(name, link);
			}
		}
		} catch (IOException ioE) {
			//TODO Treat the Exception. Yeah I'm a bit lazy
		}
		
	}

	@Override
	public TreeMap<String, URL> getComicList(String selectedInitial) {
		TreeMap<String, URL> newMangaMap = new TreeMap<>();
		
		// Get the Key/Value Pairs for all Letters
		if (selectedInitial != "#") {
			for (Entry<String, URL> entry : mangaMap.entrySet()) {
				if (entry.getKey().startsWith(selectedInitial)
						|| entry.getKey().startsWith(selectedInitial.toLowerCase())) {
					newMangaMap.put(entry.getKey(), entry.getValue());
				} else {
					newMangaMap.put("", null);
				}
			}
			
		// Get the Key/Value Pairs for special signs and numbers. mangareader.net has them seperated -.-
		} else {
			for (Entry<String, URL> entry : mangaMap.entrySet()) {
				if (entry.getKey().startsWith("#") || entry.getKey().startsWith("+") || entry.getKey().startsWith("0") || 
						entry.getKey().startsWith("1") || entry.getKey().startsWith("2") || entry.getKey().startsWith("3") || 
						entry.getKey().startsWith("4") || entry.getKey().startsWith("5") || entry.getKey().startsWith("6") || 
						entry.getKey().startsWith("7") || entry.getKey().startsWith("8") || entry.getKey().startsWith("9")) {
					newMangaMap.put(entry.getKey(), entry.getValue());
				} else {
					newMangaMap.put("", null);
				}
			}
		}
		
		return newMangaMap;
	}

	@Override
	public TreeMap<String, URL> searchComic(String searchString) {
		TreeMap<String, URL> searchMap = new TreeMap<>();
		for (Entry<String, URL> entry : mangaMap.entrySet()) {
			if (entry.getKey().toLowerCase().contains(searchString.toLowerCase())) {
				searchMap.put(entry.getKey(), entry.getValue());
			}
		}
		return searchMap;
	}

	@Override
	public Comic getComic(String mangaName) {
		Comic manga = null;
		BufferedImage cover = null;
		URL url = null;
		
		// Get the matching URL for the Manga Name
		for (Entry<String, URL> entry : mangaMap.entrySet()) {
			if (entry.getKey().equals(mangaName)) {
				url = entry.getValue();
			}
		}
		
		// Get the matching Cover
		try {
			Document doc = Jsoup.connect(url.toString()).timeout(TIMEOUT_WAIT).get();
			Element coverID = doc.getElementById("mangaimg").getElementsByTag("img").first();
			URL coverURL = new URL(coverID.absUrl("src"));
			cover = ImageIO.read(coverURL);
			
		} catch (IOException ioE) {
			//TODO Treat the Exception. Yeah I'm a bit lazy
		}

		manga = new Comic(mangaName, url, cover);
		
		return manga;
	}

	@Override
	public void downloadComic(Comic comic) {
		ArrayList<URL> chapterList = getChapterList(comic);
		
		imagesCount[1] = 0;
		
		for (URL chapter : chapterList) {
			try {
				Document doc = Jsoup.connect(chapter.toString()).timeout(30000).get();
				int imageCount = doc.getElementById("pageMenu").getElementsByTag("option").size();
				imagesCount[1] += imageCount;
			} catch (IOException e) {
				//TODO Treat the Exception. Yeah I'm a bit lazy
				e.printStackTrace();
			}
		}
		
		imagesCount[0] = 0;
		mangaName = comic.getName().replace('/', '-');
		for (final URL chapter : chapterList) {
			
			Thread thread = new Thread() {
				public void run() {
					downloadChapter(chapter);
				}
			};
			thread.start();
		}
	}

	private void downloadChapter(URL chapter) {
		try {
			Document doc = Jsoup.connect(chapter.toString()).timeout(TIMEOUT_WAIT).get();
			int imageCount = doc.getElementById("pageMenu").getElementsByTag("option").size();
			
			for (int i = 1; i <= imageCount; i++) {
				
				String chapterCount = chapter.toString().split("/")[chapter.toString().split("/").length-1];
				
				// Test if the Comics are stored in the old or in the new format
				String comparsion1 = chapter.toString().substring(chapter.toString().length() - 4).toLowerCase();
				String comparsion2 = "html";
				if (comparsion1.equals(comparsion2)) {
					chapterCount = chapterCount.substring(8, chapterCount.length()-5);
					downloadChapterMethod1(chapter, chapterCount, i);
				} else {
					downloadChapterMethod2(chapter, chapterCount, i);
				}
			}
			
		} catch (IOException e) {
			//TODO Treat the Exception. Yeah I'm a bit lazy
			e.printStackTrace();
		}
	}
	
	private void downloadChapterMethod1(URL chapter, String chapterCount, int i) throws IOException {
		
		// Modify the URL
		String newChapterURL = chapter.toString();
		String[] n1 = newChapterURL.split("/");
		newChapterURL = n1[0] + "//" + n1[2] + "/" + n1[3].substring(0, n1[3].length()-1) + i + "/" + n1[4] + "/" + n1[5];
		chapter = new URL(newChapterURL);

		Document imageDoc = Jsoup.connect(chapter.toString()).timeout(TIMEOUT_WAIT).get();
		Element imageElement = imageDoc.getElementById("imgholder").getElementsByTag("img").first();
		URL imageLink = new URL(imageElement.absUrl("src"));
		BufferedImage bi = ImageIO.read(imageLink);
		File pfad = new File(mangaName + "/chapter_" + chapterCount);
		pfad.mkdirs();
		
		ImageIO.write(bi, "jpg", new File(mangaName + "/chapter_" + chapterCount + "/page_" + i + ".jpg"));
		
		imagesCount[0]++;
		
		setChanged();
		notifyObservers(imagesCount);
		clearChanged();
	}
	
	private void downloadChapterMethod2(URL chapter, String chapterCount, int i) throws IOException {
		Document imageDoc = Jsoup.connect(chapter.toString() + "/" + i).timeout(TIMEOUT_WAIT).get();
		Element imageElement = imageDoc.getElementById("imgholder").getElementsByTag("img").first();
		URL imageLink = new URL(imageElement.absUrl("src"));
		BufferedImage bi = ImageIO.read(imageLink);
		File pfad = new File(mangaName + "/chapter_" + chapterCount);
		pfad.mkdirs();
		
		ImageIO.write(bi, "jpg", new File(mangaName + "/chapter_" + chapterCount + "/page_" + i + ".jpg"));
		
		imagesCount[0]++;
		
		setChanged();
		notifyObservers(imagesCount);
		clearChanged();
	}

	private ArrayList<URL> getChapterList(Comic manga) {
		ArrayList<URL> chapterList = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(manga.getURL().toString()).timeout(TIMEOUT_WAIT).get();
			Elements chapters = doc.getElementById("chapterlist").getElementsByTag("a");
			for (Element chapter : chapters) {
				chapterList.add(new URL(chapter.absUrl("href")));
			}
		} catch (IOException e) {
			//TODO Treat the Exception. Yeah I'm a bit lazy
			e.printStackTrace();
		}
		return chapterList;
	}

}
