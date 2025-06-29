import React, { useEffect, useState } from "react";
import type { IAlert as AlertType } from "../../interface/Alert";
import "./alert.css";

interface Props {
  alert: AlertType;
  autoDismiss?: boolean;
  dismissTime?: number;
}

function Alert({ alert, autoDismiss = true, dismissTime = 3000 }: Props) {
  const [visible, setVisible] = useState(!!alert.message);

  useEffect(() => {
    if (alert.message) {
      setVisible(true);
      if (autoDismiss) {
        const timer = setTimeout(() => {
          setVisible(false);
        }, dismissTime);
        return () => clearTimeout(timer);
      }
    }
  }, [alert.message, autoDismiss, dismissTime]);

  if (!visible || !alert.message) return null;

  return (
    <>
      {alert?.type == "error" ? (
        <div className={`ems-alert ems-alert-error`}>{alert.message}</div>
      ) : alert?.type == "info" ? (
        <div className={`ems-alert ems-alert-info`}>{alert.message}</div>
      ) : (
        <div className={`ems-alert ems-alert-success`}>{alert.message}</div>
      )}
    </>
  );
}

export default Alert;
