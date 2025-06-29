import React from "react";
import type { IAlert as AlertType } from "../../interface/Alert";

interface Props {
  alert: AlertType;
}
function Alert({ alert }: Props) {
  if (!alert.message) return null;

  return (
    <div className={`ems-alert ems-alert-${alert.type}`}>{alert.message}</div>
  );
}

export default Alert;
