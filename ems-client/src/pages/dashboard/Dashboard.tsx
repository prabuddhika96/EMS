import React, { useEffect, useState } from "react";
import type { Event } from "../../interface/Event";
import { eventService } from "../../service/eventService";

function Dashboard() {
  const [page, setPage] = useState<number>(1);
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
        console.log(apiResponse?.data);
        if (apiResponse?.data?.code == 3004) {
          console.log(apiResponse?.data?.data?.content);
        }
      }
    } catch (error) {}
  };

  useEffect(() => {
    fetchEevents();
  }, []);
  return <div>Dashboard</div>;
}

export default Dashboard;
