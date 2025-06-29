import { useEffect, useState } from "react";
import type { Event, EventFilter } from "../../interface/Event";
import { eventService } from "../../service/eventService";
import { useNavigate } from "react-router-dom";
import { RouteName } from "../../constants/routeNames";
import EventCard from "../../components/EventCard/EventCard";
import GridTemplate from "../../components/Grid/GridTemplate";
import HeaderH2 from "../../components/Headers/HeaderH2/HeaderH2";
import "./styles.css";
import type { User } from "../../interface/User";

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

const initialFilterState: EventFilter = {
  endTime: "",
  hostId: "",
  startTime: "",
};

function Dashboard() {
  const navigate = useNavigate();
  const [page, setPage] = useState<number>(1);
  const [pageSize, setPageSize] = useState<number>(10);
  const [responseData, setResponseData] = useState<Response>(initialState);
  const [hostList, setHostList] = useState<User[]>([]);
  const [filters, setFilters] = useState<EventFilter>(initialFilterState);

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

  const fetchHosts = async (visibility?: string) => {
    try {
      const apiResponse: any = await eventService.getEventHostList(visibility);

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        if (apiResponse?.data?.code == 3004) {
          // console.log(apiResponse?.data?.data?.content);
          setHostList(apiResponse?.data?.data);
        }
      }
    } catch (error) {}
  };

  useEffect(() => {
    fetchEevents(page, pageSize);
    fetchHosts();
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

  // filter
  const handleFilterChnage = (name: keyof EventFilter, value: string) => {
    console.log(name, value);
    setFilters((prev: EventFilter) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleClickFilter = async () => {
    try {
      if (filters == initialFilterState) {
        alert("Please select at least one filter option before searching.");
      }
      const apiResponse: any = await eventService.filterEvents(
        page,
        pageSize,
        filters
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

  const handleClearFilter = () => {
    setFilters(initialFilterState);
    fetchEevents(1, pageSize);
  };

  return (
    <div>
      <div className="dashboard-filter-header">
        <HeaderH2 text="Upcoming Events" />

        <div className="dashboard-filter-header-right">
          <label>Start Time</label>
          <input
            type="datetime-local"
            value={filters.startTime}
            onChange={(e) => {
              handleFilterChnage("startTime", e.target.value);
            }}
          />

          <label>End Time</label>
          <input
            type="datetime-local"
            value={filters.endTime}
            onChange={(e) => {
              handleFilterChnage("endTime", e.target.value);
            }}
          />

          {hostList && hostList?.length > 0 && (
            <>
              <label>Host</label>
              <select
                value={filters.hostId}
                onChange={(e) => {
                  handleFilterChnage("hostId", e.target.value);
                }}
              >
                <option value="" disabled selected>
                  Select Host
                </option>

                {hostList?.map((host: User) => (
                  <option
                    value={host?.id?.toString()}
                    key={host?.id?.toString()}
                  >
                    {host?.name}
                  </option>
                ))}
              </select>
            </>
          )}

          <button className="dashboard-filter-btn" onClick={handleClickFilter}>
            Filter
          </button>

          <button className="dashboard-filter-btn" onClick={handleClearFilter}>
            Clear
          </button>
        </div>
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
