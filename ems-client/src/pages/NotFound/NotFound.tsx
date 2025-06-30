import React from "react";
import { useNavigate } from "react-router-dom";
import "../Unauthorized/style.css";
import { RouteName } from "../../constants/routeNames";

function NotFound() {
  const navigate = useNavigate();

  return (
    <div className="unauthorized-page">
      <h1>404 - Page Not Found</h1>
      <p>The page you’re looking for doesn’t exist.</p>
      <button onClick={() => navigate(RouteName.Dashboard)}>Go to Home</button>
    </div>
  );
}

export default NotFound;
