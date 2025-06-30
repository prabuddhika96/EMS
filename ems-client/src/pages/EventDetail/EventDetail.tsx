import React, { useEffect, useState } from "react";
import "./style.css";
import { useNavigate, useParams } from "react-router-dom";
import { eventService } from "../../service/eventService";
import type { Event } from "../../interface/Event";
import HeaderH2 from "../../components/Headers/HeaderH2/HeaderH2";
import { attendenceService } from "../../service/attendenceService";
import type { AttendingUserResponse } from "../../interface/AttendingUserResponse";
import AttendeeTable from "./component/AttendeeTable";
import PaginationComponent from "../../components/Pagination/PaginationComponent";
import PageSizeComponent from "../../components/PageSizeSelector/PageSizeComponent";
import type { IAlert } from "../../interface/Alert";
import Alert from "../../components/Alert/Alert";
import type { User } from "../../interface/User";
import { useSelector } from "react-redux";
import type { RootState } from "../../redux/store";
import { RouteName } from "../../constants/routeNames";

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
  const navigate = useNavigate();
  const { id }: any = useParams();
  const loggedUser: User = useSelector((state: RootState) => state.user);
  const [event, setEvent] = useState<Event | null>(null);
  const [responseData, setResponseData] = useState<Response>(initialState);
  const [page, setPage] = useState<number>(1);
  const [pageSize, setPageSize] = useState<number>(10);
  const [loading, setLoading] = useState<boolean>(false);
  const [alert, setAlert] = useState<IAlert | null>(null);
  const [status, setStatus] = useState<string>("");

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

  const fetchAttendenceByEventId = async (eventId: string) => {
    try {
      const apiResponse: any = await attendenceService.getAttendenceByEventId(
        eventId
      );

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code === 4004) {
          setStatus(apiResponse?.data?.data);
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
      fetchAttendenceByEventId(id);
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

  const handleStatus = async (value: string) => {
    try {
      setStatus(value);
      const apiResponse: any = await attendenceService.markAttend(id, value);

      if (apiResponse instanceof Error) {
        setLoading(false);
        console.error("Failed to retrieve data:", apiResponse.message);
        setAlert({
          message:
            apiResponse?.message?.toString() || "An unknown error occurred.",
          type: "error",
        });
      } else {
        setLoading(false);
        if (apiResponse?.data?.code === 4000) {
          setAlert({
            message: apiResponse?.data?.message || "Event created successfully",
            type: "success",
          });
        } else {
          setAlert({
            message: apiResponse?.data?.message,
            type: "info",
          });
        }

        const timer = setTimeout(() => {
          setAlert(null);
        }, 3000);
        return () => clearTimeout(timer);
      }
    } catch (error) {
      console.error("Unexpected error:", error);
    }
  };

  const handleDeleteEvent = async () => {
    try {
      const apiResponse: any = await eventService.deleteEvent(id);

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
        setAlert({
          message:
            apiResponse?.message?.toString() || "An unknown error occurred.",
          type: "error",
        });
      } else {
        if (apiResponse?.data?.code === 3002) {
          setAlert({
            message: apiResponse?.data?.message || "Event deleted successfully",
            type: "success",
          });
        } else {
          setAlert({
            message: apiResponse?.data?.message,
            type: "info",
          });
        }

        const timer = setTimeout(() => {
          setAlert(null);
          navigate(-1);
        }, 1500);
        return () => clearTimeout(timer);
      }
    } catch (error) {
      console.error("Unexpected error:", error);
    }
  };

  const handleUpdateEvent = () => {
    navigate(RouteName.UpdateEvent.replace(":id", id));
  };

  return (
    <div className="event-detail-container">
      {event && (
        <>
          <h1 className="event-title">
            <span className="event-title-span">Event Name :</span> {event.title}
          </h1>
          <p className="event-description">{event.description}</p>

          {alert && <Alert alert={alert} />}

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

            <div className="event-meta">
              <p>
                <strong>Status:</strong>

                <select
                  value={status}
                  onChange={(e) => {
                    handleStatus(e?.target?.value);
                  }}
                  disabled={loading}
                >
                  <option value="" disabled>
                    Select Status
                  </option>
                  <option value="GOING">GOING</option>
                  <option value="MAYBE">MAYBE</option>
                  <option value="DECLINED">DECLINED</option>
                </select>
              </p>

              {(event?.hostId == loggedUser?.id ||
                loggedUser?.role == "ADMIN") && (
                <button
                  className="delete-btn-btn"
                  id="update-btn"
                  onClick={handleUpdateEvent}
                >
                  Update Event
                </button>
              )}

              {(event?.hostId == loggedUser?.id ||
                loggedUser?.role == "ADMIN") && (
                <button className="delete-btn-btn" onClick={handleDeleteEvent}>
                  Delete Event
                </button>
              )}
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
        </>
      )}
    </div>
  );
}

export default EventDetail;
