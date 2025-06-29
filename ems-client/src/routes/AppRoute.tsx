import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainLayout from "../layouts/MainLayout/MainLayout";
import { RouteName } from "../constants/routeNames";
import AuthLayout from "../layouts/AuthLayout/AuthLayout";
import Login from "../pages/login/Login";
import Dashboard from "../pages/dashboard/Dashboard";

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
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoute;
