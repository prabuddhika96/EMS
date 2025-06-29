import { Calendar, CalendarPlus, LayoutDashboard, Lock, User } from "lucide-react";
import { RouteName } from "./routeNames";

export const NavData = [
    {
        name: "Dashboard",
        icon: LayoutDashboard,
        routeName: RouteName.Dashboard,
    },
    {
        name: "Create Event",
        icon: CalendarPlus,
        routeName: RouteName.CreateEvent,
    },
    {
        name: "Profile",
        icon: User,
        routeName: RouteName.Profile,
    },
    {
        name: "Private Events",
        icon: Lock,
        routeName: RouteName.PrivateEvent,
    },
];
