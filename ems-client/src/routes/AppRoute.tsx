import React, { lazy, Suspense } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainLayout from "../layouts/MainLayout/MainLayout";
import AuthLayout from "../layouts/AuthLayout/AuthLayout";
import { RouteName } from "../constants/routeNames";
import Loader from "../components/Loader/Loader";

const Login = lazy(() => import("../pages/login/Login"));
const Dashboard = lazy(() => import("../pages/dashboard/Dashboard"));
const EventDetail = lazy(() => import("../pages/EventDetail/EventDetail"));
const Profile = lazy(() => import("../pages/profile/Profile"));
const CreateEvent = lazy(() => import("../pages/createEvent/CreateEvent"));
const PrivateEvents = lazy(
  () => import("../pages/privateEvents/PrivateEvents")
);

const LoadingFallback = () => <Loader />;

function AppRoute() {
  return (
    <BrowserRouter>
      <Suspense fallback={<LoadingFallback />}>
        <Routes>
          <Route
            path={RouteName.Home}
            element={<AuthLayout children={<Login />} />}
          />

          <Route
            path={RouteName.Dashboard}
            element={
              <MainLayout children={<Dashboard />} title={"Dashboard"} />
            }
          />

          <Route
            path={RouteName.EventDetails}
            element={
              <MainLayout children={<EventDetail />} title={"Event Details"} />
            }
          />

          <Route
            path={RouteName.Profile}
            element={
              <MainLayout children={<Profile />} title={"User Profile"} />
            }
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
              <MainLayout
                children={<PrivateEvents />}
                title={"Private Events"}
              />
            }
          />
        </Routes>
      </Suspense>
    </BrowserRouter>
  );
}

export default AppRoute;
