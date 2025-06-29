export interface Event {
    id: string;
    title: string;
    description: string;
    hostId: string;
    startTime: string;
    endTime: string;
    location: string;
    visibility: "PUBLIC" | "PRIVATE";
    createdAt?: string;
    updatedAt?: string;
}


export interface EventFilter {
    startTime: string;
    endTime: string;
    hostId: string;
}