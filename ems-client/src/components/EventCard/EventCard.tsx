import React from "react";
import type { Event } from "../../interface/Event";
import "./style.css";

interface Props {
  event: Event;
}
function EventCard({ event }: Props) {
  return (
    <div className="event-card">
      <div className="event-header">
        <h2 className="event-title">{event.title}</h2>
        <span className={`event-badge ${event.visibility.toLowerCase()}`}>
          {event.visibility}
        </span>
      </div>

      <p className="event-description">{event.description}</p>

      <div className="event-details">
        <p>
          <strong>📍 Location:</strong> {event.location}
        </p>
        <p>
          <strong>🕒 Start Time:</strong>{" "}
          {new Date(event.startTime).toLocaleString()}
        </p>

        <p>
          <strong>🕒 End Time:</strong>{" "}
          {new Date(event.endTime).toLocaleString()}
        </p>
      </div>
    </div>
  );
}

export default EventCard;
