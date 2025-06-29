import React, { useEffect, useState } from "react";
import type { Event } from "../../interface/Event";
import { eventService } from "../../service/eventService";
import "./styles.css";
import EventCard from "../../components/EventCard/EventCard";
import PaginationComponent from "../../components/Pagination/PaginationComponent";

function Dashboard() {
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

  return (
    <div>
      {eventList && eventList?.length > 0 ? (
        <div>
          <div className="grid-container">
            <div className="dashboard-grid">
              {eventList.map((event: Event, index: number) => (
                <EventCard key={index} event={event} />
              ))}

              {eventList.map((event: Event, index: number) => (
                <EventCard key={index + eventList?.length} event={event} />
              ))}
            </div>
          </div>

          <div className="pagination-row">
            <PaginationComponent
              totalPages={totalPages}
              currentPage={page}
              onPageChnage={handlePageChange}
            />
            <p>pagination</p>
          </div>
        </div>
      ) : (
        <></>
      )}
    </div>
  );
}

export default Dashboard;
