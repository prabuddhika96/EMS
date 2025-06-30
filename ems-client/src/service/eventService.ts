import axiosInstance from "../interceptor/axios-interceptor";
import type { EventFilter } from "../interface/Event";
import type { CreateEventForm } from "../interface/Form";
import type { ApiResponse } from "../interface/response";
import { convertToCleanUTCISOString } from "../util/time-utils";


const servicePath = '/api/v1/events';


const getAllUpcomingEvents = async (page?: number, pageSize?: number): Promise<ApiResponse> => {
    try {
        let url = servicePath + '/upcoming'

        if (page && pageSize) {
            url += `?page=${page - 1}&size=${pageSize}`
        }
        const response = await axiosInstance.get(url);
        return {
            message: "",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Fetch failed",
        };
    }
}

const getEventById = async (eventId: string): Promise<ApiResponse> => {
    try {
        let url = servicePath + `/${eventId}`
        const response = await axiosInstance.get(url);
        return {
            message: "",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Fetch failed",
        };
    }
}

const fetchEventsByType = async (type: "hosting" | "attending", page?: number, pageSize?: number) => {
    try {
        let url = servicePath + `/user/${type}`

        if (page && pageSize) {
            url += `?page=${page - 1}&size=${pageSize}`
        }
        const response = await axiosInstance.get(url);
        return {
            message: "",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Fetch failed",
        };
    }
}

const createEvent = async (formdata: CreateEventForm) => {
    try {
        let url = servicePath + `/create`


        const response = await axiosInstance.post(url, formdata);
        return {
            message: "",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Fetch failed",
        };
    }
}

const updateEvent = async (formdata: CreateEventForm, eventId: string) => {
    try {
        let url = servicePath + `/update/${eventId}`


        const response = await axiosInstance.put(url, formdata);
        return {
            message: "",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Fetch failed",
        };
    }
}

const getEventHostList = async (visibility: string = "PUBLIC") => {
    try {
        let url = servicePath + `/hosts?visibility=${visibility}`


        const response = await axiosInstance.get(url);
        return {
            message: "",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Fetch failed",
        };
    }
};

const filterEvents = async (fillters: EventFilter, page?: number, pageSize?: number) => {
    try {
        let url = servicePath + `/filter?`

        if (page && pageSize) {
            url += `page=${page - 1}&size=${pageSize}&`
        }
        if (fillters?.startTime) {
            url += `startDate=${convertToCleanUTCISOString(fillters.startTime)}&`
        }
        if (fillters?.endTime) {
            url += `startDate=${convertToCleanUTCISOString(fillters.endTime)}&`
        }
        if (fillters?.hostId) {
            url += `hostId=${fillters.hostId}&`
        }
        if (fillters?.visibility) {
            url += `visibility=${fillters?.visibility}&`
        }
        if (fillters?.location) {
            url += `location=${fillters?.location}&`
        }

        const response = await axiosInstance.get(url);
        return {
            message: "",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Fetch failed",
        };
    }
}

const deleteEvent = async (eventId: string) => {
    try {
        let url = servicePath + `/delete/${eventId}`
        const response = await axiosInstance.delete(url);
        return {
            message: "",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Fetch failed",
        };
    }
}

export const eventService = {
    getAllUpcomingEvents, getEventById, fetchEventsByType, createEvent, getEventHostList, filterEvents, deleteEvent, updateEvent
}