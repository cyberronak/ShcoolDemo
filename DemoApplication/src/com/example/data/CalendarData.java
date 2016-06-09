package com.example.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarData {
	
	public enum EventType
	{
		Exam,
		Event,
		Holiday
	}
	
	public static List<CalendarData> events= new ArrayList<CalendarData>();
	
	private String eventId;
	private String eventTitle;
	private EventType eventType;
	private String eventDesc;
	private Date eventDate;
	
//	public List<CalendarData> getEvents() {
//		return events;
//	}
//	public void addEvents(CalendarData eventObj) {
//		events.add(eventObj);
//	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
}
