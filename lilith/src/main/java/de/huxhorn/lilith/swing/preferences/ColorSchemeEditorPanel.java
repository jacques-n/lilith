/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2011 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.huxhorn.lilith.swing.preferences;

import de.huxhorn.lilith.swing.preferences.table.ConditionPreviewRenderer;
import de.huxhorn.lilith.swing.table.ColorScheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorSchemeEditorPanel
	extends JPanel
{
	private final Logger logger = LoggerFactory.getLogger(ColorSchemeEditorPanel.class);

	private ColorChangeListener colorChangeListener;
	private ConditionPreviewRenderer previewDummyRenderer;
	private ColorScheme colorScheme;
	private ColorChooserPanel textChooserPanel;
	private ColorChooserPanel backgroundChooserPanel;
	private ColorChooserPanel borderChooserPanel;

	public ColorSchemeEditorPanel()
	{
		createUI();
	}

	private void createUI()
	{
		colorChangeListener = new ColorChangeListener();
		textChooserPanel = new ColorChooserPanel(Color.BLACK);
		backgroundChooserPanel = new ColorChooserPanel(Color.WHITE);
		borderChooserPanel = new ColorChooserPanel(Color.WHITE);

		attachChangeListener(textChooserPanel.getColorChooser());
		attachChangeListener(backgroundChooserPanel.getColorChooser());
		attachChangeListener(borderChooserPanel.getColorChooser());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("Text", textChooserPanel);
		tabbedPane.add("Background", backgroundChooserPanel);
		tabbedPane.add("Border", borderChooserPanel);

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		add(tabbedPane, gbc);
		previewDummyRenderer = new ConditionPreviewRenderer();
		Component previewComponent = previewDummyRenderer.getTableCellRendererComponent(null, null, false, false, 0, 0);
		JPanel previewPanel = new JPanel(new GridLayout(1, 1));
		previewPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Preview"));
		previewPanel.add(previewComponent);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(previewPanel, gbc);
	}

	/**
	 * Saves the colors of the editors into the colorScheme property.
	 */
	public void saveColors()
	{
		this.colorScheme = new ColorScheme(textChooserPanel.getColor(), backgroundChooserPanel.getColor(), borderChooserPanel.getColor());
	}

	/**
	 * Initializes the editors with the colorScheme values.
	 */
	public void resetColors()
	{
		if(colorScheme == null)
		{
			colorScheme = new ColorScheme().initDefaults();
		}

		textChooserPanel.setColor(colorScheme.getTextColor());
		backgroundChooserPanel.setColor(colorScheme.getBackgroundColor());
		borderChooserPanel.setColor(colorScheme.getBorderColor());
	}

	public ColorScheme getColorScheme()
	{
		return colorScheme;
	}


	public void setColorScheme(ColorScheme colorScheme)
	{
		if(colorScheme == null)
		{
			throw new IllegalArgumentException("colorScheme must not be null!");
		}
		this.colorScheme = colorScheme;
		resetColors();
	}

	private void attachChangeListener(JColorChooser chooser)
	{
		AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
		if(panels != null)
		{
			for(AbstractColorChooserPanel current : panels)
			{
				current.getColorSelectionModel().addChangeListener(colorChangeListener);
			}
		}
		else
		{
			if(logger.isWarnEnabled()) logger.warn("No chooser panels!");
		}
	}

	private class ColorChangeListener
		implements ChangeListener
	{
		private SavedCondition dummyCondition;

		private ColorChangeListener()
		{
			dummyCondition = new SavedCondition();
			dummyCondition.setColorScheme(new ColorScheme().initDefaults());
		}

		public void stateChanged(ChangeEvent e)
		{
			// update preview component...
			ColorScheme cs = dummyCondition.getColorScheme();
			cs.setTextColor(textChooserPanel.getColor(true));
			cs.setBackgroundColor(backgroundChooserPanel.getColor(true));
			cs.setBorderColor(borderChooserPanel.getColor(true));
			if(logger.isDebugEnabled()) logger.debug("initializing to {}...", dummyCondition);
			previewDummyRenderer.getTableCellRendererComponent(null, dummyCondition, false, false, 0, 0);
		}
	}
}
