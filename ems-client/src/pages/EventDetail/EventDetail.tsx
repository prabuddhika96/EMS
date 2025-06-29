import React, { useEffect, useState } from "react";
import "./style.css";
import { useParams } from "react-router-dom";
import { eventService } from "../../service/eventService";
import type { Event } from "../../interface/Event";
import HeaderH2 from "../../components/Headers/HeaderH2/HeaderH2";
import { attendenceService } from "../../service/attendenceService";
import type { AttendingUserResponse } from "../../interface/AttendingUserResponse";
import AttendeeTable from "./component/AttendeeTable";

function EventDetail() {
  const { id }: any = useParams();
  const [event, setEvent] = useState<Event | null>(null);
  const [attendingUserList, setAttendingUserList] = useState<
    AttendingUserResponse[]
  >([]);

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

  const fetchAttendingUserByEventId = async (eventId: string) => {
    try {
      const apiResponse: any =
        await attendenceService.getAttendingUsersByEventId(eventId);

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code === 4007) {
          setAttendingUserList(apiResponse?.data?.data?.content);
        }
      }
    } catch (error) {
      console.error("Unexpected error:", error);
    }
  };

  useEffect(() => {
    if (id) {
      fetchEvent(id);
      fetchAttendingUserByEventId(id);
    }
  }, [id]);

  return (
    <div className="event-detail-container">
      {event && (
        <>
          <h1 className="event-title">{event.title}</h1>
          <p className="event-description">{event.description}</p>

          <div className="event-meta-grid">
            <div className="event-meta">
              <p>
                <strong>Start:</strong>{" "}
                {new Date(event.startTime).toLocaleString()}
              </p>
              <p>
                <strong>End:</strong> {new Date(event.endTime).toLocaleString()}
              </p>
            </div>

            <div className="event-meta">
              <p>
                <strong>Location:</strong> {event.location}
              </p>
              <p>
                <strong>Visibility:</strong> {event.visibility}
              </p>
            </div>
          </div>

          <HeaderH2 text={"Attending Users"} />

          <AttendeeTable attendingUserList={attendingUserList} />

          {/* attending events */}
        </>
      )}
    </div>
  );
}

export default EventDetail;
