import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import type { RootState } from "../../redux/store";
import type { User } from "../../interface/User";
import "./style.css";
import HeaderH2 from "../../components/Headers/HeaderH2/HeaderH2";
import { eventService } from "../../service/eventService";
import GridTemplate from "../../components/Grid/GridTemplate";
import EventCard from "../../components/EventCard/EventCard";
import type { Event } from "../../interface/Event";

interface Response {
  eventList: Event[];
  totalPages: number;
  totalRecords: number;
}

const initialState: Response = {
  eventList: [],
  totalPages: 1,
  totalRecords: 0,
};

function Profile() {
  const loggedUser: User = useSelector((state: RootState) => state.user);
  const [pageHosting, setPageHosting] = useState<number>(1);
  const [pageSizeHosting, setPageSizeHosting] = useState<number>(10);
  const [responseDataHosting, setResponseDataHosting] =
    useState<Response>(initialState);

  const [pageAttending, setPageAttending] = useState<number>(1);
  const [pageSizeAttending, setPageSizeAttending] = useState<number>(10);
  const [responseDataAttending, setResponseDataAttending] =
    useState<Response>(initialState);

  const fetchEventsByType = async (
    type: "hosting" | "attending",
    page?: number,
    pageSize?: number
  ) => {
    try {
      const apiResponse: any = await eventService.fetchEventsByType(
        type,
        page,
        pageSize
      );

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code === 3004) {
          if (type == "attending") {
            setResponseDataAttending({
              eventList: apiResponse?.data?.data?.content,
              totalPages: apiResponse?.data?.data?.totalPages || 1,
              totalRecords: apiResponse?.data?.data?.totalElements || 0,
            });
          } else {
            setResponseDataHosting({
              eventList: apiResponse?.data?.data?.content,
              totalPages: apiResponse?.data?.data?.totalPages || 1,
              totalRecords: apiResponse?.data?.data?.totalElements || 0,
            });
          }
        }
      }
    } catch (error) {
      console.error("Unexpected error:", error);
    }
  };

  useEffect(() => {
    fetchEventsByType("attending", pageAttending, pageSizeAttending);
    fetchEventsByType("hosting", pageHosting, pageSizeHosting);
  }, []);

  const handlePageChangeHosting = (value: number) => {
    if (value == pageHosting) {
      return;
    }
    setPageHosting(Number(value));
    fetchEventsByType("hosting", Number(value), pageSizeHosting);
  };

  const handleChangePageSizeHosting = (value: string) => {
    setPageSizeHosting(Number(value));
    setPageHosting(1);
    fetchEventsByType("hosting", 1, Number(value));
  };

  const handlePageChangeAttending = (value: number) => {
    if (value == pageAttending) {
      return;
    }
    setPageAttending(Number(value));
    fetchEventsByType("attending", Number(value), pageSizeAttending);
  };

  const handleChangePageSizeAttending = (value: string) => {
    setPageSizeAttending(Number(value));
    setPageAttending(1);
    fetchEventsByType("attending", 1, Number(value));
  };

  return (
    <div className="profile-detail-container">
      <h1 className="profile-title">{loggedUser?.name}</h1>

      <div className="profile-meta-grid">
        <div className="profile-meta">
          <p>
            <strong>User ID :</strong> {loggedUser?.id}
          </p>
          <p>
            <strong>Email :</strong> {loggedUser?.email}
          </p>
        </div>
      </div>

      <HeaderH2 text={"Hosting Events"} />
      {responseDataHosting?.eventList &&
      responseDataHosting?.eventList?.length > 0 ? (
        <GridTemplate
          totalPages={responseDataHosting?.totalPages}
          page={pageHosting}
          handlePageChange={handlePageChangeHosting}
          totalRecords={responseDataHosting?.totalRecords}
          handleChangePageSize={handleChangePageSizeHosting}
          ignoreHeight={true}
        >
          {responseDataHosting?.eventList.map((event: Event, index: number) => (
            <EventCard key={index} event={event} />
          ))}

          {responseDataHosting?.eventList.map((event: Event, index: number) => (
            <EventCard key={index} event={event} />
          ))}
        </GridTemplate>
      ) : (
        <h2>No Events Found</h2>
      )}

      <HeaderH2 text={"Attending Events"} />
      {responseDataAttending?.eventList &&
      responseDataAttending?.eventList?.length > 0 ? (
        <GridTemplate
          totalPages={responseDataAttending?.totalPages}
          page={pageAttending}
          handlePageChange={handlePageChangeAttending}
          totalRecords={responseDataAttending?.totalRecords}
          handleChangePageSize={handleChangePageSizeAttending}
          ignoreHeight={true}
        >
          {responseDataAttending?.eventList.map(
            (event: Event, index: number) => (
              <EventCard key={index} event={event} />
            )
          )}

          {responseDataAttending?.eventList.map(
            (event: Event, index: number) => (
              <EventCard key={index} event={event} />
            )
          )}
        </GridTemplate>
      ) : (
        <h2>No Events Found</h2>
      )}
    </div>
  );
}

export default Profile;
