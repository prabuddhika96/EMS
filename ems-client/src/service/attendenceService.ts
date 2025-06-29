import axiosInstance from "../interceptor/axios-interceptor";
import type { ApiResponse } from "../interface/response";


const servicePath = '/api/v1/attendence';

const getAttendingUsersByEventId = async (eventId: string, page?: number, pageSize?: number): Promise<ApiResponse> => {
    try {
        let url = servicePath + `/${eventId}/users`

        if (page && pageSize) {
            url += `?page=${page}&size=${pageSize}`
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

const markAttend = async (eventId: string, status: string) => {
    try {
        let url = servicePath + `/${eventId}/attend`

        const response = await axiosInstance.post(url, {
            status: status
        });
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

const getAttendenceByEventId = async (eventId: string): Promise<ApiResponse> => {
    try {
        let url = servicePath + `/${eventId}/status`

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

export const attendenceService = {
    getAttendingUsersByEventId, markAttend, getAttendenceByEventId
}