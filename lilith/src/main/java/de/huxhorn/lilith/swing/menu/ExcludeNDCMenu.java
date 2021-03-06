/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2013 Joern Huxhorn
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
package de.huxhorn.lilith.swing.menu;

import de.huxhorn.lilith.swing.ViewContainer;
import de.huxhorn.lilith.swing.actions.FilterAction;
import de.huxhorn.lilith.swing.actions.FocusNDCAction;
import de.huxhorn.lilith.swing.actions.FocusNDCPatternAction;
import de.huxhorn.lilith.swing.actions.NegateFilterAction;

public class ExcludeNDCMenu
	extends FocusNDCMenu
{
	private static final long serialVersionUID = 1051797757745679700L;

	@Override
	protected FilterAction createMessageAction(ViewContainer viewContainer, String message)
	{
		return new NegateFilterAction(new FocusNDCAction(viewContainer, message));
	}

	@Override
	protected FilterAction createMessagePatternAction(ViewContainer viewContainer, String pattern)
	{
		return new NegateFilterAction(new FocusNDCPatternAction(viewContainer, pattern));
	}
}
