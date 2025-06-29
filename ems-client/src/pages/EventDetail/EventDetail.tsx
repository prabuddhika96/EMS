import React, { useEffect, useState } from "react";
import "./style.css";
import { useParams } from "react-router-dom";
import { eventService } from "../../service/eventService";
import type { Event } from "../../interface/Event";

function EventDetail() {
  const { id }: any = useParams();
  const [event, setEvent] = useState<Event | null>(null);

  const fetchEvent = async (eventId: string) => {
    try {
      const apiResponse: any = await eventService.getEventById(eventId);

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code === 3003) {
          setEvent(apiResponse?.data?.data);
        }
      }
    } catch (error) {
      console.error("Unexpected error:", error);
    }
  };

  useEffect(() => {
    if (id) fetchEvent(id);
  }, [id]);

  return (
    <div className="event-detail-container">
      {event && (
        <>
          <h1 className="event-title">{event.title}</h1>
          <p className="event-description">{event.description}</p>

          <div className="event-meta">
            <p>
              <strong>Start:</strong>{" "}
              {new Date(event.startTime).toLocaleString()}
            </p>
            <p>
              <strong>End:</strong> {new Date(event.endTime).toLocaleString()}
            </p>
            <p>
              <strong>Location:</strong> {event.location}
            </p>
            <p>
              <strong>Visibility:</strong> {event.visibility}
            </p>
          </div>
        </>
      )}
    </div>
  );
}

export default EventDetail;
