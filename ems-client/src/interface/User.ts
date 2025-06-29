export interface User {
    id: string | null;
    name: string;
    email: string;
    role: "USER" | "ADMIN" | null
    createdAt: string | null;
    updatedAt: string | null;
}
