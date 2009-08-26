/**
 * 
 */
package de.danielsenff.radds.view.canvas;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import DDSUtil.BIUtil;
import DDSUtil.ImageOperations;
import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.models.ColorChannel;
import de.danielsenff.radds.util.ResourceLoader;
import de.danielsenff.radds.view.JCPanel;

/**
 * Control-Panel for the {@link BICanvas}. 
 * @author Daniel Senff
 *
 */
public class CanvasControlsPanel extends JCPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3204480115770320549L;
	private BICanvas canvas;
	private JComboBox zoomCombo;

	/**
	 * @param controller
	 */
	public CanvasControlsPanel(final Application controller) {
		super(controller);
		
		setLayout(new BorderLayout());
		final JPanel navigateCanvas = initNavigationPanel();
		
		final JScrollPane scrollViewPane = initScrollCanvas(controller);
		
		this.add(scrollViewPane, BorderLayout.CENTER);
		this.add(navigateCanvas, BorderLayout.SOUTH);
	}

	private JPanel initNavigationPanel() {
		final JPanel panel = new JPanel();
		final JComboBox channelCombo = new JComboBox(composeColorChannelModel());
		channelCombo.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent event) {
				final JComboBox channelCombo = (JComboBox) event.getSource();
				final ColorChannel channel = (ColorChannel) channelCombo.getSelectedItem();
				canvas.setChannelMode(channel.getChannel());
			}
			
		});
		
		
		final JLabel lblChannelCombo = new JLabel(bundle.getString("Channels")+":");
		
		panel.add(lblChannelCombo);
		panel.add(channelCombo);
		
		/*
		 * unused combo for selecting zoom
		 * final String[] defaultZooms = { "25", "50", "100", "150", "200", "400"};
		zoomCombo = new JComboBox(defaultZooms);
		zoomCombo.setSelectedIndex(2);
		zoomCombo.setEditable(true);
		zoomCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				canvas.setZoomFactor( (Float.valueOf((String) zoomCombo.getSelectedItem())) / 100);
				canvas.repaint();
			}
		});*/
		
		final JLabel lblZoomCombo = new JLabel(bundle.getString("Zoom")+":");
		
		final JSlider zoomSlider = new JSlider(10, 500, 100);
        Hashtable<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        labels.put(new Integer(10), new JLabel("0.1x"));
        labels.put(new Integer(100), new JLabel("1x"));
        labels.put(new Integer(250), new JLabel("2.5x"));
        labels.put(new Integer(500), new JLabel("5x"));
        zoomSlider.setLabelTable(labels);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true);
		zoomSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(final ChangeEvent e) {
				final int zoomValue = ((JSlider)e.getSource()).getValue();
				canvas.setZoomFactor( (Float.valueOf(zoomValue)) / 100);
				canvas.repaint();
			}
			
		});
		
		panel.add(lblZoomCombo);
		panel.add(zoomSlider);
		
		//TODO fit to width/optimal
		
		
		
		return panel;
	}
	
	
	/**
	 * Init ComboBox for selecting different Color Channels of an Image.
	 * @return
	 */
	private DefaultComboBoxModel composeColorChannelModel() {
		final DefaultComboBoxModel combo = new DefaultComboBoxModel();
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.RGB, bundle.getString("rgb_channels")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.RED, bundle.getString("r_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.GREEN, bundle.getString("g_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.BLUE, bundle.getString("b_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.ALPHA, bundle.getString("a_channel")));
		return combo;
	}
	

	private JScrollPane initScrollCanvas(final Application controller) {
		
		final ImageIcon defaultImage = ResourceLoader.getResourceIcon("/de/danielsenff/radds/resources/defaultimage.png");
		
		canvas = new BICanvas(controller, 
				BIUtil.convertImageToBufferedImage(defaultImage.getImage(), 
						BufferedImage.TYPE_4BYTE_ABGR));
		final JScrollPane scrollViewPane = new JScrollPane(canvas);
		scrollViewPane.setPreferredSize(new Dimension(700,300));
		final ScrollCanvasListener scrollCanvasListener = new ScrollCanvasListener(scrollViewPane, zoomCombo);
		canvas.addMouseMotionListener(scrollCanvasListener);
		canvas.addMouseWheelListener(scrollCanvasListener);
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		canvas.addAncestorListener(new AncestorListener() {

			public void ancestorAdded(final AncestorEvent arg0) {}

			public void ancestorMoved(final AncestorEvent arg0) {
//				canvas.repaint();
				/*Rectangle bounds = canvas.getBounds();
				scrollcanvas.repaint(new Rectangle(bounds.x+bounds.width, 80));*/
				scrollViewPane.repaint();
			}

			public void ancestorRemoved(final AncestorEvent arg0) {	}
		});
		return scrollViewPane;
	}

	/**
	 * Returns the BufferedImage drawing canvas
	 * @return
	 */
	public BICanvas getCanvas() {
		return this.canvas;
	}

}