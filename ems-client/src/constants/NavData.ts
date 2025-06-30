import { CalendarPlus, LayoutDashboard, Lock, User, type LucideIcon } from "lucide-react";
import { RouteName } from "./routeNames";

export interface NavItem {
    name: string;
    icon: LucideIcon;
    routeName: string;
    hasAccess: ("ADMIN" | "USER")[];
}

export const NavData: NavItem[] = [
    {
        name: "Dashboard",
        icon: LayoutDashboard,
        routeName: RouteName.Dashboard,
        hasAccess: ["ADMIN", "USER"],
    },
    {
        name: "Create Event",
        icon: CalendarPlus,
        routeName: RouteName.CreateEvent,
        hasAccess: ["ADMIN", "USER"],
    },
    {
        name: "Profile",
        icon: User,
        routeName: RouteName.Profile,
        hasAccess: ["ADMIN", "USER"],
    },
    {
        name: "Private Events",
        icon: Lock,
        routeName: RouteName.PrivateEvent,
        hasAccess: ["ADMIN", "USER"],
    },
    // {
    //     name: "Update Event",
    //     icon: CalendarPlus,
    //     routeName: RouteName.UpdateEvent,
    //     hasAccess: ["ADMIN", "USER"],
    // },
    {
        name: "User List",
        icon: CalendarPlus,
        routeName: RouteName.UserList,
        hasAccess: ["ADMIN"],
    },
];