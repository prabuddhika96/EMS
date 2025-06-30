import React from "react";
import { useNavigate } from "react-router-dom";
import "./style.css";
import { RouteName } from "../../constants/routeNames";

const Unauthorized = () => {
  const navigate = useNavigate();

  return (
    <div className="unauthorized-page">
      <h1>403 - Unauthorized</h1>
      <p>You don’t have permission to access this page.</p>
      <button onClick={() => navigate(RouteName.Dashboard)}>Go to Home</button>
    </div>
  );
};

export default Unauthorized;
