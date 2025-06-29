import { Calendar, CalendarPlus, LayoutDashboard, User } from "lucide-react";
import { RouteName } from "./routeNames";

export const NavData = [
    {
        name: "Dashboard",
        icon: LayoutDashboard,
        routeName: RouteName.Dashboard,
    },
    {
        name: "All Events",
        icon: Calendar,
        routeName: RouteName.Home,
    },
    {
        name: "Create Event",
        icon: CalendarPlus,
        routeName: RouteName.Home,
    },
    {
        name: "Profile",
        icon: User,
        routeName: RouteName.Home,
    },
];
