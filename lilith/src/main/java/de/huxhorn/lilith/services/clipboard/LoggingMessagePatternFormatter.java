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
package de.huxhorn.lilith.services.clipboard;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.Message;

public class LoggingMessagePatternFormatter
	implements ClipboardFormatter
{
	private static final long serialVersionUID = -8422698763039005756L;

	public String getName()
	{
		return "Copy message pattern";
	}

	public String getDescription()
	{
		return "Copies the message pattern of the logging event to the clipboard.";
	}

	public String getAccelerator()
	{
		return null;
	}

	public boolean isCompatible(Object object)
	{
		return toString(object) != null;
	}

	public String toString(Object object)
	{
		if(object instanceof EventWrapper)
		{
			EventWrapper wrapper = (EventWrapper) object;
			if(wrapper.getEvent() != null)
			{
				Object eventObj = wrapper.getEvent();
				if(eventObj instanceof LoggingEvent)
				{
					LoggingEvent loggingEvent = (LoggingEvent) eventObj;
					Message messageObj = loggingEvent.getMessage();
					if(messageObj != null)
					{
						return messageObj.getMessagePattern();
					}
				}
			}
		}

		return null;
	}
}
