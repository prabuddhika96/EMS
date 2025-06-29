export interface LoginForm {
    email: string,
    password: string
}

export interface CreateEventForm {
    title: string;
    description: string;
    startTime: string;
    endTime: string;
    location: string;
    visibility: "PUBLIC" | "PRIVATE" | null;
}
