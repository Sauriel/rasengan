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

import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.JProgressBar;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloadDialog extends JDialog {

	private static final long serialVersionUID = 4251447351437107605L;

	public DownloadDialog() {
		
		// Configure Dialog
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle("Downloading ...");
		setType(Type.UTILITY);
		setBounds(100, 100, 300, 100);
		setLayout(new BorderLayout(0, 0));
		
		// Set Content
		
		JLabel labelDownload = new JLabel("Downloading");
		add(labelDownload, BorderLayout.NORTH);
		
		JProgressBar progressBar = new JProgressBar();
		add(progressBar, BorderLayout.CENTER);
		
		JButton abortButton = new JButton("Abort");
		add(abortButton, BorderLayout.SOUTH);
		
		// Set Listener
		
		abortButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
	}

}
