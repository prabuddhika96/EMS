import { Calendar, CalendarPlus, LayoutDashboard, User } from "lucide-react";
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
        name: "All Events",
        icon: Calendar,
        routeName: RouteName.Home,
    },
];
