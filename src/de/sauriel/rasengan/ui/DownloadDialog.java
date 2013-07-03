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

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.util.Observable;
import java.util.Observer;

public class DownloadDialog extends JDialog implements Observer {

	private static final long serialVersionUID = 4251447351437107605L;
	
	JProgressBar progressBar;

	public DownloadDialog() {
		
		// Configure Dialog
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setModal(true);
		setModalityType(ModalityType.MODELESS);
		setResizable(false);
		setTitle("Downloading: " + RasenganMainFrame.comic.getName());
		setBounds(100, 100, 300, 60);
		setLayout(new BorderLayout(0, 0));
		
		// Set Content
		
		JLabel labelDownload = new JLabel("Downloading: " + RasenganMainFrame.comic.getName());
		add(labelDownload, BorderLayout.NORTH);
		
		progressBar = new JProgressBar();
		add(progressBar, BorderLayout.CENTER);
	}

	@Override
	public void update(Observable comicService, Object imagesCount) {
		int[] newImagesCount= (int[]) imagesCount;
		progressBar.setMaximum(newImagesCount[1]);
		progressBar.setValue(newImagesCount[0]);
		
		if (newImagesCount[0] == newImagesCount[1]) {
			dispose();
		}
	}

}
