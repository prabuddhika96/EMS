import React, { useState } from "react";
import type { Event } from "../../interface/Event";

function Dashboard() {
  const [page, setPage] = useState<number>(1);
  const [pageSize, setPageSize] = useState<number>(10);
  const [eventList, setEventList] = useState<Event[]>([]);
  return <div>Dashboard</div>;
}

export default Dashboard;
