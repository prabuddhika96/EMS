import axiosInstance from "../interceptor/axios-interceptor";
import type { LoginForm } from "../interface/Form";
import type { ApiResponse } from "../interface/response";

const login = async (formData: LoginForm): Promise<ApiResponse> => {
    try {
        const response = await axiosInstance.post('/api/v1/auth/login', formData);
        return {
            message: "Login successful.",
            data: response.data
        };
    } catch (err: any) {
        return {
            data: null,
            message: err.response?.data?.message || err.message || "Login failed",
        };
    }
}

export const authService = {
    login
}