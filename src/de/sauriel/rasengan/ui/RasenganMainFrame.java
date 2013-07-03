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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;

import de.sauriel.rasengan.services.Comic;
import de.sauriel.rasengan.services.ComicService;
import de.sauriel.rasengan.services.MangaReaderNetService;

public class RasenganMainFrame extends JFrame {

	private static final long serialVersionUID = -7183468013385609542L;
	
	private JPanel contentPane;
	private JTextField searchField;
	
	public static ComicService comicService = new MangaReaderNetService();
	
	private final String[] LETTERS = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	String selectedInitial = "#";
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	// Prevents double selection in the Comic List
	private int counter = 0;

	public RasenganMainFrame() {
		
		// Configure Frame
		
		setTitle("Rasengan - Manga and Comic Downloader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		// Set MenuBar
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Menu");
		menu.setMnemonic('M');
		menuBar.add(menu);
		
		JMenuItem menuStartDownload = new JMenuItem("Start Download");
		menuStartDownload.setMnemonic('D');
		menu.add(menuStartDownload);
		
		JMenuItem menuSettings = new JMenuItem("Settings");
		menuSettings.setMnemonic('S');
		menu.add(menuSettings);
		
		JMenuItem menuExit = new JMenuItem("Exit");
		menuExit.setMnemonic('E');
		menu.add(menuExit);
		
		// Set Content
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setDividerLocation(250);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new BorderLayout(0, 0));
		leftPanel.setMinimumSize(new Dimension(250, 600));
		
		JComboBox<String> initialSelector = new JComboBox<String>(LETTERS);
		leftPanel.add(initialSelector, BorderLayout.NORTH);
		
		searchField = new JTextField("Enter search text and press enter");
		leftPanel.add(searchField, BorderLayout.SOUTH);
		searchField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		
		TreeMap<String, URL> comicMap = comicService.getComicList(selectedInitial);
		ArrayList<String> list = new ArrayList<>();
		list.addAll(comicMap.keySet());
		for(String comic : list){
		    listModel.addElement(comic);
		}
		
		JList<String> comicList = new JList<>(listModel);
		scrollPane.setViewportView(comicList);
		
		JPanel rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new BorderLayout(0, 0));
		rightPanel.setMinimumSize(new Dimension(550, 600));
		
		JPanel rightBottomPanel = new JPanel();
		rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);
		rightBottomPanel.setLayout(new BorderLayout(0, 0));
		
		JButton downloadButton = new JButton("Download");
		rightBottomPanel.add(downloadButton, BorderLayout.EAST);
		
		// Set Listener
		
		menuExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		
		menuSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RasenganSettings settings = new RasenganSettings();
				settings.setLocationRelativeTo(null);
				settings.setVisible(true);
			}
			
		});
		
		menuStartDownload.addActionListener(new DownloadActionListener());
		
		downloadButton.addActionListener(new DownloadActionListener());
		
		initialSelector.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				JComboBox<String> selectedLetter = (JComboBox<String>) actionEvent.getSource();
				String selection = LETTERS[selectedLetter.getSelectedIndex()];
				selectedInitial = selection;

				ArrayList<String> newList = new ArrayList<>();
				newList.addAll(comicService.getComicList(selectedInitial).keySet());
				
				listModel.removeAllElements();
				for(String manga : newList){
				    listModel.addElement(manga);
				}
			}
			
		});
		
		comicList.addListSelectionListener(new ListSelectionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent listEvent) {
				if (counter == 1) {
					JList<String> newComicList = (JList<String>) listEvent.getSource();
					String comicName = newComicList.getSelectedValue();
					Comic comic = comicService.getComic(comicName);

					// TODO Show the Cover, let me download it
					System.out.println(comic.getName());
					
					counter = 0;
				} else {
					counter++;
				}
			}
			
		});
		
		searchField.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				searchField.setText("");
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// nothing to do here
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// nothing to do here
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// nothing to do here
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// nothing to do here
			}
			
		});
		searchField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				JTextField newSearchBox = (JTextField) actionEvent.getSource();
				String selection = newSearchBox.getText();
				selectedInitial = selection;

				ArrayList<String> newList = new ArrayList<>();
				newList.addAll(comicService.searchComic(selectedInitial).keySet());
				
				listModel.removeAllElements();
				for(String manga : newList){
				    listModel.addElement(manga);
				}
			}
			
		});
	}

}
