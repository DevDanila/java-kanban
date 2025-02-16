package com.practicum.java_kanban.http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd'T'HH:mm")
			.optionalStart()
			.appendPattern(":ss")
			.optionalEnd()
			.toFormatter();

	@Override
	public void write(JsonWriter out, LocalDateTime localDateTime) throws IOException {
		if (localDateTime == null) {
			out.nullValue();
		} else {
			out.value(formatter.format(localDateTime));
		}
	}

	@Override
	public LocalDateTime read(JsonReader in) throws IOException {
		if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		return LocalDateTime.parse(in.nextString(), formatter);
	}
}