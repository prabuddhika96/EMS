import React from "react";
import "./style.css";
import { useNavigate } from "react-router-dom";
import { RouteName } from "../../constants/routeNames";

function Sidebar() {
  const navigate = useNavigate();

  const handleClickLogo = () => {
    navigate(RouteName.Dashboard);
  };
  return (
    <div className="sidebar-container">
      <h2 className="">EMS</h2>

      <p>DAshboard</p>
      <p>DAshboard</p>
      <p>DAshboard</p>
      <p>DAshboard</p>
      <p>DAshboard</p>
      <p>DAshboard</p>
    </div>
  );
}

export default Sidebar;
