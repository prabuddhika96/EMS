import axiosInstance from "../interceptor/axios-interceptor";
import type { ApiResponse } from "../interface/response";


const servicePath = '/api/v1/events';


const getAllUpcomingEvents = async (page?: number, pageSize?: number): Promise<ApiResponse> => {
    try {
        let url = servicePath + '/filter'

        if (page && pageSize) {
            url += `?page=${page}size=${pageSize}`
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
            url += `?page=${page}size=${pageSize}`
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

export const eventService = {
    getAllUpcomingEvents, getEventById, fetchEventsByType
}