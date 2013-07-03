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

package de.sauriel.rasengan.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloadActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		DownloadDialog downloadDialog = new DownloadDialog();
		
		RasenganMainFrame.comicService.addObserver(downloadDialog);
		
		downloadDialog.setLocationRelativeTo(null);
		downloadDialog.setVisible(true);
		
		RasenganMainFrame.comicService.downloadComic(RasenganMainFrame.comic);
	}

}
