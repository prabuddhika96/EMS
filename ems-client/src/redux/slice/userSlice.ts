import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { User } from "../../interface/User";

const initialUserState: User = {
    id: null,
    name: "",
    email: "",
    role: null,
    createdAt: null,
    updatedAt: null,
};

export const userSlice = createSlice({
    name: "user",
    initialState: initialUserState,
    reducers: {
        setUser: (state: User, action: PayloadAction<User>) => {
            return { ...state, ...action.payload };
        },
        logoutUser: (state) => {
            return { ...state, ...initialUserState };
        },
    }
})

export const {
    setUser, logoutUser
} = userSlice.actions;

export default userSlice.reducer;