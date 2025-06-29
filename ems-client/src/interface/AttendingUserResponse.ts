export interface AttendingUserResponse {
    userId: string;
    name: string;
    email: string;
    status: 'GOING' | 'MAYBE' | 'DECLINED';
    respondedAt: string;
}
