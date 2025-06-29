import { useEffect, useState } from "react";
import type { Event } from "../../interface/Event";
import { eventService } from "../../service/eventService";
import { useNavigate } from "react-router-dom";
import { RouteName } from "../../constants/routeNames";
import EventCard from "../../components/EventCard/EventCard";
import GridTemplate from "../../components/Grid/GridTemplate";
import HeaderH2 from "../../components/Headers/HeaderH2/HeaderH2";

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

function Dashboard() {
  const navigate = useNavigate();
  const [page, setPage] = useState<number>(1);
  const [pageSize, setPageSize] = useState<number>(10);
  const [responseData, setResponseData] = useState<Response>(initialState);

  const fetchEevents = async (page: number, pageSize: number) => {
    try {
      const apiResponse: any = await eventService.getAllUpcomingEvents(
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
    fetchEevents(page, pageSize);
  }, []);

  const handlePageChange = (value: number) => {
    if (value == page) {
      return;
    }
    setPage(Number(value));
    fetchEevents(Number(value), pageSize);
  };

  const handleChangePageSize = (value: string) => {
    setPageSize(Number(value));
    setPage(1);
    fetchEevents(1, Number(value));
  };

  const handleEventClick = (eventId: string) => {
    navigate(RouteName.EventDetails.replace(":id", eventId));
  };

  return (
    <div>
      <div
        style={{
          marginLeft: "10px",
        }}
      >
        <HeaderH2 text="Upcoming Events" />
      </div>
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
        <></>
      )}
    </div>
  );
}

export default Dashboard;
