import axiosInstance from "../interceptor/axios-interceptor";
import type { ApiResponse } from "../interface/response";

const servicePath = '/api/v1/users';

const getUserList = async (page?: number, pageSize?: number): Promise<ApiResponse> => {
    try {
        let url = servicePath + '/user-list'

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

const updateUserRole = async (userId: string, role: "ADMIN" | "USER") => {
    try {
        let url = servicePath + `/change-role?userId=${userId}&newRole=${role}`
        const response = await axiosInstance.patch(url);
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

export const userService = {
    getUserList, updateUserRole
}