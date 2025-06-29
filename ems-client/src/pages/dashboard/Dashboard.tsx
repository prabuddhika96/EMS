import { useEffect, useState } from "react";
import type { Event } from "../../interface/Event";
import { eventService } from "../../service/eventService";
import { useNavigate } from "react-router-dom";
import { RouteName } from "../../constants/routeNames";
import EventCard from "../../components/EventCard/EventCard";
import GridTemplate from "../../components/Grid/GridTemplate";

function Dashboard() {
  const navigate = useNavigate();
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [pageSize, setPageSize] = useState<number>(10);
  const [eventList, setEventList] = useState<Event[]>([]);

  const fetchEevents = async () => {
    try {
      const apiResponse: any = await eventService.getAllUpcomingEvents(
        page - 1,
        pageSize
      );

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code == 3004) {
          // console.log(apiResponse?.data?.data?.content);
          setEventList(apiResponse?.data?.data?.content);
          setTotalPages(apiResponse?.data?.data?.totalPages);
        }
      }
    } catch (error) {}
  };

  useEffect(() => {
    fetchEevents();
  }, []);

  const handlePageChange = (value: number) => {
    if (value == page) {
      return;
    }
    setPage(Number(value));
    fetchEevents();
  };

  const handleEventClick = (eventId: string) => {
    navigate(RouteName.EventDetails.replace(":id", eventId));
  };

  return (
    <div>
      {eventList && eventList?.length > 0 ? (
        <GridTemplate
          totalPages={totalPages}
          page={page}
          handlePageChange={handlePageChange}
        >
          {eventList.map((event: Event, index: number) => (
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
