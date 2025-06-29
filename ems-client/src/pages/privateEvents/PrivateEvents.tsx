import React, { useEffect, useState } from "react";
import type { Event } from "../../interface/Event";
import "./styles.css";
import EventCard from "../../components/EventCard/EventCard";
import GridTemplate from "../../components/Grid/GridTemplate";
import { eventService } from "../../service/eventService";
import { RouteName } from "../../constants/routeNames";
import { useNavigate } from "react-router-dom";
import type { User } from "../../interface/User";
import { useSelector } from "react-redux";
import type { RootState } from "../../redux/store";

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

function PrivateEvents() {
  const navigate = useNavigate();
  const loggedUser: User = useSelector((state: RootState) => state.user);
  const [page, setPage] = useState<number>(1);
  const [pageSize, setPageSize] = useState<number>(10);
  const [responseData, setResponseData] = useState<Response>(initialState);

  const fetchPrivateProjects = async (page: number, pageSize: number) => {
    try {
      const apiResponse: any = await eventService.filterEvents(
        {
          visibility: "PRIVATE",
          hostId: loggedUser.id || "",
        },
        page,
        pageSize
      );

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code == 3004) {
          // console.log(apiResponse?.data?.data?.content);
          setResponseData({
            eventList: apiResponse?.data?.data?.content,
            totalPages: apiResponse?.data?.data?.totalPages || 1,
            totalRecords: apiResponse?.data?.data?.totalElements || 0,
          });
        }
      }
    } catch (error) {}
  };

  useEffect(() => {
    fetchPrivateProjects(page, pageSize);
  }, []);

  const handlePageChange = (value: number) => {
    if (value == page) {
      return;
    }
    setPage(Number(value));
    fetchPrivateProjects(Number(value), pageSize);
  };

  const handleChangePageSize = (value: string) => {
    setPageSize(Number(value));
    setPage(1);
    fetchPrivateProjects(1, Number(value));
  };

  const handleEventClick = (eventId: string) => {
    navigate(RouteName.EventDetails.replace(":id", eventId));
  };
  return (
    <div>
      {responseData?.eventList && responseData?.eventList?.length > 0 ? (
        <GridTemplate
          totalPages={responseData?.totalPages}
          page={page}
          handlePageChange={handlePageChange}
          totalRecords={responseData?.totalRecords}
          handleChangePageSize={handleChangePageSize}
        >
          {responseData?.eventList.map((event: Event, index: number) => (
            <EventCard key={index} event={event} onClick={handleEventClick} />
          ))}
        </GridTemplate>
      ) : (
        <p
          style={{
            width: "100%",
            textAlign: "center",
          }}
        >
          You have not created any private events.
        </p>
      )}
    </div>
  );
}

export default PrivateEvents;
