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
package de.huxhorn.lilith.conditions;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.Message;

public class MessagePatternContainsCondition
	implements LilithCondition, SearchStringCondition
{
	private static final long serialVersionUID = -4701718332615561032L;

	public static final String DESCRIPTION="messagePattern.contains";

	private String searchString;

	public MessagePatternContainsCondition()
	{
		this(null);
	}

	public MessagePatternContainsCondition(String searchString)
	{
		this.searchString = searchString;
	}

	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}

	public String getSearchString()
	{
		return searchString;
	}

	public boolean isTrue(Object value)
	{
		if(searchString == null)
		{
			return false;
		}
		if(searchString.length() == 0)
		{
			return true;
		}
		if(value instanceof EventWrapper)
		{
			EventWrapper wrapper = (EventWrapper) value;
			Object eventObj = wrapper.getEvent();
			if(eventObj instanceof LoggingEvent)
			{
				LoggingEvent event = (LoggingEvent) eventObj;

				String message = null;
				Message messageObj = event.getMessage();
				if(messageObj != null)
				{
					message = messageObj.getMessagePattern();
				}

				return message != null && message.contains(searchString);
			}
		}
		return false;
	}

	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		final MessagePatternContainsCondition that = (MessagePatternContainsCondition) o;

		return !(searchString != null ? !searchString.equals(that.searchString) : that.searchString != null);
	}

	public int hashCode()
	{
		int result;
		result = (searchString != null ? searchString.hashCode() : 0);
		return result;
	}

	public MessagePatternContainsCondition clone()
		throws CloneNotSupportedException
	{
		return (MessagePatternContainsCondition) super.clone();
	}

	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(getDescription()).append("(");
		if(searchString != null)
		{
			result.append("\"");
			result.append(searchString);
			result.append("\"");
		}
		else
		{
			result.append("null");
		}
		result.append(")");
		return result.toString();
	}

	public String getDescription()
	{
		return DESCRIPTION;
	}
}
