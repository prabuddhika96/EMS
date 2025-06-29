import React, { useEffect, useState } from "react";
import "./style.css";
import { useParams } from "react-router-dom";
import { eventService } from "../../service/eventService";
import type { Event } from "../../interface/Event";
import HeaderH2 from "../../components/Headers/HeaderH2/HeaderH2";
import { attendenceService } from "../../service/attendenceService";
import type { AttendingUserResponse } from "../../interface/AttendingUserResponse";
import AttendeeTable from "./component/AttendeeTable";
import PaginationComponent from "../../components/Pagination/PaginationComponent";
import PageSizeComponent from "../../components/PageSizeSelector/PageSizeComponent";

interface Response {
  attendingUserList: AttendingUserResponse[];
  totalPages: number;
  totalRecords: number;
}

const initialState: Response = {
  attendingUserList: [],
  totalPages: 1,
  totalRecords: 0,
};
function EventDetail() {
  const { id }: any = useParams();
  const [event, setEvent] = useState<Event | null>(null);
  const [responseData, setResponseData] = useState<Response>(initialState);
  const [page, setPage] = useState<number>(1);
  const [pageSize, setPageSize] = useState<number>(10);

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
        await attendenceService.getAttendingUsersByEventId(
          eventId,
          page,
          pageSize
        );

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code === 4007) {
          setResponseData({
            attendingUserList: apiResponse?.data?.data?.content,
            totalPages: apiResponse?.data?.data?.totalPages || 1,
            totalRecords: apiResponse?.data?.data?.totalElements || 0,
          });
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

  const handlePageChange = (value: number) => {
    if (value == page) {
      return;
    }
    setPage(Number(value));
    fetchAttendingUserByEventId(id);
  };

  const handleChangePageSize = (value: string) => {
    setPageSize(Number(value));
    setPage(1);
    fetchAttendingUserByEventId(id);
  };

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

          <AttendeeTable attendingUserList={responseData.attendingUserList} />

          {responseData?.attendingUserList &&
            responseData.attendingUserList?.length > 0 && (
              <>
                <div className="pagination-row">
                  <PaginationComponent
                    totalPages={responseData?.totalPages}
                    currentPage={page}
                    onPageChnage={handlePageChange}
                  />
                  <PageSizeComponent
                    totalRecords={responseData?.totalRecords}
                    handleChangePageSize={handleChangePageSize}
                  />
                </div>
              </>
            )}

          {/* attending events */}
        </>
      )}
    </div>
  );
}

export default EventDetail;
