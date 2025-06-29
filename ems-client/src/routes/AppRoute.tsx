import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainLayout from "../layouts/MainLayout/MainLayout";
import { RouteName } from "../constants/routeNames";
import AuthLayout from "../layouts/AuthLayout/AuthLayout";
import Login from "../pages/login/Login";
import Dashboard from "../pages/dashboard/Dashboard";
import EventDetail from "../pages/EventDetail/EventDetail";
import Profile from "../pages/profile/Profile";
import CreateEvent from "../pages/createEvent/CreateEvent";
import PrivateEvents from "../pages/privateEvents/PrivateEvents";

function AppRoute() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path={RouteName.Home}
          element={<AuthLayout children={<Login />} />}
        />

        <Route
          path={RouteName.Dashboard}
          element={<MainLayout children={<Dashboard />} title={"Dashboard"} />}
        />

        <Route
          path={RouteName.EventDetails}
          element={
            <MainLayout children={<EventDetail />} title={"Event Details"} />
          }
        />

        <Route
          path={RouteName.Profile}
          element={<MainLayout children={<Profile />} title={"User Profile"} />}
        />

        <Route
          path={RouteName.CreateEvent}
          element={
            <MainLayout children={<CreateEvent />} title={"Create Event"} />
          }
        />

        <Route
          path={RouteName.PrivateEvent}
          element={
            <MainLayout children={<PrivateEvents />} title={"Private Events"} />
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoute;
