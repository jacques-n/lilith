/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2011 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Copyright 2007-2011 Joern Huxhorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.huxhorn.lilith.data.logging.logback;

import de.huxhorn.lilith.data.converter.Converter;
import de.huxhorn.sulky.codec.Encoder;
import de.huxhorn.lilith.data.eventsource.LoggerContext;

import ch.qos.logback.classic.spi.LoggingEvent;

import java.util.HashMap;
import java.util.Map;

public class TransformingEncoder
		implements Encoder<LoggingEvent>
{
	private Converter<de.huxhorn.lilith.data.logging.LoggingEvent> converter = new SameThreadLogbackLoggingConverter();
	private Encoder<de.huxhorn.lilith.data.logging.LoggingEvent> lilithEncoder;
	private String applicationIdentifier;
	private String uuid;

	public Encoder<de.huxhorn.lilith.data.logging.LoggingEvent> getLilithEncoder()
	{
		return lilithEncoder;
	}

	public void setLilithEncoder(Encoder<de.huxhorn.lilith.data.logging.LoggingEvent> lilithEncoder)
	{
		this.lilithEncoder = lilithEncoder;
	}

	public String getApplicationIdentifier()
	{
		return applicationIdentifier;
	}

	public void setApplicationIdentifier(String applicationIdentifier)
	{
		this.applicationIdentifier = applicationIdentifier;
	}

	public String getUUID()
	{
		return uuid;
	}

	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}

	public byte[] encode(LoggingEvent logbackEvent)
	{
		de.huxhorn.lilith.data.logging.LoggingEvent lilithEvent = converter.convert(logbackEvent);
		if(applicationIdentifier != null || uuid != null)
		{
			LoggerContext loggerContext = lilithEvent.getLoggerContext();
			if(loggerContext == null)
			{
				loggerContext=new LoggerContext();
			}
			Map<String, String> props = loggerContext.getProperties();
			if(props == null)
			{
				props = new HashMap<String, String>();
			}

			if(applicationIdentifier != null)
			{
				props.put(LoggerContext.APPLICATION_IDENTIFIER_PROPERTY_NAME, applicationIdentifier);
			}
			if(uuid != null)
			{
				props.put(LoggerContext.APPLICATION_UUID_PROPERTY_NAME, uuid);
			}

			loggerContext.setProperties(props);
			lilithEvent.setLoggerContext(loggerContext);
		}
		return lilithEncoder.encode(lilithEvent);
	}
}
