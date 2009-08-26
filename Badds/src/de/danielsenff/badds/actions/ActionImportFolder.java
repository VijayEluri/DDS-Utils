/**
 * 
 */
package de.danielsenff.badds.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;

import javax.swing.JOptionPane;

import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.util.ResourceLoader;



/**
 * @author danielsenff
 *
 */
public class ActionImportFolder extends BasicAction {

	/**
	 * @param controller
	 */
	public ActionImportFolder(Application controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		try {
			File directory = controller.showImageFileChooser().openDirectoryDialogue();

			File[] files = directory.listFiles(new FileFilter(){

				public boolean accept(final File f) {
					// TODO this could be done recursively through the filesystem
					//return (f.isDirectory() || f.getName().toLowerCase().endsWith("." + "dds"));
					return ( f.getName().toLowerCase().endsWith("." + "dds"));
				}

			});
			if(files.length > 0) {
				controller.getFilesListModel().addFiles(files);
			} else {
				JOptionPane.showMessageDialog(controller.getView(), 
						"<html>The selected folder didn't include any DDSImages.</html>",	"No DDSImages found", 
						JOptionPane.INFORMATION_MESSAGE);
			}
			

		} catch (NullPointerException e1) {

		}
		
	}

}