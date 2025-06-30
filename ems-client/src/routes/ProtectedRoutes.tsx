import React, { useEffect, useLayoutEffect, type ReactNode } from "react";
import { useNavigate } from "react-router-dom";
import { RouteName } from "../constants/routeNames";

interface ProtectedRoutesProps {
  allowedRoles: string[];
  children: ReactNode;
  loggedUserRole: string;
}

function ProtectedRoutes({
  allowedRoles,
  children,
  loggedUserRole,
}: ProtectedRoutesProps) {
  const navigate = useNavigate();

  const hasAccess = allowedRoles?.includes(loggedUserRole);

  useEffect(() => {
    if (!hasAccess && allowedRoles?.length > 0) {
      navigate(RouteName.Unauthorized);
    }
  }, [hasAccess]);

  return hasAccess || allowedRoles?.length === 0 ? <>{children}</> : null;
}

export default ProtectedRoutes;
