import React from "react";
import type { Event } from "../../interface/Event";
import "./style.css";

interface Props {
  event: Event;
  onClick?: (eventId: string) => void;
}
function EventCard({ event, onClick }: Props) {
  return (
    <div
      className="event-card stylish-card"
      onClick={() => {
        if (onClick) {
          onClick(event?.id);
        }
      }}
    >
      <div className="event-card-content">
        <div className="event-header">
          <h2 className="event-title">{event.title}</h2>
          <span className={`event-badge ${event.visibility.toLowerCase()}`}>
            {event.visibility}
          </span>
        </div>
        <p className="event-description">{event.description}</p>
        <div className="event-details">
          <div className="event-detail-item">
            <span className="event-detail-icon">📍</span>
            <span>{event.location}</span>
          </div>
          <div className="event-detail-item">
            <span className="event-detail-icon">🕒</span>
            <span>{new Date(event.startTime).toLocaleString()}</span>
          </div>
          <div className="event-detail-item">
            <span className="event-detail-icon">🕒</span>
            <span>{new Date(event.endTime).toLocaleString()}</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EventCard;
